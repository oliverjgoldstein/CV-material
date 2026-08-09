import sys
import math

import Weights
import Given_Tweet_Find_Sentiment
import Global_Enums

# @return : (bool, sentiment_type) used to decide whether to print it.
def isTweetValid( tweet, keyword, sentiment_type, stream_type, upper_bound, lower_bound, is_retweet, disable_retweets, follower_count ):

    if ( is_retweet == True and disable_retweets == True ):
        return ( False, None )

    if ( upper_bound < Weights.max_follower_count ): # When it is equal precisely to the max follower count that is equivalent to no maximum

        if ( follower_count >= upper_bound ):
            return ( False, None )
    
    if ( lower_bound > Weights.min_follower_count ): # When it is equal precisely to the min follower count that is equivalent to no minimum

        if ( follower_count <= lower_bound ):
            return ( False, None )

    if ( stream_type == Global_Enums.Stream_Type.live_stream ):
        bigger_more_positive               = Weights.live_bigger_more_positive
        smaller_more_negative              = Weights.live_smaller_more_negative
        sentiment_magnitude_threshold_both = Weights.live_sentiment_magnitude_threshold_both

    elif ( stream_type == Global_Enums.Stream_Type.past_stream ):
        bigger_more_positive               = Weights.past_bigger_more_positive
        smaller_more_negative              = Weights.past_smaller_more_negative
        sentiment_magnitude_threshold_both = Weights.past_sentiment_magnitude_threshold_both
    else:
        print "\nError (Decision Function)!"
        print "\nStream type: " + str(stream_type)
        return ( False, None )


    # tuple index 0 : positivity, tuple index 1 : negativity
    sentiment_pair  = Given_Tweet_Find_Sentiment.findOverallSentiment( tweet, keyword )
    tweet_magnitude = math.sqrt( sentiment_pair[0] * sentiment_pair[0] + sentiment_pair[1] * sentiment_pair[1] )
    tweet_theta     = math.degrees( math.atan2( sentiment_pair[0] , sentiment_pair[1] ) )

    if (( sentiment_pair[0] == 0 ) and ( sentiment_pair[1] == 0 )):
        return ( False, None )

    # Consider both the magnitude and angle if they want both.
    if sentiment_type == Global_Enums.Sentiment_Type.both:
 
        if  ( tweet_theta >= bigger_more_positive ) and ( tweet_magnitude >= sentiment_magnitude_threshold_both ) and ( sentiment_pair[0] >= sentiment_pair[1] ):
            return ( True, Global_Enums.Sentiment_Type.positive )
        elif( tweet_theta <= smaller_more_negative ) and ( tweet_magnitude >= sentiment_magnitude_threshold_both ) and ( sentiment_pair[1] >= sentiment_pair[0] ):
            return ( True, Global_Enums.Sentiment_Type.negative )
        else:
            return ( False, None )

    elif sentiment_type == Global_Enums.Sentiment_Type.positive:
        if  ( tweet_theta >= bigger_more_positive ) and ( sentiment_pair[0] >= sentiment_pair[1] ):
            return ( True, Global_Enums.Sentiment_Type.positive )
        else:
            return ( False, None )

    elif sentiment_type == Global_Enums.Sentiment_Type.negative:
        if  ( tweet_theta <= smaller_more_negative ) and ( sentiment_pair[1] >= sentiment_pair[0] ):
            return ( True, Global_Enums.Sentiment_Type.negative )
        else:
            return ( False, None )
