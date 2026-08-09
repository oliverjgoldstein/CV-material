import json
import re
import datetime

# User functions
import Weights
import Tweepy_Auth

# Properties of tweet_data:
# ['__class__', '__delattr__', '__dict__', '__doc__', '__eq__', '__format__', '__getattribute__', '__getstate__', '__hash__', '__init__', '__module__', 
# '__ne__', '__new__', '__reduce__', '__reduce_ex__', '__repr__', '__setattr__', '__sizeof__', '__str__', '__subclasshook__', '__weakref__', '_api',
# '_json', 'author', 'contributors', 'coordinates', 'created_at', 'destroy', 'entities', 'favorite', 'favorite_count', 'favorited', 'geo', 'id', 
# 'id_str', 'in_reply_to_screen_name', 'in_reply_to_status_id', 'in_reply_to_status_id_str', 'in_reply_to_user_id', 'in_reply_to_user_id_str', 
# 'is_quote_status', 'lang', 'metadata', 'parse', 'parse_list', 'place', 'retweet', 'retweet_count', 'retweeted', 'retweeted_status', 'retweets', 
# 'source', 'source_url', 'text', 'truncated', 'user']

# In case we want to include these:
# Tweet Text Additions: Quoted Text / Retweeted Text Quoted Text

# retweeted_quoted_text = tweet_data._json['retweeted_status']['quoted_status']['text']
# quoted_status         = tweet_data._json['quoted_status']['text']

encodeUTF8 = lambda data : data.encode( encoding="utf-8", errors="strict" ) if data != None else ""

def jsonDecoderPastStream( tweet_data ):

    # If this is a retweet find the original retweeter id/screen name in order to retrieve retweets later on.
    if tweet_data.retweet_count > 1:
        is_retweet = True

        # Both of these data items only seem to be here irregularly. I am lazy to figure out the pattern in the JSON data.
        try:
            if tweet_data._json['retweeted_status']['id'] != None and tweet_data._json['retweeted_status']['user']['screen_name'] != None:
                originator_screen_name = tweet_data._json['retweeted_status']['user']['screen_name']
                originator_id = tweet_data._json['retweeted_status']['id']
            else:
                originator_id          = -1
                originator_screen_name = ""
        except:
            print 'Unable to retrieve retweeter info'
            originator_id           = -1
            originator_screen_name  = ""

    else:
        originator_id          = -1
        originator_screen_name = ""
        is_retweet             = False

    try:
        if tweet_data._json['user']['location'] != None:
            user_location = encodeUTF8(tweet_data._json['user']['location'])
        else:
            user_location = "Unknown"
    except:
        user_location = "Unknown"

    # Find the full urls to replace the 23 character long t.co urls with expanded urls.
    url_list = return_url_list( tweet_data._json )
    tweet_text = processUrls( url_list, tweet_data.text )

    # Be aware of the existence of: something like tweet_data._json['quoted_status']['text']
    # Displayed variables:
    list_of_data_elements = []
    list_of_data_elements.append(   tweet_text                                                               )
    list_of_data_elements.append(   is_retweet                                                               )
    list_of_data_elements.append(   long( tweet_data._json['user']['followers_count'] )                      )
    list_of_data_elements.append(   encodeUTF8(tweet_data._json['user']['name'])                             )
    list_of_data_elements.append(   encodeUTF8(tweet_data._json['user']['screen_name'])                      )
    list_of_data_elements.append(   user_location                                                            )
    list_of_data_elements.append(   str(tweet_data.created_at.strftime("%A %d %B %Y %H %M"))                 ) # to become JSON serialisable
    list_of_data_elements.append(   encodeUTF8(tweet_data.user._json['description'])                         )
    list_of_data_elements.append(   tweet_data._json['user']['profile_image_url_https']                      )
    list_of_data_elements.append(   str(tweet_data.retweet_count)                                            )
    list_of_data_elements.append(   str(tweet_data.id)                                                       )
    list_of_data_elements.append(   url_list                                                                 )
    list_of_data_elements.append(   str(originator_id)                                                       )
    list_of_data_elements.append(   originator_screen_name                                                   )
    list_of_data_elements.append(   tweet_data._json['user']['time_zone']                                    )

    # Just in case need profile URL: Although this won't get it, this is an idea.
    # api.get_user(tweet_data._json['user']['screen_name'])

    return list_of_data_elements




