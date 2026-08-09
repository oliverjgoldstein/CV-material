from enum import Enum

class Stream_Type(Enum):

    def __str__(self):
        return str(self.value)

    live_stream  = 0
    past_stream  = 1
    both_streams = 2

class Sentiment_Type(Enum):

    def __str__(self):
        return str(self.value)

    positive    = 0
    negative    = 1
    both        = 2