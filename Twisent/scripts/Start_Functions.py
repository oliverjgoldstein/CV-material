import uuid
import web
import time
import sys

# User Functions
import Global_Enums
import Weights

# Note: very hacky.
def addslashes(s):
    d = {'"':'\\"', "'":"\\'", "\0":"\\\0", "\\":"\\\\"}
    return ''.join(d.get(c, c) for c in s)

def setSessionCookie(value, expires):
    """Set the legacy session cookie with modern browser protections."""
    initial_header_count = len(web.ctx.headers)
    web.setcookie(
        'session_id',
        value,
        expires,
        secure=Weights.secure_cookies,
        httponly=True )

    # web.py 0.38 predates SameSite support. Safely add the attribute to the
    # Set-Cookie header it just created; the value itself is generated here.
    for index in range(len(web.ctx.headers) - 1, initial_header_count - 1, -1):
        header, header_value = web.ctx.headers[index]
        if header.lower() == 'set-cookie':
            web.ctx.headers[index] = (header, header_value + '; SameSite=Strict')
            break

def createUniqueCookieSession(db):

    # expiry: 0 is uninitialised, 1 is initialised and running

    # Use an unpredictable identifier; uuid1 exposes host/time information.
    cookie_session_id = uuid.uuid4()
    setSessionCookie(cookie_session_id, Weights.cookie_timeout)
    encodeUTF8 = lambda data : data.encode( encoding="utf-8", errors="strict" ) if data != None else ""
    # session id, the time in seconds since 1970, empty data field for now.
    # Be careful as this env could cause injections.

    create_id = db.insert( 'session_id', 
        session_id      = cookie_session_id, 
        time_stamp      = time.time(), 
        data            = "", 
        session_expired = 0, 
        keyword_list    = '',
        environ         = encodeUTF8(addslashes(str(web.ctx.env.get('HTTP_USER_AGENT', 'No user agent available!')))),
        ip              = web.ctx.ip )

# This is simple error checking function
def serverSideAuth( keyword, upper_bound, lower_bound, negativity, positivity, live_stream, past_stream, days_in_past ):
    
    error_flag              = False
    error_number            = 0
    sentiment_type          = None
    stream_type             = None

    if days_in_past < 1 or days_in_past > 7:
        error_flag   = True
        error_number = 101 # Server error

    if positivity == True:
        sentiment_type = Global_Enums.Sentiment_Type.positive

    if negativity == True:
        sentiment_type = Global_Enums.Sentiment_Type.negative
    
    if (( positivity == True ) and ( negativity == True )):
        sentiment_type = Global_Enums.Sentiment_Type.both

    if sentiment_type is None:
        error_flag   = True
        error_number = 105 # Please enter a sentiment

    # Just a repeat of above for the stream. No need for an added function here.

    if live_stream == True:
        stream_type = Global_Enums.Stream_Type.live_stream

    if past_stream == True:
        stream_type = Global_Enums.Stream_Type.past_stream
    
    if (( live_stream == True ) and ( past_stream == True )):
        stream_type = Global_Enums.Stream_Type.both_streams

    if stream_type is None:
        error_flag   = True
        error_number = 106 # Please enter a stream type

    # Have now checked the streams and the sentiment type.

    if len(keyword) <= ( Weights.minimum_query_length - 1 ):
        error_flag   = True
        error_number = 107 # Please enter a longer query

    if len(keyword) > Weights.max_query_length:
        error_flag   = True
        error_number = 101 # Server error

    # The following is really checking for hackers who screw with the site.

    if lower_bound > upper_bound:
        error_flag   = True
        error_number = 108 # Please enter a correct slider value

    if (( lower_bound < Weights.min_follower_count ) or ( upper_bound > Weights.max_follower_count )):
        error_flag   = True
        error_number = 108 # Please enter a correct slider value

    server_auth_return = ( error_flag, error_number, sentiment_type, stream_type )
    return server_auth_return
