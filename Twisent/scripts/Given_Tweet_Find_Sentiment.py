
from __future__ import division # This ensures floating point division.
import math

import os
import string
import operator # For special tuple addition
import re
import string
from   string import maketrans

# User Function
import Sentiment_Automaton
import Weights

# tweet -> word -> [ positivity, negativity ]
def findOverallSentiment( tweet, keyword ):
    
    tweet_sentiment = ( 0.0 , 0.0 )

    # Before we do anything let's deal with URLS: especially news articles with info in the link.

    URL_REGEX   = re.compile(ur'(?i)\b((?:https?://|www\d{0,3}[.]|[a-z0-9.\-]+[.][a-z]{2,4}/)(?:[^\s()<>]+|\(([^\s()<>]+|(\([^\s()<>]+\)))*\))+(?:\(([^\s()<>]+|(\([^\s()<>]+\)))*\)|[^\s`!()\[\]{};:\'".,<>?\xab\xbb\u201c\u201d\u2018\u2019]))')
    url_list    = list( re.findall( URL_REGEX, tweet ) )
    tweet       = re.sub( URL_REGEX, "", tweet )

    encodeUTF8 = lambda data : data.encode( encoding="utf-8", errors="strict" ) if data != None else ""

    # We remove the urls and then find the sentiment for each of them individually.
    for url_looper in range(0, len(url_list)):
        punc_replacer           = maketrans( string.punctuation, ' '*len(string.punctuation) )
        url_list[url_looper]    = encodeUTF8(url_list[url_looper][0]).translate(punc_replacer)
        tweet_sentiment         = tuple( map( operator.add, tweet_sentiment, findSentenceSentiment( url_list[url_looper], False, keyword )))

    tweet = string.replace( tweet, '?', '.' ) # (Possible) Update: introduce an uncertainty measure for questions.

    # tweet = string.replace( tweet, '#', ' ' ) 
    # tweet = string.replace( tweet, '@', ' ' ) 

    for sentence in tweet.split("."):

        test_for_exclamation = sentence.split("!")
        if ( len(test_for_exclamation) >= 2 ):
            
            # There is at least one exclamation mark.
            # The very last split never has an exclamation mark.

            split_iterator = 0
            for sentence_exclamation in test_for_exclamation:
                split_iterator += 1

                # (-2) as we want to catch on the second last element with a zero indexed array.
                
                if ( split_iterator < len(test_for_exclamation) - 2 ):
                    tweet_sentiment = tuple( map( operator.add, tweet_sentiment, findSentenceSentiment( sentence_exclamation, True, keyword )))
                else:
                    tweet_sentiment = tuple( map( operator.add, tweet_sentiment, findSentenceSentiment( sentence_exclamation, False, keyword )))

        elif len(test_for_exclamation) == 1:
            tweet_sentiment = tuple(map(operator.add, tweet_sentiment, findSentenceSentiment( sentence, False, keyword )))
            
        else:
            print "Error in (Given Tweet Find Sentiment)!"
    
    tweet_sentiment    = list ( tweet_sentiment )
    tweet_sentiment[0] = round( tweet_sentiment[0], Weights.sentiment_decimal_places )
    tweet_sentiment[1] = round( tweet_sentiment[1], Weights.sentiment_decimal_places )

    # Here the total sentiment is rounded to the nearest integer.
    return tweet_sentiment


# sentence -> bool -> ( positive, negative )
def findSentenceSentiment( sentence, exclamation_present, keyword ):
    
    # Note: No longer removing punctuation due to emojis.
    # Remove keyword typed in case keyword has sentiment.
    sentence                    = re.sub(r"\b" + keyword + r"\b", "", sentence)
    sentence                    = sentence.lower() # convert to lower case to make comparisons.
    data                        = Sentiment_Automaton.findSentiment(sentence)
    sentence_sentiment          = ( data["positive"], data["negative"] )
    
    # If a ! exists then it has more emotional content.
    if exclamation_present == True:
        
        exclamation_function = lambda y : int( float( y ) * 
            Weights.exclamation_sentiment_multiplier ) # Round to nearest integer.
        
        sentence_sentiment = tuple( map( exclamation_function, sentence_sentiment ) )
    
    return sentence_sentiment




