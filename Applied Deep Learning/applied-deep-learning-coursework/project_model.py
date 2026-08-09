from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import sys
import os

import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt
import cPickle as pickle
import batch_generator as batches
import img_ops as im_ops
import sign_labels as signs
from tensorflow.python.ops.variables import Variable

#to run our attempt at replicating Zhang et Al, run with WHITENING_ENABLED=True and DROPOUT and HIST... as False
WHITENING_ENABLED = True
DROPOUT_ENABLED = False
HISTOGRAM_EQUALIZATION_ENABLED = False
NUM_EPOCHS = 50 #if you run with the improvements turned on, SET THE EPOCHS TO 150!
########-------------------#########
########-------------------#########
LOCAL_RESPONSE_NORM_ENABLED = False
BATCH_NORMALIZATION_ENABLED = False
WRITE_FILTERS_TO_DISK_ENABLED = False
WRITE_MISTAKES_TO_DISK_ENABLED = False
IMAGE_FLIPPING_ENABLED = False
ROTATE_AND_PROJECT_ENABLED = False
TEST_AFTER_EVERY_EPOCH = True
########-------------------#########
#these arrays store a running total of the true number of signs seen by the network and the number of signs correctly guessed in each gategory
correct_signs = np.zeros(6)
temp_correct_signs = np.zeros(6)
total_signs = [4320,1500,210,1770,2790,2040]
epoch_accuracies = []
CLASS_COUNT = 43
 
FLAGS = tf.app.flags.FLAGS
tf.app.flags.DEFINE_integer  ('log-frequency', 10, 'Number of steps between logging results to the console and saving summaries.' + ' (default: %(default)d)')
tf.app.flags.DEFINE_integer  ('flush-frequency', 50, 'Number of steps between flushing summary results. (default: %(default)d)')
tf.app.flags.DEFINE_integer  ('save-model-frequency', 100, 'Number of steps between model saves. (default: %(default)d)')
tf.app.flags.DEFINE_string   ('log-dir', '{cwd}/logs/'.format(cwd=os.getcwd()), 'Directory where to write event logs and checkpoint. (default: %(default)s)')
tf.app.flags.DEFINE_integer  ('max-steps', NUM_EPOCHS, 'Number of mini-batches to train on. (default: %(default)d)')
tf.app.flags.DEFINE_float    ('learning-rate', 1e-2, 'Number of examples to run. (default: %(default)d)')
tf.app.flags.DEFINE_float    ('momentum', 0.9, 'Momentum described in paper. (default: %(default)d)')
tf.app.flags.DEFINE_float    ('weight_decay', 1e-4, 'Weight decay described in the paper. (default: %(default)d)')
tf.app.flags.DEFINE_integer  ('batch-size', 100, 'Number of examples per mini-batch. (default: %(default)d)')

run_log_dir     = os.path.join(FLAGS.log_dir, ('proj_bs_wh_{wh}_do_{do}_lnorm_{lnorm}_flip_{flip}_rotproj_{rot_proj}_equ_{equ}!').format(wh=WHITENING_ENABLED, do=DROPOUT_ENABLED, lnorm=LOCAL_RESPONSE_NORM_ENABLED, flip=IMAGE_FLIPPING_ENABLED, rot_proj=ROTATE_AND_PROJECT_ENABLED, equ=HISTOGRAM_EQUALIZATION_ENABLED))
checkpoint_path = os.path.join(run_log_dir, 'model.ckpt')
gpu_options     = tf.GPUOptions(per_process_gpu_memory_fraction=0.33)
    
