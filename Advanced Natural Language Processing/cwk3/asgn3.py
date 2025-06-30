from __future__ import division
from math import log,sqrt,floor
import operator
from nltk.stem import *
from nltk.stem.porter import *
import matplotlib.pyplot as plt
from load_map import *
import collections
import numpy as np
from matplotlib.ticker import FuncFormatter


STEMMER = PorterStemmer()

# helper function to get the count of a word (string)
def w_count(word):
  return o_counts[word2wid[word]]

def tw_stemmer(word):
  '''Stems the word using Porter stemmer, unless it is a 
  username (starts with @).  If so, returns the word unchanged.

  :type word: str
  :param word: the word to be stemmed
  :rtype: str
  :return: the stemmed word

  '''
  if word[0] == '@': #don't stem these
    return word
  else:
    return STEMMER.stem(word)

def PMI(c_xy, c_x, c_y, N):
  '''Compute the pointwise mutual information using cooccurrence counts.

  :type c_xy: int 
  :type c_x: int 
  :type c_y: int 
  :type N: int
  :param c_xy: coocurrence count of x and y
  :param c_x: occurrence count of x
  :param c_y: occurrence count of y
  :param N: total observation count
  :rtype: float
  :return: the pmi value

  '''
  

  pmi = log(float(c_xy * N) / float(c_x * c_y), 2)
  return pmi


#Do a simple error check using value computed by hand
if(PMI(2,4,3,12) != 1): # these numbers are from our y,z example
    print("Warning: PMI is incorrectly defined")
else:
    print("PMI check passed")
    


def cos_sim(v0,v1):
  '''Compute the cosine similarity between two sparse vectors.

  :type v0: dict
  :type v1: dict
  :param v0: first sparse vector
  :param v1: second sparse vector
  :rtype: float
  :return: cosine between v0 and v1
  '''
  numerator = 0
  for m in v0.keys():
      if m in v1.keys():
          numerator += v0[m] * v1[m] 
  denominator_v0 = 0
  for n in v0.values():
      denominator_v0 += (n**2)
  denominator_v0 = denominator_v0 ** 0.5
  denominator_v1 = 0
  for n in v1.values():
      denominator_v1 += (n**2)
  denominator_v1 = denominator_v1 ** 0.5
  if denominator_v0 * denominator_v1 == 0:
      return 5
  cos_sim = numerator / (denominator_v0 * denominator_v1)
  print('cos:'+ str(numerator) + ' ' + str((denominator_v0 * denominator_v1)) + str(cos_sim))
  return cos_sim
      
      
  
      
  # We recommend that you store the sparse vectors as dictionaries
  # with keys giving the indices of the non-zero entries, and values
  # giving the values at those dimensions.

  #You will need to replace with the real function
  return cos_sim

def Jen2_sim(v0,v1):
    p0 = convert_PPMI_vec_to_probability_vec(v0)
    p1 = convert_PPMI_vec_to_probability_vec(v1)

    kuh1 = 0
    kuh2 = 0
    for wid1 in p0.keys():
        if wid1 in p1.keys():
            kuh1 += p0[wid1] * log(((2*p0[wid1]) /(p1[wid1] + p0[wid1])),2) + p1[wid1] * log(((2*p1[wid1]) /(p0[wid1] + p1[wid1])),2)
        else:
            kuh2 += p0[wid1]
        
    for wid0 in p1.keys():
        if wid0 not in p0.keys():
            kuh2 += p1[wid0]
    print('jen:' + str(kuh1/2) + ' ' + str(kuh2/2) + ' ' + str(kuh1 / kuh2))
    kuh = kuh1 + kuh2
    return 1 - kuh / 2
    

