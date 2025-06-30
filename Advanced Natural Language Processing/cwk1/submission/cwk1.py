""" Authorship: Created at Edinburgh University:
 Kleber Noel (klebnoel@gmail.com) and Oliver Goldstein (oliverjgoldstein@gmail.com)
 This code was written for ANLP coursework to evaluate n-gram patterns in data.
"""
import re
import sys
import random
from math import log
from collections import defaultdict
from collections import Counter
from collections import OrderedDict
from string import ascii_lowercase
import numpy as np
import math


""" this function currently removes all characters that are not
 1. English alphabet. 2. Spaces 3. Digits 4. '.'
 This function also lowercases all characters and all digit
 are converted to zero."""
def preprocess_line(line):
    """treat each line as a separate sequence for 
    modeling purposes (rather than treating the entire 
    input file as a single sequence"""
    line = re.sub('([^a-zA-Z0-9. ]|\n)', '', line)
    line = line.lower()
    line = re.sub('[0-9]','0',line)
    line = str('##'+line+'#')
    return line



    # Initialise trigram_training_file dictionary to include all 3 letter sequences.
def initialise_dictionary_with_regexp(bi_or_tri):
    """This function initialises dictionaries to contain correct sequences of characters"""
    counts = defaultdict(float)
    alpha  = '[a-z0. ]'
    correct_reg_exp_tri = re.compile("(#(%s{2})|##%s|(%s{2})#|%s{3})" % (alpha, alpha, alpha, alpha))
    #All strings are correct for bigram
    for a in ascii_lowercase+'.# 0':
        for b in ascii_lowercase+'.# 0':
            if bi_or_tri == 1:
                for c in ascii_lowercase+'.# 0': 
                    #Check whether the trigram sequence is valid.
                    if re.match(correct_reg_exp_tri, (a+b+c)):
                        counts[a + b + c] = 0.0
            else:
                counts[a+b] = 0.0
    return counts





# Returns 29 or 30 in each bigram bin. #_# _#_
def get_bigram_bin_size(trigram_training_file):
    bigram_bin_size = initialise_dictionary_with_regexp(0)
    
    #initalise the bigram_bin_size using the possible key values:
    
    for k, v in trigram_training_file.items():
        bigram_bin_size[k[:2]]+=1
        
    return bigram_bin_size






def perplexity_given_tri_counts(trigram_probabilities, trigram_test_file):
    log_probabilities = 0.0
    N = 0
    for k, v in trigram_test_file.items():
        for p in range(0, int(v)):
            N += 1
            log_probabilities += log(trigram_probabilities[k], 2)
        
    total_perplexity = math.pow(2, (float(-1)/float(N))*log_probabilities)
    return total_perplexity








def add_alpha_smoothing(bigram_bin_size, bigram, trigram_training_file, trigram_probabilities, alpha):
    """This function implements add alpha smoothing"""
    for k, v in trigram_training_file.items():
        numerator                   = trigram_training_file[k] + alpha
        denominator                 = bigram[k[:2]] + bigram_bin_size[k[:2]] * alpha
        trigram_probabilities[k]    = numerator / denominator
    
    return trigram_probabilities









def read_in_preprocess_file_sum_trigrams(infile, dictionary):
    
    with open(infile) as f:
        for line in f:
            line = preprocess_line(line)
            if len(line) <= 4:
                continue
            for j in range(0, len(line)-(2)):
                seq3 = line[j:j+3]
                dictionary[seq3] += 1

    return dictionary
    



def test_bigram_bin_size(bigram, bigram_bin_size):
    for k, v in bigram.items():
        if "#" not in k:
            print("Bigram: " + k + " has bin size " + str(bigram_bin_size[k]))




def test_training_probabilities(trigram_probabilities):
    print(trigram_probabilities.items())




def test_sum_to_one(trigram_probabilities):
    """This function checks whether the bins of trigram_probabilities sum to one for each set
    where a prior bigram defines the set"""
    sum_to_one = initialise_dictionary_with_regexp(0)
    
    for k,v in trigram_probabilities.items():
        sum_to_one[k[:2]] += trigram_probabilities[k]

    for k, v in sum_to_one.items():
        print(sum_to_one[k])





def write_model_to_file(trigram_probabilities):
    f = open("model_probabilities","w+")
    
    for k, v in trigram_probabilities.items():
    	f.write(str(k) + '\t'+ str(v) + '\n')
        
    f.close()






