from __future__ import division # This ensures floating point division.
import math

# A super gay state machine for sentiment analysis
from porter2 import stem
import Weights
import Read_In_From_File
    
file_path                       = 'word-lists/BetaEmotionLookupTable.txt'
emotions                        = Read_In_From_File.read_dict(file_path, None)

file_path                       = 'word-lists/BoosterWordList.txt'
booster_words                   = Read_In_From_File.read_dict(file_path, "\t")

file_path                       = 'word-lists/NegatingWordList.txt'
negating_words                  = Read_In_From_File.read(file_path)

file_path                       = 'word-lists/emoticons/Plaintext_Positive_Emoticons.txt'
positive_plaintext_emoticons    = Read_In_From_File.read(file_path)

file_path                       = 'word-lists/emoticons/Plaintext_Negative_Emoticons.txt'
negative_plaintext_emoticons    = Read_In_From_File.read(file_path)

file_path                       = 'word-lists/emoticons/Unicode_Positive_Emoticons.txt'
positive_unicode_emoticons      = Read_In_From_File.read(file_path)

file_path                       = 'word-lists/emoticons/Unicode_Negative_Emoticons.txt'
negative_unicode_emoticons      = Read_In_From_File.read(file_path)

# An Enum:
wordType = type("Word", (), dict( none_or_emoticon = 0, sentiment = 1, booster_word = 2, negation = 3 ))


# sentence -> state
def findSentiment(sentence):
    state = { "positive" : Weights.initial_positivity, "negative" : Weights.initial_negativity, "multiplier_boost" : Weights.initial_multiplier_boost, "adder_boost" : Weights.initial_adder_boost } 

    for word in sentence.split():
        state = updateState(state, word)
    return state


# state -> word -> state
def updateState(state, word):

    (kind, amount) = parseWord(word)

    if kind == wordType.none_or_emoticon:
        if amount > 0:
            state["positive"] += amount
        else:
            state["negative"] += abs(amount)

    elif kind == wordType.sentiment:
        if amount * state["multiplier_boost"] > 0:
            state["positive"] += amount * state["multiplier_boost"] + state["adder_boost"]
        else:
            state["negative"] += abs(amount * state["multiplier_boost"]) + state["adder_boost"]
        state["multiplier_boost"] = Weights.initial_multiplier_boost
        state["adder_boost"] = Weights.initial_adder_boost

    elif kind == wordType.negation:
        state["multiplier_boost"] *= amount

    elif kind == wordType.booster_word:
        #booster words consume multipliers
        state["adder_boost"] += amount * state["multiplier_boost"]
        state["multiplier_boost"] = Weights.initial_multiplier_boost

    return state

# word -> (kind, amount)
def parseWord(word):

    #Negating words behave like negative boosters - at the moment the weight for them is homogenous
    if word in negating_words:
        return (wordType.negation, Weights.negating_word_multiplier)
    elif word in booster_words:
        return ( wordType.booster_word, boosterCalculator(word) )

    elif stem(word) in emotions:
        return ( wordType.sentiment, emotions[stem(word)] )

    elif word in emotions:
        return ( wordType.sentiment, emotions[word] )

    elif word in positive_unicode_emoticons or word in positive_plaintext_emoticons: # Only deals with isolated emojis.
        return ( wordType.sentiment, Weights.positive_emoticon_addition )

    elif word in negative_unicode_emoticons or word in negative_plaintext_emoticons:
        return ( wordType.sentiment, Weights.negative_emoticon_addition )

    else:

        # Check for individual emojis:

        running_total = 0
        for character in word[:]:

            if character in negative_unicode_emoticons:
                running_total -= Weights.negative_emoticon_addition
            elif character in positive_unicode_emoticons:
                running_total += Weights.positive_emoticon_addition

            sigmoid = lambda x : (( Weights.double_sigmoid_max_value ) / ( 1 + math.exp( -1 * Weights.slowdown * x ) )) - ( Weights.double_sigmoid_max_value / 2 )

        return ( wordType.none_or_emoticon, sigmoid( running_total ) )



def boosterCalculator( word ):

    booster_word = float( booster_words[word] )

    if ( booster_words < 0 ):
        return booster_word * Weights.lzero_booster_m  + Weights.lzero_booster_c 

    else:
       return ( booster_word * Weights.gzero_booster_m ) + Weights.gzero_booster_c