def Jac_sim(v0,v1):
    intersection = 0
    for wid0 in v0.keys():
        if wid0 in v1.keys():
            intersection += min(v0[wid0], v1[wid0])
    
    max_values_both = 0
    total_max_values = 0
    max_only_v0 = 0
    max_only_v1 = 0
    for wid0 in v0.keys():
        if wid0 in v1.keys():            
            max_values_both += max(v0[wid0], v1[wid0])
        else:
            max_only_v0 += v0[wid0]
    for wid1 in v1.keys():
        if wid1 not in v0.keys():
            max_only_v1 += v1[wid1]
        
    total_max_values = max_values_both + max_only_v0 + max_only_v1
    
    if total_max_values == 0:
        return 1
    print('Jac:' + str(intersection) + ' ' + str(total_max_values) + ' ' + str(intersection / total_max_values))
    return intersection / total_max_values

# v1 is always (v0 + v1) / 2
def Kuh_sim(v0,v1):
    kuh = 0
    for wid1 in v1.keys():
        if wid1 in v0.keys():
            kuh += v0[wid1] * log((v0[wid1] / v1[wid1]),2)
    return kuh
    
def convert_PPMI_vec_to_probability_vec(v0):
    total_val = sum(v0.values())
    new_probability_vector = {}
    for wid0 in v0.keys():
        new_probability_vector[wid0] = v0[wid0] / total_val
    
    return new_probability_vector

def Jensen_sim(v0,v1):
    ''' 
    @Param: Takes in two probability distributions.
    '''
    
    p0 = convert_PPMI_vec_to_probability_vec(v0)
    p1 = convert_PPMI_vec_to_probability_vec(v1)
    # Create (v1 + v0) / 2 such that it conforms.
    js_q_1 = {} # Mapping from WID to (v1 + v0) / 2
    
    for key0 in p0.keys():
        if key0 in p1.keys():
            # Covers the intersection
            js_q_1[key0] = (p0[key0] + p1[key0]) / 2
        else:
            # Covers only v0
            js_q_1[key0] = p0[key0] / 2
        
    for key1 in p1.keys():
        if key1 not in p0.keys():
            js_q_1[key1] = p1[key1] / 2
        
    KL_sim = 1 - 0.5*(Kuh_sim(p0, js_q_1) + Kuh_sim(p1, js_q_1))
    return KL_sim
#def create_TFIDF_vectors(co_counts, )

    
def create_coc_vectors(wids, co_counts):
    vectors = {}
    for wid0 in wids:
        vectors[wid0] = {}
        vector  = {}
        
        for wid1, co_count in co_counts[wid0].items():
            vector[wid1] = co_count
        
        vectors[wid0] = vector
    return vectors

def create_ppmi_vectors(wids, o_counts, co_counts, tot_count):
    '''Creates context vectors for the words in wids, using PPMI.
    These should be sparse vectors.

    :type wids: list of int
    :type o_counts: dict
    :type co_counts: dict of dict
    :type tot_count: int
    :param wids: the ids of the words to make vectors for
    :param o_counts: the counts of each word (indexed by id)
    :param co_counts: the cooccurrence counts of each word pair (indexed by ids)
    :param tot_count: the total number of observations
    :rtype: dict
    :return: the context vectors, indexed by word id
    '''
    vectors = {}
    for wid0 in wids:
        # Initialise dictionaries 
        vectors[wid0] = {}
        vector        = {}
        
        # Get the counts and co_counts
        for wid1, co_count in co_counts[wid0].items():
            # Get the word id of co_count_dict
            C_xy = co_count
            C_x  = o_counts[wid0]
            C_y  = o_counts[wid1]
            N    = tot_count
            PPMI = max(0, PMI(C_xy, C_x, C_y, N))
            
            #            if C_y <= 800:
            #                PPMI = 0
        
            if PPMI != 0:
                vector[wid1] = PPMI
            
        vectors[wid0] = vector
        
    return vectors


