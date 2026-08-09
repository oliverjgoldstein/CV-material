import numpy as np
import matplotlib.pyplot as plt
import os

def make_dir_if_not_exist(dirname):
    try:
        os.makedirs(dirname)
    except OSError:
        if not os.path.isdir(dirname):
            raise

# @args: np array of shape (X,Y,3)
# @returns: np array of shape (X,Y,3)
def image_normalise(image):

    r,g,b       = np.dsplit(image,3)

    r_mean, g_mean, b_mean = np.mean(r), np.mean(g), np.mean(b)

    r_std_dev, g_std_dev, b_std_dev  = np.std(r), np.std(g), np.std(b)

    remove_mean = lambda elem, mean, std_dev: (elem - mean) / std_dev
    np_func     = np.vectorize(remove_mean)
    r,g,b       = np_func(r, r_mean, r_std_dev), np_func(g, g_mean, g_std_dev), np_func(b, b_mean, b_std_dev)

    return np.dstack((r,g,b))

normalise   = lambda elem, _min, _max: (elem - _min) / (_max - _min)  

def kernel_normalise(kernel):
    
    r,g,b       = np.dsplit(kernel,3)

    r_min, g_min, b_min = np.min(r), np.min(g), np.min(b)  
    r_max, g_max, b_max = np.max(r), np.max(g), np.max(b) 

    r,g,b       = normalise(r, r_min, r_max), normalise(g, g_min, g_max), normalise(b, b_min, b_max)
    
    return np.dstack((r,g,b))

def inner_kernel_normalise(kernel):

    result = np.zeros((5,5))

    for y in range(0,5):
        for x in range(0,5):
            result[y][x] = np.sum( kernel[y,x,:] )

    return normalise(result, np.min(result), np.max(result))     

def inner_kernel_transformation(kernel):
    result = np.zeros((32, 5, 5, 1))

    for f in range(0,32):
        for y in range(0,5):
            for x in range(0,5):
                result[f][y][x][0] = np.sum( kernel[f,y,x,:])
    
    return result

def write_filters_to_disk(kernels1, kernels2):

    plt.axis("off")

    for i in range(0,32):
        image = kernel_normalise(kernels1[:,:,:,i])
        plt.imshow(image)
        plt.savefig("filters/CONV1_KERNEL_IMAGE_"+str(i)+".png", bbox_inches='tight')
    
        image = inner_kernel_normalise(kernels2[:,:,:,i])
        plt.imshow(image, cmap='gray')
        plt.savefig("filters/CONV2_KERNEL_IMAGE_"+str(i)+".png", bbox_inches='tight')
    plt.close()

        

