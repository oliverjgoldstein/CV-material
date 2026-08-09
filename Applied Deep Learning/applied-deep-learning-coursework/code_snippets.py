
        opt = tf.train.MomentumOptimizer(-1, FLAGS.momentum).minimize(log_loss, global_step=global_step)
        dLdw = opt.compute_gradients(log_loss) # Returns: [(gv[0], gv[1]),......,]

        W_i = dLdw[NEURON_N]

        # Modification of the gradient occurs here:

        # Calculation of dLdw occurs here:
        dLdw_Di = tf.reduce_mean(dLdw)[0]
        exp1 = tf.multiply(FLAGS.weight_decay, FLAGS.learning_rate)
        exp2 = tf.multiply(exp1, W_i)
        exp3 = tf.sub(0, exp2)
        # Now have: - weight_decay * learning_rate * W_i
        # Now construct: - learning_rate * (average_loss)
        exp4 = tf.multiply(FLAGS.learning_rate, dLdw_Di)
        exp5 = tf.sub(0, exp4)
        # Now add the two terms together:
        exp6 = tf.add(exp5, exp3)
        result = sess.run(exp6)




            average_gradient = tf.reduce_mean(grad_var_pairs)[0]
            1. Calculate the average value of the gradient = batch derivative.
            momentum_variable.assign(change_momentum_var())
            2. Implement the learning rule for the momentum variable.
            3. Update the weight
            4. Retain the state of the momentum variable







        opt = tf.train.MomentumOptimizer(FLAGS.learning_rate, FLAGS.momentum).minimize(log_loss, global_step=global_step)
        dLdw = opt.compute_gradients(log_loss, [weights])

        def update_momentum_variable(weights):
            return tf.assign(grad_weights,
                                                tf.sub(tf.sub(tf.multiply(FLAGS.momentum, previous_momentum_variable),
                                                tf.multiply((tf.multiply(FLAGS.weight_decay * FLAGS.learning_rate) * weight)),
                                                tf.multiply(FLAGS.learning_rate, grad_weights))))

        def update_weight(previous_weight, momentum_variable):
            return tf.add(previous_weight, momentum_variable)

        updated_weights = [(update_weight(w) for w in dLdw]


        opt.apply_gradients(updated_weights)

        Compute the gradients with compute_gradients()
        Process the gradients as you wish.
        Apply the processed gradients with apply_gradients().

        # Things added by me: (They should not work)
        momentum = tf.Variable(init_momentum, trainable=False)