def read_counts(filename, wids):
  '''Reads the counts from file. It returns counts for all words, but to
  save memory it only returns cooccurrence counts for the words
  whose ids are listed in wids.

  :type filename: string
  :type wids: list
  :param filename: where to read info from
  :param wids: a list of word ids
  :returns: occurence counts, cooccurence counts, and tot number of observations
  '''
  o_counts = {} # Occurence counts ** 0.9 
  co_counts = {} # Cooccurence counts
  fp = open(filename)
  N = float(next(fp))
  for line in fp:
    line = line.strip().split("\t")
    wid0 = int(line[0])
    o_counts[wid0] = int(line[1])
    if(wid0 in wids):
        co_counts[wid0] = dict([int(y) for y in x.split(" ")] for x in line[2:])
  return (o_counts, co_counts, N)

def print_sorted_pairs(similarities, o_counts, first=0, last=100):
  '''Sorts the pairs of words by their similarity scores and prints
  out the sorted list from index first to last, along with the
  counts of each word in each pair.

  :type similarities: dict 
  :type o_counts: dict
  :type first: int
  :type last: int
  :param similarities: the word id pairs (keys) with similarity scores (values)
  :param o_counts: the counts of each word id
  :param first: index to start printing from
  :param last: index to stop printing
  :return: none
  '''
  if first < 0: last = len(similarities)
  for pair in sorted(similarities.keys(), key=lambda x: similarities[x], reverse = True)[first:last]:
    word_pair = (wid2word[pair[0]], wid2word[pair[1]])
    print("{:.2f}\t{:30}\t{}\t{}".format(similarities[pair],str(word_pair),
                                         o_counts[pair[0]],o_counts[pair[1]]))

def freq_v_sim(sims):
  xs = []
  ys = []
  for pair in sims.items():
    ys.append(pair[1])
    c0 = o_counts[pair[0][0]]
    c1 = o_counts[pair[0][1]]
    xs.append(min(c0,c1))
  plt.clf() # clear previous plots (if any)
  plt.xscale('log') #set x axis to log scale. Must do *before* creating plot
  plt.plot(xs, ys, 'k.') # create the scatter plot
  plt.xlabel('Min Freq')
  plt.ylabel('Similarity')
  print("Freq vs Similarity Spearman correlation = {:.2f}".format(spearmanr(xs,ys)[0]))
#  plt.show() #display the set of plots

def make_pairs(items):
  '''Takes a list of items and creates a list of the unique pairs
  with each pair sorted, so that if (a, b) is a pair, (b, a) is not
  also included. Self-pairs (a, a) are also not included.

  :type items: list
  :param items: the list to pair up
  :return: list of pairs

  '''
  return [(x, y) for x in items for y in items if x < y]

def special_pairs(wid, wids):
    return [(wid, y) for y in wids]
    
def compare_difference(v0, v1):
    diff = {}
    for i in v0.keys():
        if i in v1.keys():
            diff[i] = v1[i] - v0[i]
    return diff

def compare_maxi(v0,v1):
    maxi= {}
    for i in v0.keys():
        if i in v1.keys():
            maxi[i] = max(v0[i], v1[i])
        else:
            maxi[i] = v0[i]
            
    for i in v1.keys():
        if i not in v0.keys():
            maxi[i] = v1[i]
    return maxi


        
def compare_intersection(v0, v1):
    inter= {}
    for i in v0.keys():
        if i in v1.keys():
            inter[i] = min(v0[i], v1[i])
    return inter

def print_dict(type_s, sorted_dict, elem_count, w1, w2, o_counts, co_counts):
    print("\n\nTop " + str(elem_count) + " " + type_s + " with words " + w1 + " and " + w2)
    i = 0
    
    wid1 = word2wid[w1]
    wid2 = word2wid[w2]
    print("o_count of " + w1 + " : " + str(o_counts[wid1]))
    print("o_count of " + w2 + " : " + str(o_counts[wid2]))
    
    for (k,v) in sorted_dict:
        
        print(str(wid2word[k]) + " " + type_s + ": " + str(v))
        print("o_count: " + str(o_counts[k]))
        print("co_count with " + w1 + ": " + str(co_counts[wid1][k]))
        print("co_count with " + w2 + ": " + str(co_counts[wid2][k]))
        i += 1
        
        if i == elem_count:
            break

    print("\n\n")