def jsonDecoderLiveStream( tweet_data ):

    tweet_json              = json.loads( tweet_data )
    
    try:
        if tweet_data['user']['location'] != None:
            user_location = encodeUTF8(tweet_data['user']['location'])
        else:
            user_location = "Unknown"
    except:
        user_location = "Unknown"

    try: # These try catch blocks are created out of ambiguity. I can't be bothered in other words.
        if tweet_json['retweeted_status']['id'] != None and tweet_json['retweeted_status']['user']['screen_name'] != None:
            originator_id          = tweet_json['retweeted_status']['id']
            originator_screen_name = tweet_json['retweeted_status']['user']['screen_name']
            is_retweet             = True
        else:
            originator_id           = -1
            originator_screen_name  = ""
            is_retweet              = False
    except:
        originator_id           = -1
        originator_screen_name  = ""
        is_retweet              = False

    url_list            = return_url_list( tweet_json )
    tweet_text          = processUrls( url_list, tweet_json['text'] )
    live_retweet_amount = 0

    list_of_data_elements = []
    list_of_data_elements.append(   tweet_text                                                               )
    list_of_data_elements.append(   is_retweet                                                               )
    list_of_data_elements.append(   long( tweet_json['user']['followers_count'] )                            )
    list_of_data_elements.append(   encodeUTF8(tweet_json['user']['name'])                                   )
    list_of_data_elements.append(   encodeUTF8(tweet_json['user']['screen_name'])                            )
    list_of_data_elements.append(   user_location                                                            )
    list_of_data_elements.append(   tweet_json['created_at']                                                 )
    list_of_data_elements.append(   encodeUTF8( tweet_json['user']['description'] )                          )
    list_of_data_elements.append(   tweet_json['user']['profile_image_url_https']                            )
    list_of_data_elements.append(   str(live_retweet_amount)                                                 )
    list_of_data_elements.append(   tweet_json['id_str']                                                     )
    list_of_data_elements.append(   url_list                                                                 )
    list_of_data_elements.append(   str(originator_id)                                                       )
    list_of_data_elements.append(   originator_screen_name                                                   )
    list_of_data_elements.append(   tweet_json['user']['time_zone']                                          )

    return list_of_data_elements




def return_url_list( json_data ):
    url_list = []
    for looper in range( 0, Weights.max_urls_in_post ):
        try:
            url_list.append( encodeUTF8( json_data['entities']['urls'][looper]['expanded_url'] ))
        except:
            # There are no more urls in the post.
            break
    return url_list


def processUrls( url_list, tweet_text ):

    # Process tweet text - remove twitter generated links.
    tweet_text = tweet_text.replace("'", "")
    tweet_text = tweet_text.replace('"', "")
    URL_REGEX  = re.compile(ur'(?i)\b((?:https?://|www\d{0,3}[.]|[a-z0-9.\-]+[.][a-z]{2,4}/)(?:[^\s()<>]+|\(([^\s()<>]+|(\([^\s()<>]+\)))*\))+(?:\(([^\s()<>]+|(\([^\s()<>]+\)))*\)|[^\s`!()\[\]{};:\'".,<>?\xab\xbb\u201c\u201d\u2018\u2019]))')
    url_looper = 0
    for url in url_list:
        tweet_text  = re.sub(URL_REGEX, url_list[url_looper], tweet_text, 1)
        url_looper += 1

    return tweet_text









