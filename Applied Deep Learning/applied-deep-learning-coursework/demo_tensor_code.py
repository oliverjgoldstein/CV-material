""" Neural Network.

A 2-Hidden Layers Fully Connected Neural Network (a.k.a Multilayer Perceptron)
implementation with TensorFlow. This example is using the MNIST database
of handwritten digits (http://yann.lecun.com/exdb/mnist/).

Links:
    [MNIST Dataset](http://yann.lecun.com/exdb/mnist/).

Author: Aymeric Damien
Project: https://github.com/aymericdamien/TensorFlow-Examples/
"""

from __future__ import print_function

# Import MNIST data
from tensorflow.examples.tutorials.mnist import input_data
mnist = input_data.read_data_sets("/tmp/data/", one_hot=True)

import tensorflow as tf

# Parameters
learning_rate = 1e-2
weight_decay = 1e-4
momentum = 0.9
num_steps = 500
batch_size = 100
display_step = 100

# Network Parameters
n_hidden_1 = 256 # 1st layer number of neurons
n_hidden_2 = 256 # 2nd layer number of neurons
num_input = 784 # MNIST data input (img shape: 28*28)
num_classes = 10 # MNIST total classes (0-9 digits)

# tf Graph input
X = tf.placeholder("float", [None, num_input])
Y = tf.placeholder("float", [None, num_classes])

# Store layers weight & bias
weights = {
    'h1': tf.Variable(tf.random_normal([num_input, n_hidden_1])),
    'h2': tf.Variable(tf.random_normal([n_hidden_1, n_hidden_2])),
    'out': tf.Variable(tf.random_normal([n_hidden_2, num_classes]))
}

momenta = {
    'm1': tf.Variable(tf.random_normal([n_hidden_1]))
}

biases = {
    'b1': tf.Variable(tf.random_normal([n_hidden_1])),
    'b2': tf.Variable(tf.random_normal([n_hidden_2])),
    'out': tf.Variable(tf.random_normal([num_classes]))
}


# Create model
def neural_net(x):
    # Hidden fully connected layer with 256 neurons
    layer_1 = tf.add(tf.matmul(x, weights['h1']), biases['b1'])
    # Hidden fully connected layer with 256 neurons
    layer_2 = tf.add(tf.matmul(layer_1, weights['h2']), biases['b2'])
    # Output fully connected layer with a neuron for each class
    out_layer = tf.matmul(layer_2, weights['out']) + biases['out']
    return out_layer

# Construct model
logits = neural_net(X)
prediction = tf.nn.softmax(logits)



# opt = tf.train.MomentumOptimizer(FLAGS.learning_rate, FLAGS.momentum).minimize(log_loss, global_step=global_step)
# dLdw = opt.compute_gradients(log_loss, [weights])
#
# def update_momentum_variable(weights):
#     return tf.assign(grad_weights,
#                                         tf.sub(tf.sub(tf.multiply(FLAGS.momentum, previous_momentum_variable),
#                                         tf.multiply((tf.multiply(FLAGS.weight_decay * FLAGS.learning_rate) * weight)),
#                                         tf.multiply(FLAGS.learning_rate, grad_weights))))
#
# def update_weight(previous_weight, momentum_variable):
#     return tf.add(previous_weight, momentum_variable)
#
# updated_weights = [(update_weight(w) for w in dLdw]
#
#
# opt.apply_gradients(updated_weights)
#
# Compute the gradients with compute_gradients()
# Process the gradients as you wish.
# Apply the processed gradients with apply_gradients().
#
# # Things added by me: (They should not work)
# momentum = tf.Variable(init_momentum, trainable=False)



# Define loss and optimizer
loss_op = tf.reduce_mean(tf.losses.log_loss(predictions=prediction, labels=Y))
global_step = tf.Variable(0, trainable=False, name='global_step')

optimizer = tf.train.MomentumOptimizer(learning_rate=learning_rate, momentum=momentum)
train_op = optimizer.minimize(loss_op, global_step=global_step)





dLdw = optimizer.compute_gradients(loss_op)

momentum_variable = tf.Variable(0, trainable=False, dtype=tf.float32)
average_derivative = 5

def change_momentum_var(grad, var, momentum, momentum_var, weight_decay, learning_rate, average_derivative):
    return momentum * momentum_var - weight_decay * learning_rate * var - learning_rate * 5

def change_weight_var(weight, momentum_var):
    return weight + momentum_var

def change_grad_vars(grad_var_pairs):
    [(gv[0], gv[1]),......,]
    average_gradient = tf.reduce_mean(grad_var_pairs)[0]
    1. Calculate the average value of the gradient = batch derivative.
    momentum_variable.assign(change_momentum_var())
    2. Implement the learning rule for the momentum variable.
    3. Update the weight
    4. Retain the state of the momentum variable

optimizer.apply_gradients(changed_gradient_values)








# Evaluate model
correct_pred = tf.equal(tf.argmax(prediction, 1), tf.argmax(Y, 1))
accuracy = tf.reduce_mean(tf.cast(correct_pred, tf.float32))

# Initialize the variables (i.e. assign their default value)
init = tf.global_variables_initializer()

# Start training
with tf.Session() as sess:

    # Run the initializer
    sess.run(init)

    for step in range(1, num_steps+1):
        batch_x, batch_y = mnist.train.next_batch(batch_size)
        # Run optimization op (backprop)
        sess.run(train_op, feed_dict={X: batch_x, Y: batch_y})
        if step % display_step == 0 or step == 1:
            # Calculate batch loss and accuracy
            loss, acc = sess.run([loss_op, accuracy], feed_dict={X: batch_x, Y: batch_y})

            gradient_of_loss_wrt_w = sess.run([dLdw], feed_dict={X: batch_x, Y: batch_y})

            print("\n\nGradient of loss:" + str(gradient_of_loss_wrt_w) + "\n\n")

            print("Step " + str(step) + ", Minibatch Loss= " + \
                  "{:.4f}".format(loss) + ", Training Accuracy= " + \
                  "{:.3f}".format(acc))

    print("Optimization Finished!")

    # Calculate accuracy for MNIST test images
    print("Testing Accuracy:", \
        sess.run(accuracy, feed_dict={X: mnist.test.images,
                                  Y: mnist.test.labels}))
