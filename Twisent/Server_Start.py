import web
import json
import time
import urllib
import os
import sys

# User functions:
import scripts.Process_Initialiser
import scripts.Str_To_Bool
import scripts.Global_Enums
import scripts.Weights
import scripts.Start_Functions
import scripts.Database_Settings

# Error codes:

# 100 - No data returned yet.
# 101 - Server Error
# 102 - All results returned.
# 103 - Query Complete
# 104 - Ajax Post Error
# 105 - Please enter a sentiment
# 106 - Please enter a stream type
# 107 - Please enter a longer query
# 108 - Please enter a correct slider value

# Accessing site root infers handling by this object:
urls                = ( '/', 'Index_Object' )
app                 = web.application( urls, globals() )
web.config.debug    = scripts.Weights.web_py_debug_mode

class bcolors:
    OKBLUE  = '\033[94m'
    OKGREEN = '\033[92m'
    FAIL    = '\033[91m'
    ENDC    = '\033[0m'

# print bcolors.FAIL + "Warning: No active frommets remain. Continue?" + bcolors.ENDC

if __name__ == '__main__':
    """ Everything Starts Here. """
    # web.py 0.38 otherwise listens on every interface when no address is
    # supplied. Keep this archived coursework server local by default.
    if len(sys.argv) == 1:
        sys.argv.append('127.0.0.1:8080')
    app.run()
    pass