def deepnn(x_image, initializer, bias_initializer, training, class_count=43):
    
    def true_fn():
        return tf.contrib.image.rotate(
            tf.contrib.image.transform( x_image, tf.contrib.image.angles_to_projective_transforms( tf.random_uniform([1], -0.2,0.2, tf.float32), 32, 32 ) ), tf.random_uniform([1], -0.2,0.2, tf.float32)  )
    
    def false_fn():
        return x_image
    if(ROTATE_AND_PROJECT_ENABLED): 
        x_image = tf.cond(training, true_fn=true_fn, false_fn=false_fn)
    
    conv1 = tf.layers.conv2d(
        inputs=x_image,
        filters=32,
        kernel_size=[5, 5],
        padding='same',
        activation=tf.nn.relu,
        use_bias=False,
        name='conv1',
        strides=1,
        kernel_initializer=initializer,
        bias_initializer=bias_initializer
    )

    if(LOCAL_RESPONSE_NORM_ENABLED):
        conv1 = tf.nn.local_response_normalization( input=conv1, depth_radius=5, bias=2, alpha=1e-4, beta=0.75 )
    if(BATCH_NORMALIZATION_ENABLED):
        conv1 = tf.nn.relu(tf.layers.batch_normalization(conv1))

    pool1 = tf.layers.average_pooling2d(
        inputs=conv1,
        pool_size=[3, 3],
        strides=2,
        padding="same",
        name='pool1'
    )

    if(DROPOUT_ENABLED):
        pool1 = tf.cond(training, lambda: tf.nn.dropout(pool1, keep_prob=0.9, name='dropout1'), lambda: pool1)

    conv2 = tf.layers.conv2d(
        inputs=pool1,
        filters=32,
        kernel_size=[5, 5],
        padding='same',
        activation=tf.nn.relu,
        use_bias=False,
        name='conv2',
        strides=1,
        kernel_initializer=initializer,
        bias_initializer=bias_initializer
    )

    if(LOCAL_RESPONSE_NORM_ENABLED):
        conv2 = tf.nn.local_response_normalization( input=conv2, depth_radius=5, bias=2, alpha=1e-4, beta=0.75 )
    if(BATCH_NORMALIZATION_ENABLED):
        conv2 = tf.nn.relu(tf.layers.batch_normalization(conv2))

    pool2 = tf.layers.average_pooling2d(
        inputs=conv2,
        pool_size=[3, 3],
        strides=2,
        name='pool2',
        padding="same"
    )

    if(DROPOUT_ENABLED):
        pool2 = tf.cond(training, lambda: tf.nn.dropout(pool2, keep_prob=0.75, name='dropout2'), lambda: pool2)

    conv3 = tf.layers.conv2d(
        inputs=pool2,
        filters=64,
        kernel_size=[5, 5],
        padding='same',
        activation=tf.nn.relu,
        use_bias=False,
        name='conv3',
        strides=1,
        kernel_initializer=initializer,
        bias_initializer=bias_initializer
    )

    if(LOCAL_RESPONSE_NORM_ENABLED):
        conv3 = tf.nn.local_response_normalization( input=conv3, depth_radius=5, bias=2, alpha=1e-4, beta=0.75 )
    if(BATCH_NORMALIZATION_ENABLED):
        conv3 = tf.nn.relu(tf.layers.batch_normalization(conv3))

    pool3 = tf.layers.max_pooling2d(
        inputs=conv3,
        pool_size=[3, 3],
        strides=2,
        name='pool3',
        padding="same"
    )

    if(DROPOUT_ENABLED):
        pool3 = tf.cond(training, lambda: tf.nn.dropout(pool3, keep_prob=0.75, name='dropout3'), lambda: pool3)

    pool3_flat  = tf.reshape(pool3, [-1, 4 * 4 * 64], name='pool3_flattened')
    fc1         = tf.layers.dense(inputs=pool3_flat, activation=tf.nn.relu, units=64, name='fc1', kernel_initializer=initializer, bias_initializer=bias_initializer)
    
    if(DROPOUT_ENABLED):
        fc1     = tf.cond(training, lambda: tf.nn.dropout(fc1, keep_prob=0.5, name='dropout4'), lambda: fc1)

    fc2         = tf.layers.dense(inputs=fc1, activation=tf.nn.relu, units=64, name='fc2', kernel_initializer=initializer, bias_initializer=bias_initializer)

    if(DROPOUT_ENABLED):
        fc2     = tf.cond(training, lambda: tf.nn.dropout(fc2, keep_prob=0.5, name='dropout5'), lambda: fc2)

    logits      = tf.layers.dense(inputs=fc2, activation=tf.nn.relu, units=43, name='fc3', kernel_initializer=initializer, bias_initializer=bias_initializer)

    return logits

def change_gradients(grads, weights):
    exp1 = tf.multiply(FLAGS.weight_decay, FLAGS.learning_rate)
    exp2 = tf.scalar_mul(exp1, weights)
    exp3 = tf.negative(exp2)
    #Now have - weight_decay * learning_rate * w_i
    #Now construct: - learning_rate * loss
    exp4 = tf.scalar_mul(FLAGS.learning_rate, grads)
    exp5 = tf.negative(exp4)
    exp6 = tf.add(exp5, exp3)
    return exp6

