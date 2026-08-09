import numpy as np 
import cPickle as pickle
import matplotlib.pyplot as plt
import random
import img_ops
import math

EXPORT_WHITENED_DATASET = True
EXPORT_WHITENED_AND_AUGMENTED_DATASET = False

data = pickle.load(open('dataset.pkl', 'rb'))

self_flippable_horizontally = np.array([11, 12, 13, 15, 17, 18, 22, 26, 30, 35])
self_flippable_vertically = np.array([1, 5, 12, 15, 17])
self_flippable_both = np.array([32, 40])
cross_flippable = np.array([[19, 20], [33, 34], [36, 37], [38, 39],[20, 19], [34, 33], [37, 36], [39, 38]])

def getClass( one_hot ):
    for idx, val  in enumerate( one_hot ):
        if(val == 1):
            return idx
    raise ValueError('Could not find a 1 in the one hot vector, something is wrong!')

def createOneHot(sign):
    one_hot = np.zeros(43)
    one_hot[sign] = 1
    return one_hot

def subtract_mean_and_divide(image, r_mean, g_mean, b_mean, r_stdv, g_stdv, b_stdv):
    r,g,b       = np.dsplit(image,3)
    r = (r - r_mean)/r_stdv
    g = (g - g_mean)/g_stdv
    b = (b - b_mean)/b_stdv
    return np.dstack((r,g,b))

if(EXPORT_WHITENED_DATASET):
    #for image in range(0, len(data[0]) ):
    #    data[0][image] = (img_ops.image_normalise( data[0][image][0] ), data[0][image][1])
    #for image in range(0, len(data[1]) ):
    #    data[1][image] = (img_ops.image_normalise( data[1][image][0] ), data[1][image][1])
    r_means = []
    g_means = []
    b_means = []
    r_vars = []
    g_vars = []
    b_vars = []
    for i in range(0,2):
        for(image in range(0, len(data[i]) ) ):
            r_means.append(np.mean(data[i][image][0][:,:,0] ) )
            g_means.append(np.mean(data[i][image][0][:,:,1] ) )
            b_means.append(np.mean(data[i][image][0][:,:,2] ) )
            r_vars.append(np.var(data[i][image][0][:,:,0]))
            g_vars.append(np.var(data[i][image][0][:,:,1]))
            b_vars.append(np.var(data[i][image][0][:,:,2]))
    r_mean, g_mean, b_mean  = np.mean(r_means), np.mean(g_means), np.mean(b_means)
    r_var, g_var, b_var = np.mean(r_vars), np.mean(g_var), np.mean(b_vars)
    r_stdv, g_stdv, b_stdv = math.sqrt(r_var), math.sqrt(g_stdv), math.sqrt(b_stdv)
    for image in range(0, len(data[0]) ):
        data[0][image] = (subtract_mean_and_divide( data[0][image][0] ), data[0][image][1])
    for image in range(0, len(data[1]) ):
        data[1][image] = (subtract_mean_and_divide( data[1][image][0] ), data[1][image][1])
    
training_image_list = data[0]

if(EXPORT_WHITENED_AND_AUGMENTED_DATASET):
    for image in range(0, len(data[0]) ):
        sign_class = getClass( data[0][image][1] )

        if sign_class in self_flippable_horizontally:
	    training_image_list.append( ( np.fliplr(data[0][image][0]), data[0][image][1]) )
        if sign_class in self_flippable_vertically:
	    training_image_list.append( ( np.flipud(data[0][image][0]), data[0][image][1]) )
        if sign_class in self_flippable_both:
	    training_image_list.append( ( np.fliplr( np.flipud(data[0][image][0]) ), data[0][image][1]) )
        if sign_class in cross_flippable[:,0]:
	        flip_class = cross_flippable[ cross_flippable[:,0] == sign_class ][0][1]
	        training_image_list.append( ( np.fliplr(data[0][image][0]), createOneHot(sign_class) )  )

if(EXPORT_WHITENED_AND_AUGMENTED_DATASET):
    pickle.dump( (training_image_list, data[1]), open("augmented_whitened_dataset.pkl", "wb"), -1)
elif(EXPORT_WHITENED_DATASET):
    pickle.dump( data, open("whitened_dataset.pkl", "wb"), -1) #-1 forces to use the highest

#so there are 12630 test set images...
#There are 39209 test images mate...