two_test_words = ['odd','weird']

stemmed_words = [tw_stemmer(w) for w in two_test_words]
all_wids = [word2wid[x] for x in stemmed_words] #stemming might create duplicates; remove them
wid_list = list(all_wids)
wid_pairs = special_pairs(wid_list[0],wid_list[1:len(wid_list)])

(o_counts, co_counts, N) = read_counts("/afs/inf.ed.ac.uk/group/teaching/anlp/asgn3/counts", all_wids)

#make the word vectors
vectors = create_ppmi_vectors(all_wids, o_counts, co_counts, N)





w1 = stemmed_words[0]
wid1 = word2wid[w1]
w2 = stemmed_words[1]
wid2 = word2wid[w2]
odd_x = []
odd_y = []
weird_x = []
weird_y = []
PPMI_vec1 = vectors[wid1]
PPMI_vec2 = vectors[wid2]
multiplier = 1
max_o_count_val = 100000*multiplier
# Differences

for wid, ppmi_val in PPMI_vec1.items():
    if wid not in PPMI_vec2.keys():
        if o_counts[wid] <= max_o_count_val:
            odd_x.append(o_counts[wid])
            odd_y.append(ppmi_val)

for wid, ppmi_val in PPMI_vec2.items():
    if wid not in PPMI_vec1.keys():
        if o_counts[wid] <= max_o_count_val:
            weird_x.append(o_counts[wid])
            weird_y.append(ppmi_val)

intersect_odd_x = []
intersect_odd_y = []
intersect_weird_x = []
intersect_weird_y = []

for wid, ppmi_val in PPMI_vec1.items():
    if wid in PPMI_vec2.keys():
        if o_counts[wid] <= max_o_count_val:
            intersect_odd_x.append(o_counts[wid])
            intersect_odd_y.append(ppmi_val)

for wid, ppmi_val in PPMI_vec2.items():
    if wid in PPMI_vec1.keys():
        if o_counts[wid] <= max_o_count_val:
            intersect_weird_x.append(o_counts[wid])
            intersect_weird_y.append(ppmi_val)

#total_ppmi = 



# This gets the elements of the difference and prints them


total = 0     
hash_or_at = 0       
low_freq_diff_odd = {}
for wid, ppmi_val in PPMI_vec2.items():
    if wid not in PPMI_vec1.keys():
        if o_counts[wid] <= 1000:
            total += 1
            if ("@" in (wid2word[wid]) or "#" in (wid2word[wid])):
                hash_or_at += 1
            # Creates a mapping from a string (word id and ppmi value) to ocounts.
            low_freq_diff_odd['word: '+str(wid2word[wid])+',  ppmi '+str(ppmi_val)+' cc: '+str(co_counts[wid2][wid])] = o_counts[wid]
low_freq_diff_odd  = sorted(low_freq_diff_odd.items(), key=lambda kv: kv[1], reverse = True)

#for kv in low_freq_diff_odd:
#    print(kv)
    

low_freq_diff_weird = {}
for wid, ppmi_val in PPMI_vec2.items():
    if wid not in PPMI_vec1.keys():
        if o_counts[wid] <= 1000:
            total += 1
            
            if ("@" in (wid2word[wid]) or "#" in (wid2word[wid])):
                hash_or_at += 1
            
            # Creates a mapping from a string (word id and ppmi value) to ocounts.
            low_freq_diff_weird['word: '+str(wid2word[wid])+',  ppmi '+str(ppmi_val)+' cc: '+str(co_counts[wid2][wid])] = o_counts[wid]
low_freq_diff_weird  = sorted(low_freq_diff_weird.items(), key=lambda kv: kv[1], reverse = True)

