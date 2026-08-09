
# Tweepy API Libraries
import tweepy
from tweepy import Stream, OAuthHandler
import os

def required_env(name):
    value = os.environ.get(name)
    if not value:
        raise RuntimeError("Missing required environment variable: " + name)
    return value


cKey = required_env("TWITTER_CONSUMER_KEY")
cSecret = required_env("TWITTER_CONSUMER_SECRET")
aToken = required_env("TWITTER_ACCESS_TOKEN")
aSecret = required_env("TWITTER_ACCESS_TOKEN_SECRET")

auth = OAuthHandler(cKey, cSecret)
auth.set_access_token(aToken, aSecret)
# wait_on_rate_limit = True means we should never go over our limit. It will wait for us when we exceed the rate limit.
api = tweepy.API( auth, wait_on_rate_limit=True )