def main(_):
    tf.reset_default_graph()
    if(WRITE_MISTAKES_TO_DISK_ENABLED ):
        im_ops.make_dir_if_not_exist("mistakes")
    if(WRITE_FILTERS_TO_DISK_ENABLED):
        im_ops.make_dir_if_not_exist("filters")

    dataset_file = 'dataset.pkl'
    if(HISTOGRAM_EQUALIZATION_ENABLED):
        dataset_file = "equalized_dataset.pkl"
    elif(IMAGE_FLIPPING_ENABLED):
        dataset_file = 'augmented_whitened_dataset.pkl'
    elif(WHITENING_ENABLED):
        dataset_file = 'whitened_dataset.pkl'
    else:
        dataset_file = "dataset.pkl"
    
    data            = pickle.load(open(dataset_file, 'rb'))
    train_batches   = batches.batch_generator(data, "train", batch_size=100)
    test_batches    = batches.batch_generator(data, "test", batch_size=100)

    with tf.name_scope('inputs'):
        x        = tf.placeholder(tf.float32, shape=[None, 32 * 32 * 3])
        x_image  = tf.placeholder(tf.float32, shape=[None, 32, 32, 3])
        y_       = tf.placeholder(tf.float32, shape=[None, CLASS_COUNT])
        training = tf.placeholder(tf.bool)

    with tf.variable_scope('model'):
        initializer             = tf.random_uniform_initializer(-0.05, 0.05, 0, dtype=tf.float32)
        config                  = initializer.get_config()
        initializer             = tf.random_uniform_initializer.from_config(config)

        bias_initializer        = tf.random_uniform_initializer(-1, -1, 0, dtype=tf.float32)
        config                  = initializer.get_config()
        bias_initializer        = tf.random_uniform_initializer.from_config(config)

        logits                  = deepnn(x_image, initializer, bias_initializer, training)
        cross_entropy           = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(labels=y_, logits=logits))

        correct_labels          = tf.argmax(y_, axis=1, name='correct_labels')
        logits_predictions      = tf.argmax(logits, axis=1, name='logits_predictions')
        correct_prediction      = tf.equal(correct_labels, logits_predictions )

        accuracy                = tf.reduce_mean(tf.cast(correct_prediction, tf.float32), name='accuracy')
        global_step             = tf.Variable(0, name='global_step', trainable=False) # this will be incremented automatically by tensorflow
        opt                     = tf.train.MomentumOptimizer(learning_rate=-1, momentum=FLAGS.momentum)
        grads_and_vars          = opt.compute_gradients(loss=cross_entropy)
        changed_gradients       = [(change_gradients(gv[0], gv[1]), gv[1]) for gv in grads_and_vars]
        train_step              = opt.apply_gradients(grads_and_vars=changed_gradients, global_step=global_step)

    loss_summary            = tf.summary.scalar("Loss", cross_entropy)
    accuracy_summary        = tf.summary.scalar("Accuracy", accuracy)
    learning_rate_summary   = tf.summary.scalar("Learning Rate", 0)
    img_summary             = tf.summary.image('Input Images', x_image)
    test_img_summary        = tf.summary.image('Test Images', x_image)
    train_summary           = tf.summary.merge([loss_summary, accuracy_summary, learning_rate_summary, img_summary])
    validation_summary      = tf.summary.merge([loss_summary, accuracy_summary])

    saver = tf.train.Saver(tf.global_variables(), max_to_keep=1)
    with tf.Session() as sess:

        sess.run(tf.global_variables_initializer())

        train_writer        = tf.summary.FileWriter(run_log_dir + "_train", sess.graph)
        validation_writer   = tf.summary.FileWriter(run_log_dir + "_validation", sess.graph)

        step = 0
        current_best_accuracy = 0
        elapsed_epochs = 0

        # Training and validation
        for x in range(0, FLAGS.max_steps, 1):
            for train_images, train_labels in batches.batch_generator(data, "train"):
                test_gen                 = batches.batch_generator(data, "test")
                test_images, test_labels = test_gen.next()

                step = step + 1

                _, train_summary_str= sess.run([train_step, train_summary], feed_dict={x_image: train_images, y_: train_labels, training: True})

                # Validation: Monitoring accuracy using validation set
                if step % FLAGS.log_frequency == 0:
                    train_writer.add_summary(train_summary_str, step)
                    validation_accuracy, validation_summary_str = sess.run([accuracy, validation_summary], feed_dict={x_image: test_images, y_: test_labels, training: False})
                    print('step {}, accuracy on validation set : {}'.format(step, validation_accuracy))
                    validation_writer.add_summary(validation_summary_str, step)

                # Save the model checkpoint periodically.
                if step % FLAGS.save_model_frequency == 0 or (step + 1) == FLAGS.max_steps:
                    saver.save(sess, checkpoint_path, global_step=step)

                if step % FLAGS.flush_frequency == 0:
                    train_writer.flush()
                    validation_writer.flush()
                
            if(TEST_AFTER_EVERY_EPOCH):
                evaluated_images, test_accuracy = 0, 0
                temp_correct_signs.fill(0)
                for test_images, test_labels in batches.batch_generator(data, "test"):
                    test_accuracy_temp, correct_labels_list, logits_predictions_list = sess.run([accuracy, correct_labels, logits_predictions], feed_dict={x_image: test_images, y_: test_labels, training: False})
                    evaluated_images += len(test_labels)
                    test_accuracy += test_accuracy_temp

                    for idx, val in enumerate(correct_labels_list):
                        sign_index = signs.getSignIndex(val)
                        if val == logits_predictions_list[idx]:
                            temp_correct_signs[sign_index] += 1

                #test_accuracy = np.sum(temp_correct_signs)/12360.0
                test_accuracy = test_accuracy /( evaluated_images /100.0 )

                if(test_accuracy > current_best_accuracy):
                    correct_signs = temp_correct_signs.copy()
                    current_best_accuracy = test_accuracy
                epoch_accuracies.append( test_accuracy )
                elapsed_epochs += 1

        # Resetting the internal batch indices
        test_accuracy, batch_count, num_mistakes = 0, 0, 0

        if(WRITE_FILTERS_TO_DISK_ENABLED):
            conv1_kernel_val = tf.get_default_graph().get_tensor_by_name("model/conv1/kernel:0").eval()
            conv2_kernel_val = tf.get_default_graph().get_tensor_by_name("model/conv2/kernel:0").eval()
            im_ops.write_filters_to_disk(conv1_kernel_val, conv2_kernel_val)

        while batch_count <= 126:
            (test_images, test_labels)  = test_batches.next()

            temp_correct_signs.fill(0)

            test_accuracy_temp, correct_labels_list, logits_predictions_list = sess.run([accuracy, correct_labels, logits_predictions], feed_dict={x_image: test_images, y_: test_labels, training: False})
            #this loop goes through the true sign labels and increments the counters in signs_total, and if the network predicts the right sign, also increments the counters in correct_signs
            for idx, val in enumerate(correct_labels_list):
                #print("val: ", val)
                sign_index = signs.getSignIndex(val)
                if val == logits_predictions_list[idx]:
                    temp_correct_signs[sign_index] += 1
                elif WRITE_MISTAKES_TO_DISK_ENABLED:
                    plt.imshow(im_ops.kernel_normalise(test_images[idx]))
                    plt.savefig("mistakes/MISTAKE_"+str(num_mistakes)+".png", bbox_inches='tight')
                    print("Written MISTAKE_", str(num_mistakes), " to disk")
                    num_mistakes += 1
                    
            batch_count                 += 1
            test_accuracy               += test_accuracy_temp

        test_accuracy = test_accuracy / batch_count

        if(TEST_AFTER_EVERY_EPOCH):
            for idx, acc in enumerate(epoch_accuracies):
                print("EPOCH: ", idx, ". Accuracy: ", acc )
            print("Best epoch: ", np.argmax(epoch_accuracies),". Accuracy: ", np.max(epoch_accuracies))       

        #this loop divides the number of correctly guessed signs by the totals to get the accuracy, then prints the
            for idx, val in enumerate(correct_signs):
                accuracy = val / float(total_signs[idx])
                print(('test set: accuracy on {si} signs: {ac} ({n1}/{n2} correct)').format(si=signs.sign_types[idx], ac=accuracy, n1=correct_signs[idx], n2=total_signs[idx]))
        print('test set: accuracy on test set: %0.3f' % test_accuracy)
        print('model saved to ' + checkpoint_path)

        train_writer.close()
        validation_writer.close()
        plt.close()

if __name__ == '__main__':
    tf.app.run(main=main)