def test_alphas(bigram_bin_size, bigram, trigram_training_file, trigram_probabilities):
    """This function iterates through 1000 alpha values between 0 and 1, step==0.001."""
    alpha_vals = np.arange(0.001,1,0.001)

    for x in range(0, len(alpha_vals)):
        trigram_probabilities = add_alpha_smoothing(bigram_bin_size, bigram, trigram_training_file, trigram_probabilities, alpha_vals[x])
        perplexity            = perplexity_given_tri_counts(trigram_probabilities, trigram_test_file)
        print(str(alpha_vals[x]) + ", " + str(perplexity))






def generate_random_char_seq(trigram_probabilities, sequence, recursive_step, seq_length=300):
    """This function returns a sequence of characters depending on the trigram_probabilities
    read in when the number of characters that remain to be generated==recurisve_step==0
    """
    if recursive_step <= 0:
        return sequence
    else:
        prev_two = {k:v for (k,v) in trigram_probabilities.items() 
                    if k[:2]==(sequence[-2]+sequence[-1])}
        prev_two_vals = np.array(prev_two.values())
        prev_two_vals /= prev_two_vals.sum()
        next_char = np.random.choice(list(prev_two.keys()), replace=True, p = list(prev_two_vals))[2]
        sequence += next_char
        
        if(next_char == '#'):
            sequence += '#'
            recursive_step -=1
        
        sequence = generate_random_char_seq(trigram_probabilities, sequence, recursive_step - 1, seq_length)
        return sequence




def read_in_trigram_probabilities(file):
    """This function reads in model-br.en and returns a dictionary of the values therein"""
    new_dictionary = initialise_dictionary_with_regexp(1)
    
    with open(file) as f:
        for line in f:
            line_vars = line.split("\t")
            
            key = line_vars[0]
            value = line_vars[1]
            value = float(re.sub('\n','',value))

            new_dictionary[key] = value
    
    return new_dictionary





def main():
    trigram_training_file   = initialise_dictionary_with_regexp(1) #counts of all trigrams in input
    trigram_test_file       = initialise_dictionary_with_regexp(1) #counts of all trigrams in input
    bigram                  = initialise_dictionary_with_regexp(0) #counts of all bigrams in input
    trigram_probabilities   = initialise_dictionary_with_regexp(1) #probability dictionary mapping trigram keys to smoothed probabilities

    if len(sys.argv) != 3:
        print("Put in 2 file names: ") 
        sys.exit(1)
        
    training_file = sys.argv[1]
    test_file     = sys.argv[2]
    
    print(training_file)
    print(test_file)
    
    trigram_training_file       = read_in_preprocess_file_sum_trigrams(training_file, trigram_training_file)
    trigram_test_file           = read_in_preprocess_file_sum_trigrams(test_file, trigram_test_file)
    
    trigram_training_file       = OrderedDict(sorted(trigram_training_file.items(), key=lambda t: t[0]))
    trigram_probabilities       = OrderedDict(sorted(trigram_probabilities.items(), key=lambda t: t[0]))
    bigram                      = OrderedDict(sorted(bigram.items(), key=lambda t: t[0]))
   
    # Updates the bigram count for all three letter trigrams
    for k, v in trigram_training_file.items():
        bigram[k[:2]] += v
    
    # Creates a dictionary of all possible bigram cases
    bigram_bin_size = get_bigram_bin_size(trigram_training_file)
    
    alpha = 0.7 # Initial guess
    print("Alpha value is "+str(alpha))
    # Generate probabilities from the trigrams, bigrams
    trigram_probabilities = add_alpha_smoothing(bigram_bin_size, bigram, trigram_training_file, trigram_probabilities, alpha)
    perplexity            = perplexity_given_tri_counts(trigram_probabilities, trigram_test_file)
    random_seq            = generate_random_char_seq(trigram_probabilities, '##', 298, 298)
    print("Random sequence generated by trigram generated from training data: ")
    print(random_seq)
    print("Final perplexity of " + str(test_file) + " with the training trigram probabilities is: " + str(perplexity))
    
    # Write the model to the file.
    write_model_to_file(trigram_probabilities)	
    print("Model written to file!")
    
    new_trigram_probabilities = read_in_trigram_probabilities("data/model-br.en")
    random_seq_new            = generate_random_char_seq(new_trigram_probabilities, '##', 298, 298)
    print("Random sequence generated by trigram generated from data/model-br.en: ", random_seq_new)
    
    return 1

if __name__ == "__main__":
    main()