#for kv in low_freq_diff_weird:
#    print(kv)
    
    
# The same but for the intersection
print("\n\n\n")
print(str(total))
print(str(hash_or_at))
tot2 = 0
hash2 = 0
low_freq_intersect = {}
for wid, ppmi_val in PPMI_vec2.items():
    if wid in PPMI_vec1.keys():
        if o_counts[wid] <= 1000:
            tot2 += 1
            
            if ("@" in (wid2word[wid]) or "#" in (wid2word[wid])):
                hash2 += 1
            # Creates a mapping from a string (word id and ppmi value) to ocounts.
            low_freq_intersect['word: '+str(wid2word[wid])+',  ppmi '+str(ppmi_val)+' cc: '+str(co_counts[wid2][wid])] = o_counts[wid]
low_freq_intersect  = sorted(low_freq_intersect.items(), key=lambda kv: kv[1], reverse = True)

# This prints LOW frequency elements in the weird and odd context.

#for kv in low_freq_intersect:
#    print(kv)    

print("\n\n\n")
print(str(tot2))
print(str(hash2))






























odd_x = intersect_odd_x
odd_y = intersect_odd_y
weird_x = intersect_weird_x
weird_y = intersect_weird_y
''' THIS PRINTS AND LOGS THINGS '''

interval_resolution = 2000*multiplier
interval_bins = 50
intervals_odd = [0] * interval_bins       
for i in range(0, len(odd_x)):
    co_count_val = odd_x[i]
    ppmi_val     = odd_y[i]
    interval_num = floor(co_count_val / interval_resolution)
    if interval_num < interval_bins:
        intervals_odd[interval_num] +=  ppmi_val
        
intervals_weird = [0] * interval_bins       
for i in range(0, len(weird_x)):
    co_count_val = weird_x[i]
    ppmi_val     = weird_y[i]
    interval_num = floor(co_count_val / interval_resolution)
    if interval_num < interval_bins:
        intervals_weird[interval_num] +=  ppmi_val

sum_total = sum(PPMI_vec1.values()) + sum(PPMI_vec2.values())

intervals_odd = [i / sum_total for i in intervals_odd]
intervals_weird = [0 / sum_total for i in intervals_weird]


print(sum(intervals_odd) + sum(intervals_weird))

intervals_total = [intervals_odd[i] + intervals_weird[i] for i in range(len(intervals_odd))]

fig1 = plt.figure()
plt.plot(odd_x, odd_y, 'g.', label = 'odd context words', markersize = 1.5)
plt.plot(weird_x, weird_y, 'r.', label = 'weird context words', markersize = 1.5)
plt.legend()
plt.title('PPMI value versus occurrence count value for overlapping non-zero entries')
plt.ylabel('PPMI value')
plt.xlabel('occurence count')
fig1.savefig('odd  ', bbox_inches = 'tight', dpi =400)
plt.show()


#fig1 = plt.figure()
#plt.plot(odd_x, odd_y, 'g.', label = 'odd context words', markersize = 1.5)
#plt.legend()
#plt.title('PPMI value versus co-occurrence value for non-overlapping non-zero entries')
#plt.ylabel('PPMI value')
#plt.xlabel('co_counts')
#fig1.savefig('odd context words2', bbox_inches = 'tight', dpi =400)
#plt.show()

fig2 = plt.figure()
plt.plot(weird_x, weird_y, 'c.', label = 'shine context words', markersize = 1.5)
plt.legend()
plt.title('PPMI value versus occurrence count value for non-overlapping non-zero entries')
plt.ylabel('PPMI value')
plt.xlabel('occurence count')
fig2.savefig('weird context words', bbox_inches = 'tight', dpi =400)
plt.show()
#    

def to_percent(y, position):
    return str(round(float(100 * y), 2)) + '%'
 

