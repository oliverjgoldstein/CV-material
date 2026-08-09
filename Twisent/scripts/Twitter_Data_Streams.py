
# Python Standard Libraries Used
import os
import time
import tweepy
import string
import json
import datetime
from   Tweepy_Auth import *  # This is code with a few twitter API authentication variables.
from   tweepy import Stream, OAuthHandler
from   tweepy.streaming import StreamListener


# User functions
import Given_Tweet_Find_Sentiment   # This is used by the past stream to find the relevant sentiment data for each tweet.
import Tweepy_Listener_Class        # This is a listener class for the live stream of twitter data.         
import Add_Tweet_To_DB
import Global_Enums
import Weights
import Decision_Function
import Decode_Tweet

# Set the timezone to London.
os.environ['TZ'] = 'Europe/London'
time.tzset()

class bcolors:
    HEADER      = '\033[95m'
    OKBLUE      = '\033[94m'
    OKGREEN     = '\033[92m'
    WARNING     = '\033[93m'
    FAIL        = '\033[91m'
    ENDC        = '\033[0m'
    BOLD        = '\033[1m'
    UNDERLINE   = '\033[4m'

# This function retrieves tweets from the last week.
# Due to Tweepy API limitations - we can only get tweets from the last week.

def pastStream(
        keyword, 
        sentiment_type,
        stream_type,
        upper_bound,
        lower_bound,
        disable_retweets,
        session_id,
        days_in_past ):
    
    # Create the date string from one week ago.

    one_week_ago_string     = str           ( datetime.date.today() + datetime.timedelta( days = -days_in_past ))
    date_string             = str           ( datetime.date.today().strftime( '%Y-%m-%d' ))
    tweet_iter              = tweepy.Cursor ( api.search, q = keyword, since = one_week_ago_string , until = date_string, lang = Weights.language ).items()
    
    print bcolors.UNDERLINE + '\nPast Stream Established.' + bcolors.ENDC

    printed_tweet_count     = 0
    unprinted_tweet_count   = 0

    # Loop forever, printing tweets, if the tweet sentiment is above threshold.

    while True:
        try:
            # For Debug Mode:
            time.sleep(Weights.debug_mode_sleep_time)

            tweet                   = tweet_iter.next()

            tweet_data              = Decode_Tweet.jsonDecoderPastStream( tweet )

            tweet_text              = tweet_data[0]
            is_retweet              = tweet_data[1]
            users_follower_count    = tweet_data[2]
            users_full_name         = tweet_data[3]
            users_screen_name       = tweet_data[4]
            tweet_location          = tweet_data[5]
            tweet_post_date         = tweet_data[6]
            user_description        = tweet_data[7]
            user_img_https_link     = tweet_data[8]
            retweet_count           = tweet_data[9]
            tweet_id                = tweet_data[10]
            tweet_urls              = tweet_data[11] # This is here in case we want to follow the links in future.
            originator_id           = tweet_data[12]
            originator_screen_name  = tweet_data[13] # This is unused currently.
            time_zone               = tweet_data[14] # This is unused currently.

            is_printable            = Decision_Function.isTweetValid(   
                tweet_text,
                keyword,
                sentiment_type,
                Global_Enums.Stream_Type.past_stream,
                upper_bound,
                lower_bound,
                is_retweet,
                disable_retweets,
                users_follower_count )

            if ( is_printable[0] == True ):
    
                Add_Tweet_To_DB.addTweetToSession( 
                    tweet_text,
                    is_printable[1],
                    stream_type,
                    users_full_name, 
                    users_screen_name,
                    users_follower_count, 
                    tweet_location, 
                    tweet_post_date,
                    is_retweet,
                    user_description,
                    user_img_https_link,
                    retweet_count,
                    tweet_id,
                    originator_id,
                    session_id )

                printed_tweet_count   += 1

            else:
                unprinted_tweet_count += 1
                pass

            if ( unprinted_tweet_count + printed_tweet_count ) % Weights.number_of_rounds_print_status == 0:
                print bcolors.HEADER + "\n.........\n" + bcolors.ENDC
                print bcolors.HEADER + "\nPast Total Count #: " + str( unprinted_tweet_count + printed_tweet_count ) + bcolors.ENDC
                print bcolors.HEADER + "\nUnprinted: "        + str( unprinted_tweet_count ) + " Printed: " + str( printed_tweet_count ) + bcolors.ENDC
                print bcolors.HEADER + "\n.........\n" + bcolors.ENDC
            
        except tweepy.TweepError:
            print api.rate_limit_status()
            print bcolors.FAIL + '\nAPI Rate Limit Exceeded!' + bcolors.ENDC
            continue

        except StopIteration:
            print api.rate_limit_status()
            print bcolors.FAIL + '\nFatal Error: Tweepy has stopped!' + bcolors.ENDC
            continue


# This retrieves live tweets.

def liveTweetStream(
        keyword, 
        sentiment_type,
        stream_type,
        upper_bound,
        lower_bound,
        disable_retweets,
        session_id ):
    
    twitterStream = Stream( 
        auth, 
        Tweepy_Listener_Class.listener(
            keyword, 
            sentiment_type,
            stream_type,
            upper_bound,
            lower_bound,
            disable_retweets,
            session_id )
        )

    twitterStream.filter(track = [keyword])