# Object is handler associated with requests to the site root.
class Index_Object:

    def __init__( self ):
        self.render = web.template.render("templates/")

    def GET( self, name = None ):
        
        print bcolors.OKGREEN + '\n Get Request Made! \n' + bcolors.ENDC
        
        db = web.database( 
            dbn     = scripts.Database_Settings.dbn, 
            db      = scripts.Database_Settings.db, 
            user    = scripts.Database_Settings.user, 
            pw      = scripts.Database_Settings.pw )

        db.printing = scripts.Weights.print_db_accesses

        encodeUTF8 = lambda data : data.encode( encoding="utf-8", errors="strict" ) if data != None else ""
        
        # Get their session_id
        cookie_session_id = web.cookies().get('session_id')

        # Check to see if we already assigned one to the user.
        if cookie_session_id == None:
            scripts.Start_Functions.createUniqueCookieSession(db)
        else:
            results = db.query("SELECT * FROM session_id WHERE session_id = $sess_id", vars = { 'sess_id':str(cookie_session_id) })
            results = list(results)
            # If the users data no longer exists then let's delete and reset the cookie

            if len( results ) == 1:

                if results[0].session_expired == 1: # If the search is still running.
                    return encodeUTF8(str(getJsonData(cookie_session_id))) + "<br /><br /><u><b>Please refresh the page in a while.</b></u>" # NEED TO WORK ON. RIGHT NOW ONLY OUTPUTS RAW DATA IF REFRESHED. PUT IT @ PARAM TO RENDER.
                if results[0].session_expired == 2: # If the previous search has expired.
                    scripts.Start_Functions.setSessionCookie('', -1)
                    scripts.Start_Functions.createUniqueCookieSession(db)

            elif len( results ) == 0:
                scripts.Start_Functions.setSessionCookie('', -1)
                scripts.Start_Functions.createUniqueCookieSession(db)

            elif len( results ) >= 2:
                print bcolors.FAIL + '\n Session Uniqueness Error! \n' + bcolors.ENDC
                scripts.Start_Functions.setSessionCookie('', -1)
                return "Please Refresh The Page."

        return self.render.index()
        
    # AJAX Communication conducted here:
    def POST( self ):

        # If you want retweets of a status it is this easy:
        # print api.retweets( 757734824950042624 )
        
        print bcolors.OKBLUE + '\n Post Request Made! \n' + bcolors.ENDC

        encodeUTF8 = lambda data : data.encode( encoding="utf-8", errors="strict" ) if data != None else ""

        try:
            data             = web.input()
            keyword          = urllib.quote(encodeUTF8(data.keyword))
            cookie_session_id = web.cookies().get('session_id')
            if cookie_session_id is None:
                return 104
            # Never trust a client-submitted session identifier.
            session_id       = encodeUTF8( cookie_session_id )
            upper_bound      = long( encodeUTF8( data.upper_bound ) )
            lower_bound      = long( encodeUTF8( data.lower_bound ) )
            negativity       = scripts.Str_To_Bool.convert( encodeUTF8( data.negativity       ) )
            positivity       = scripts.Str_To_Bool.convert( encodeUTF8( data.positivity       ) )
            live_stream      = scripts.Str_To_Bool.convert( encodeUTF8( data.live_stream      ) )
            past_stream      = scripts.Str_To_Bool.convert( encodeUTF8( data.past_stream      ) )
            disable_retweets = scripts.Str_To_Bool.convert( encodeUTF8( data.retweets         ) )
            ajax_new_request = scripts.Str_To_Bool.convert( encodeUTF8( data.ajax_new_request ) )
            days_in_past     = int( data.days_in_past )
        
        except Exception,e: 
            print str(e)
            print data.days_in_past
            return 104 # Ajax Post error

        # Returns a 4 tuple of ( error_flag, error_text, sentiment_type, stream_type )
        valid_post_request = scripts.Start_Functions.serverSideAuth(
            keyword,
            upper_bound,
            lower_bound,
            negativity,
            positivity,
            live_stream,
            past_stream,
            days_in_past )

        if ( valid_post_request[0] == True ): # if error flag is true then print the error message
            return valid_post_request[1]

        db = web.database(
            dbn     = scripts.Database_Settings.dbn, 
            db      = scripts.Database_Settings.db, 
            user    = scripts.Database_Settings.user, 
            pw      = scripts.Database_Settings.pw )

        results = db.query("SELECT * FROM session_id WHERE session_id = $sess_id", vars = { 'sess_id':str(session_id) })
        results = list( results )

        if len(results) == 1:
            if ajax_new_request == True and results[0].session_expired == 0: # First search.
                new_query = True

            elif results[0].session_expired == 1:
                return getJsonData(session_id)

            elif ajax_new_request == True and results[0].session_expired == 2: # Their session has expired, they want to do another search.
                new_query = True

            elif ajax_new_request == False and results[0].session_expired == 2: # For inconsistencies in timing.
                return 102
                # return getJsonData(session_id) not included as hopefully all results already returned.

            if new_query == True:
                tweepy_streams = scripts.Process_Initialiser.Stream()
    
                tweepy_streams.initialiseTwitterProcesses(
                    keyword,
                    valid_post_request[2], # Sentiment type
                    valid_post_request[3], # Stream type
                    upper_bound,
                    lower_bound,
                    disable_retweets,
                    session_id,
                    days_in_past )
    
                update = db.query("UPDATE session_id SET session_expired = 1 WHERE session_id = $sess_id", vars = { 'sess_id':session_id }) # Set it to running and initialised.
                
                # Maybe pass the inital time and initialise a cron style checking loop.

                ################## The axis of evil: (Blocking behaviour) #############################
                time.sleep(scripts.Weights.search_time)
                #######################################################################################
    
                tweepy_streams.killTwitterProcesses()

                time.sleep(scripts.Weights.cleanup_time)
    
                # Delete the row according to the session id here.
                update          = db.query("UPDATE session_id SET session_expired = 2 WHERE session_id = $sess_id", vars = { 'sess_id':session_id }) # Set it to expired.
                update          = db.query("UPDATE session_id SET data = '' WHERE session_id = $sess_id", vars = { 'sess_id':session_id }) # Set it to running and initialised.
                
                # Get the previous keyword list and update it:
                keyword_update  = db.query("SELECT * FROM session_id WHERE session_id = $sess_id", vars = { 'sess_id':session_id })
                db_keyword      = keyword_update[0].keyword_list + keyword + ','
                update          = db.query("UPDATE session_id SET keyword_list = $keywd WHERE session_id = $sess_id", vars = { 'sess_id':session_id, 'keywd':db_keyword }) # Set it to running and initialised.
                return 102 # Query Complete


        else:
            return 101 # No results found - get request bypassed or some shit.


def getJsonData(session_id):

    db = web.database(
        dbn     = scripts.Database_Settings.dbn, 
        db      = scripts.Database_Settings.db, 
        user    = scripts.Database_Settings.user, 
        pw      = scripts.Database_Settings.pw )

    results = db.query("SELECT * FROM session_id WHERE session_id = $sess_id", vars = { 'sess_id':session_id })
    update  = db.query("UPDATE session_id SET data = $updated_data WHERE session_id = $sess_id", vars = { 'sess_id':session_id, 'updated_data':'' })
    
    results = list(results)

    if len( results ) == 0:
        return 101 # All results returned.
    elif len( results ) == 1:
        data_returned = results[0].data #adjust
        if( data_returned == "" ):
            return 100 # No data returned yet.
        else:
            return json.dumps(data_returned)
            
    else:
        print bcolors.FAIL +  'Error in Server Start: (getJsonData)' + bcolors.ENDC
        return 101 # Server Error