fig3 = plt.figure()
#plt.plot(intervals_odd, 'g*', label = 'odd')
x_location = np.linspace(0,98000*multiplier,50)
plt.bar(x_location, intervals_total, width = 1500*multiplier, color = 'orange', alpha = 0.8, label = "Proportion of non-overlapping norm \n for words 'balloon' and 'shine' ")
plt.legend()
#plt.plot(intervals_total, 'r*', label = 'weird')
#plt.legend()
formatter  = FuncFormatter(to_percent)
plt.gca().yaxis.set_major_formatter(formatter)
#plt.xticks(np.linspace(0,40,11),np.linspace(0,200,11))
plt.title('Non-overlapping L1 norm divided by total L1 norm versus occurence count')
plt.ylabel('Proportion')
plt.xlabel('occurence count')
#plt.yaxis.set_major_formatter(FormatStrFormatter('%.2f'))
fig3.savefig('Proportion1', bbox_inches = 'tight', dpi =400)
plt.show()




''' THIS DOES FREQUENCY VS O_COUNTS '''
#bin_freq_count = odd_x + weird_x
#bin_size = 1000
#num_bins = 100
#bins_x_1 = [0] * num_bins
#for i in range(0, len(bin_freq_count)):
#    interval_num = floor(bin_freq_count[i]/bin_size)
#    bins_x_1[interval_num] += 1
#    
#bins_x_2 = [0] * num_bins
#for i in range(0, len(intersect_odd_weird_x)):
#    interval_num = floor(intersect_odd_weird_x[i]/bin_size)
#    bins_x_2[interval_num] += 1
#    
#bins_y = [x for x in range(0, num_bins)]
#

















''' THIS IS THE REST ''' 


intersection_dict = compare_intersection(vectors[all_wids[0]], vectors[all_wids[1]])
difference_dict   = compare_difference(vectors[all_wids[0]], vectors[all_wids[1]])
maxi_dict         = compare_maxi(vectors[all_wids[0]], vectors[all_wids[1]])


c_sims = {(wid0,wid1): cos_sim(vectors[wid0],vectors[wid1]) for (wid0,wid1) in wid_pairs}
j_sims = {(wid0,wid1): Jac_sim(vectors[wid0],vectors[wid1]) for (wid0,wid1) in wid_pairs}
jen_sims = {(wid0,wid1): Jen2_sim(vectors[wid0],vectors[wid1]) for (wid0,wid1) in wid_pairs}

intersect_vals  = sorted(intersection_dict.items(), key=lambda kv: kv[1], reverse = True)
difference_vals = sorted(difference_dict.items(), key=lambda kv: kv[1], reverse = True)
maxi_vals       = sorted(maxi_dict.items(), key=lambda kv: kv[1], reverse = True)







len1 = len(vectors[all_wids[0]])
len2 = len(vectors[all_wids[1]])
w1 = stemmed_words[0]
w2 = stemmed_words[1]
o1 = o_counts[all_wids[0]]
o2 = o_counts[all_wids[1]]
print("\n\n\n----------------")
print("word frequency for: " + w1 + " : " + str(o1))
print("word frequency for: " + w2 + " : " + str(o2))
print("word frequency ratio: " + str(o1/o1) + " :: " + str(o2/o1))

print("PPMI spread ratio: " + str(len1/len1) + " :: " + str(len2/len1))
print("PPMI spread of word: " + w1 + " " + str(len1) )
print("PPMI spread of word: " + w2 + " " + str(len2) )
print("# of intersection: " + str(len(intersection_dict)))
print("# of maximum: " + str(len(maxi_dict)))
print("\n\n\n----------------")
#print_dict("intersections", intersect_vals, 100, stemmed_words[0], stemmed_words[1], o_counts, co_counts)
#print_dict("differences", difference_vals, 1000, stemmed_words[0], stemmed_words[1], o_counts, co_counts)

print("Sort by cosine similarity\n\n\n")
print_sorted_pairs(c_sims, o_counts)
print("Sort by Jaccard similarity\n\n\n")
print_sorted_pairs(j_sims, o_counts)
print("Sort by Jensen-Shannon similarity\n\n\n")
print_sorted_pairs(jen_sims, o_counts)














