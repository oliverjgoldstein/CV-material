Dear reader:

We implemented whitening as a preprocessing step that loads the file dataset.pkl and then creates another one called whitened_dataset.pkl.
This can be created by just calling "python translate_dataset.py" into the terminal. This file also has another flag in it related to a previous 
attempt to use flipping to augment the dataset, but by default that is turned off.

To create the dataset required to get our highest accuracy you must called "python hist_dataset.py"...
This uses a function from the library scikit image, which wasn't on blue crystal when I was fiddling with it. So I may have to upload the whole
dataset to SAFE.


To run the model run project_model.py

There are various flags that can be switched on and off. Only the top 3 should be touched. By defualy it will just be

WHITENING_ENABLED = True
DROPOUT_ENABLED = False
HISTOGRAM_EQUALIZATION_ENABLED = False

To run it with our improvements that we got the best accuracy with run with

WHITENING_ENABLED = False
DROPOUT_ENABLED = True
HISTOGRAM_EQUALIZATION_ENABLED = True
