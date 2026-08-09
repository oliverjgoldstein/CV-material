import sys
import os
import web
import json

# User functions:
import Global_Enums
import Database_Settings
import Weights

def addTweetToSession(
    tweet,
    sentiment_type,
    stream_type,
    name, 
    screen_name, 
    follower_count, 
    location, 
    date_posted, 
    is_retweet,
    user_description,
    user_img_https_link,
    retweet_count,
    tweet_id,
    originator_id,
    session_id ):

    db = web.database(
        dbn     = Database_Settings.dbn, 
        db      = Database_Settings.db,
        user    = Database_Settings.user, 
        pw      = Database_Settings.pw )

    db.printing = Weights.print_db_accesses

    results     = db.query("SELECT * FROM session_id WHERE session_id = $sess_id", vars = { 'sess_id':session_id }) 
    results     = list(results)

    if len( results ) == 1:
        unpublished_tweet_data = results[0].data # If there is more than one session mapping ignore it.

        if sentiment_type == Global_Enums.Sentiment_Type.positive:
            sentiment_text = "Positive Sentiment"
        else:
            sentiment_text = "Negative Sentiment"

        if stream_type == Global_Enums.Stream_Type.live_stream:
            print '\nLIVE TWEET BEING ADDED TO DB\n'
            stream_text = "Live Stream"
        else:
            print '\nPAST TWEET BEING ADDED TO DB\n'
            stream_text = "Past Stream"

        json_formatted_data = [{
            'stream_type':              stream_text,
            'name':                     name,
            'sentiment':                sentiment_text,
            'location':                 location,
            'date':                     date_posted,
            'tweet':                    tweet,
            'screen_name':              screen_name,
            'followers':                follower_count,
            'is_retweet':               is_retweet,
            'user_description':         user_description,
            'user_img_https_link':      user_img_https_link,
            'retweet_count':            retweet_count,
            'tweet_id':                 tweet_id,
            'originator_id':            originator_id
        }]

        if unpublished_tweet_data == "":
            db_data = { "tweets": 
                [json_formatted_data]
            }
        else:
            try:
                db_data = json.loads(unpublished_tweet_data)
                db_data["tweets"].append(json_formatted_data)
            except:
                print '\nJSON Formatting Error - Unable to append to tweets. Add_Tweet_To_DB.py'

        # Avoid reverse twitter sql or xss injection. If create their own queries.
        encodeUTF8 = lambda data : data.encode( encoding="utf-8", errors="strict" ) if data != None else ""
        update     = db.query("UPDATE session_id SET data = $data WHERE session_id = $sess_id", vars = { 'sess_id':encodeUTF8(session_id), 'data':encodeUTF8(json.dumps( db_data )) }) # Set it to running and initialised.
        return 0
    
    else:
        return 0






