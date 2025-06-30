import math
from scipy.stats import bernoulli
from scipy.spatial.distance import euclidean

class Neuron:
    def __init__(self, col, row, pruning_factor, size):
        self.col = col
        self.row = row
        self.state = 1
        self.temp_prev_state = 0
        self.weight_params = (size,size) # col, row
        self.weights = [[0 for i in range(0, self.weight_params[0])] for j in range(0, self.weight_params[1])]
        self.pruned_connections = [(self.row, self.col)]
        self.pruning_factor = pruning_factor

    def set_state(self, state):
        self.state = state
        return self

    def get_state(self):
        return self.state

    def get_string_state(self):
        string_state = ''
        if self.state == 1:
            string_state = " 1"
        else:
            string_state = "-1"
        return string_state

    def set_weights(self, weight, row, col):
        if (row, col) in self.pruned_connections:
            return self
        else:
            self.weights[row][col] = weight
        return self

    def get_weights(self, row, col):
        if (row, col) in self.pruned_connections:
            return 0
        else:
            return self.weights[row][col]

    def encode_memories(self, memories, network):
        # Go over all weights
        for row in range(0, self.weight_params[1]):
            for col in range(0, self.weight_params[0]):

                # Update them as per the formula
                new_weight = 0
                for mem in memories:
                    mu_i_j = mem[row][col]
                    mu_x_y = mem[self.row][self.col]
                    new_weight += mu_i_j * mu_x_y
                new_weight /= len(memories)
                # Update the weights with the sum over the formula given.
                # Update the weights over the network to enforce symmetricity. Won't have an effect.
                network[row][col] = network[row][col].set_weights(new_weight, self.row, self.col)
                network[self.row][self.col] = network[self.row][self.col].set_weights(new_weight, row, col)

        # update the weights for the symmetric neuron.
        return network

    def get_total_input(self, network, network_params):
        weights_input = 0
        # Go over the network
        for row in range(0, network_params[1]):
            for col in range(0, network_params[0]):
                if (row,col) not in self.pruned_connections:
                    # Get the input as state multiplied by the weights to this neuron.
                    presynaptic_neuron = network[row][col]
                    weights_input = weights_input + (presynaptic_neuron.get_weights(self.row, self.col) * presynaptic_neuron.state)

        return weights_input


    # This takes a
    # def prune_connection(self, network, network_params, row, col):
    #     # First reset the weights on both sides
    #     network[row][col].weights[self.row][self.col] = 0
    #     network[self.row][self.col].weights[row][col] = 0
    #     # Then prune the connections on both sides with side effects
    #     network[row][col].pruned_connections.append((self.row, self.col))
    #     network[self.row][self.col].pruned_connections.append((row, col))
    #
    #     return network

    # def prune(self, network, network_params):
    #     for row in range(0, self.weight_params[1]):
    #         for col in range(0, self.weight_params[0]):
    #             if (row, col) not in self.pruned_connections:
    #                 if self.get_weights(row,col) < (self.pruning_factor * euclidean([row,col], [self.row,self.col])):
    #                     # To prune a connection, first set it to zero, then add it to the pruned list.
    #                     network = self.prune_connection(network, network_params, row, col)
    #     return network
    #
    #
    #
    #
    # def update_neuron_state_stochastic(self, network, network_params):
    #     total_input = self.get_total_input(network, network_params)
    #     prob_activation = 1/(1 + math.exp(-total_input/4))
    #     bernoulli_var = bernoulli.rvs(size=1,p=prob_activation)[0]
    #     if bernoulli_var == 1:
    #         self.state = 1
    #     else:
    #         self.state = -1
    #     return self


    def update_neuron_state(self, network, network_params):
        total_input = self.get_total_input(network, network_params)
        if total_input > 0:
            self.state = 1
        else:
            self.state = -1
        return self
