import sys
import math

import Given_Tweet_Find_Sentiment
import Weights

class bcolors:
    OKBLUE  = '\033[94m'
    OKGREEN = '\033[92m'
    FAIL    = '\033[91m'
    ENDC    = '\033[0m'

def main():
    print bcolors.ENDC + "Type EXIT to stop the unit test." + bcolors.ENDC
    print "ATAN2 Thresholds for live stream: "
    print Weights.live_bigger_more_positive
    print Weights.live_smaller_more_negative
    print "Magnitude: "
    print Weights.live_sentiment_magnitude_threshold_both
    print "ATAN2 Thresholds for past stream: "
    print Weights.past_bigger_more_positive                
    print Weights.past_smaller_more_negative         
    print "Magnitude: "      
    print Weights.past_sentiment_magnitude_threshold_both 
    
    while 1:

        data = raw_input("Enter a sentence (Live tweet thresholds): ")
        
        if data == "EXIT":
            break

        sentiment_pair = Given_Tweet_Find_Sentiment.findOverallSentiment(data, "")

        print "Sentiment is: "
        print "Positive: " + str(sentiment_pair[0])
        print "Negative: " + str(sentiment_pair[1])

        bigger_more_positive               = Weights.live_bigger_more_positive
        smaller_more_negative              = Weights.live_smaller_more_negative
        sentiment_magnitude_threshold_both = Weights.live_sentiment_magnitude_threshold_both

        tweet_magnitude = math.sqrt( sentiment_pair[0] * sentiment_pair[0] + sentiment_pair[1] * sentiment_pair[1] )
        tweet_theta     = math.degrees( math.atan2( sentiment_pair[0] , sentiment_pair[1] ) )
    
        print "ATAN2: "     + str(tweet_theta)
        print "MAGNITUDE: " + str(tweet_magnitude)

        if  ( tweet_theta >= bigger_more_positive ) and ( tweet_magnitude >= sentiment_magnitude_threshold_both ) and ( sentiment_pair[0] >= sentiment_pair[1] ):
            print bcolors.OKGREEN + 'Passes BOTH Positive threshold' + bcolors.ENDC
        elif( tweet_theta <= smaller_more_negative ) and ( tweet_magnitude >= sentiment_magnitude_threshold_both ) and ( sentiment_pair[1] >= sentiment_pair[0] ):
            print bcolors.OKBLUE + 'Passes BOTH Negative threshold' + bcolors.ENDC
        else:
            print bcolors.FAIL + 'Fails BOTH threshold' + bcolors.ENDC
    
        if  ( tweet_theta >= bigger_more_positive ) and ( sentiment_pair[0] >= sentiment_pair[1] ):
            print bcolors.OKGREEN + 'Passes Positive threshold' + bcolors.ENDC
        else:
            print bcolors.FAIL + 'Fails Positive threshold' + bcolors.ENDC
    
        if  ( tweet_theta <= smaller_more_negative ) and ( sentiment_pair[1] >= sentiment_pair[0] ):
            print bcolors.OKBLUE + 'Passes Negative threshold' + bcolors.ENDC
        else:
            print bcolors.FAIL + 'Fails Negative threshold' + bcolors.ENDC
    
    sys.exit("Unit Test Complete. STARTING SERVER!\n\n")

if __name__ == '__main__':
    """ Everything Starts Here. """
    main()
    pass
