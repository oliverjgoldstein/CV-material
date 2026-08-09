import numpy as np
from skimage import exposure
import cPickle as pickle

data = pickle.load(open("dataset.pkl","rb"))

print( exposure.equalize_adapthist(data[0][0][0]) )

for image in range(0, len(data[0])):
    data[0][image] = (exposure.equalize_adapthist(data[0][image][0]), data[0][image][1])
for image in range(0, len(data[1])):
    data[1][image] = (exposure.equalize_adapthist(data[1][image][0]), data[1][image][1])

pickle.dump(data, open("equalized_dataset.pkl", "wb"), -1)

