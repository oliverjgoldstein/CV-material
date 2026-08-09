# This class is only used by the live stream.
import json
import datetime
import time

# Tweepy API Libraries
from Tweepy_Auth import *
from tweepy import Stream, OAuthHandler
from tweepy.streaming import StreamListener

# User functions
    
import Decision_Function
import Global_Enums
import Add_Tweet_To_DB
import Weights
import Str_To_Bool
import Decode_Tweet

class bcolors:
    HEADER      = '\033[95m'
    OKBLUE      = '\033[94m'
    OKGREEN     = '\033[92m'
    WARNING     = '\033[93m'
    FAIL        = '\033[91m'
    ENDC        = '\033[0m'
    BOLD        = '\033[1m'
    UNDERLINE   = '\033[4m'

class listener(StreamListener):

    def __init__( self, keyword, sentiment_type, stream_type, upper_bound, lower_bound, disable_retweets, session_id ):        
        
        self.__keyword          = keyword
        self.__sentiment_type   = sentiment_type
        self.__stream_type      = stream_type
        self.__upper_bound      = upper_bound
        self.__lower_bound      = lower_bound
        self.__disable_retweets = disable_retweets
        self.__session_id       = session_id
        self.__unprinted_count  = 0
        self.__printed_count    = 0

        print bcolors.UNDERLINE + '\nLive Stream Established.' + bcolors.ENDC

    
    def on_data( self, data ):
        
        # For Debug Mode:
        time.sleep(Weights.debug_mode_sleep_time)

        tweet_data              = Decode_Tweet.jsonDecoderLiveStream( data )

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


        # Whether the correct stream is printed is handled in Process_Initialiser
        is_printable            = Decision_Function.isTweetValid( 
            tweet_text,
            self.__keyword,
            self.__sentiment_type,
            Global_Enums.Stream_Type.live_stream,
            self.__upper_bound,
            self.__lower_bound,
            is_retweet,
            self.__disable_retweets,
            users_follower_count )

        if( is_printable[0] == True ):

            Add_Tweet_To_DB.addTweetToSession( 
                tweet_text,
                is_printable[1],
                Global_Enums.Stream_Type.live_stream,
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
                self.__session_id )
            
            self.__printed_count += 1

        else:
            self.__unprinted_count += 1
            pass

        if ( self.__unprinted_count + self.__printed_count ) % Weights.number_of_rounds_print_status == 0:
                print bcolors.OKGREEN + "\n---------\n"             + bcolors.ENDC
                print bcolors.OKGREEN + '\nLive Total Count #: '    + str( self.__unprinted_count + self.__printed_count ) + bcolors.ENDC
                print bcolors.OKGREEN + "\nUnprinted: "             + str( self.__unprinted_count ) + " Printed: " + str( self.__printed_count )  + bcolors.ENDC
                print bcolors.OKGREEN + "\n---------\n"             + bcolors.ENDC

        return True

    def on_error(self, status_code):

        # https://dev.twitter.com/overview/api/response-codes
        # The amount of time a client has to wait after receiving error 420 will increase exponentially each time they make a failed attempt.

        if status_code == 420:
            print bcolors.FAIL + 'Data Limit Exceeded.\n' + bcolors.ENDC

        print status_code

        return False

