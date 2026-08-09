
import os

# SETTINGS:

# General

print_db_accesses                           = False

# Thread Initialiser 
search_time                                 = 17 # In seconds. MAKE SURE THIS IS AT LEAST TOTAL JS SEARCH TIME - JS UPDATE TO STOP INCONSISTENCIES.

# Twitter Data Streams/Tweepy Listener Class: WARNING Set this to zero when code is in use.
debug_mode_sleep_time                       = 1 # In seconds: we do not want to reach the data limit whilst debugging code.
language                                    = "en" # This is sent to twitter.
number_of_rounds_print_status               = 4

# Server Start:
cookie_timeout                              = 3600 # One hour
secure_cookies                              = os.environ.get("TWISENT_SECURE_COOKIES", "1") != "0"
minimum_query_length                        = 3 # In characters
max_follower_count                          = 100000
min_follower_count                          = 0
max_query_length                            = 30
web_py_debug_mode                           = False
cleanup_time                                = 5 # This is the time in seconds the server waits to make sure any unseen tweets get returned.

# Decode Tweet:
max_urls_in_post                            = 6 # 140/23 or url length is just over 6.

###################################################################################

# WEIGHTS:

# Given Tweet Find Sentiment:
# Most of these are self explanatory  
exclamation_sentiment_multiplier            = 1.6
sentiment_decimal_places                    = 0

# Sentiment Automaton: tweet sentiment calculated as float type.
initial_positivity                          =  0.0   # Trivial: The initial positivity of each tweet.
initial_negativity                          =  0.0   # Here for simplicity. Ideal to keep at zero.
initial_multiplier_boost                    =  1.0   # If you want - weight this higher assuming important part of tweet is at first point of sentiment.
initial_adder_boost                         =  0   # If you want - weight this higher assuming important part of tweet is at first point of sentiment.
lzero_booster_m                             =  1   # Check comments in automaton for explanation
lzero_booster_c                             =  0    # booster e.g. "slightly"
gzero_booster_m                             =  1   # booster e.g. "extremely"
gzero_booster_c                             =  0  
positive_emoticon_addition                  =  1.8   # For each emoticon how much sentiment do we add.
negative_emoticon_addition                  = -1.8   # This is determined by a sigmoid function
double_sigmoid_max_value                    =  10.0  # From -5.0 to +5.0 in this case.
slowdown                                    =  0.5
negating_word_multiplier                    =  -1    # Just flip the sign

# Decision Function: LIVE Stream:
# In degrees: ranging from 0 to 90. ATAN2 in Decision Function. (Note: <=/>= to values below.)
live_bigger_more_positive                   = 55.0
live_smaller_more_negative                  = 35.0
live_sentiment_magnitude_threshold_both     = 3.0

# PAST Stream: threshold slightly higher as normally more tweets to search through.
past_bigger_more_positive                   = 60.0
past_smaller_more_negative                  = 30.0
past_sentiment_magnitude_threshold_both     = 4.0
