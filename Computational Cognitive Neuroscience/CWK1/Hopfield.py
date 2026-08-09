from __future__ import print_function
import Neuron
import Memories
from scipy.spatial.distance import hamming
import random

size = 10
def main():
    network_params = (size,size) # col, row
    pruning_factor = 0.1
    network = [[Neuron.Neuron(col, row, pruning_factor, size) for col in range(0, network_params[0])] for row in range(0, network_params[1])]
    time_steps = 101

    network = encode_memories(network, network_params, Memories.memories)
    # Allow the network to settle.
    network = update_over_time(time_steps, network, network_params)
    pretty_print_network_weights(network, network_params)
    print("Degraded memory one:")
    demonstrate_performance(network, network_params, time_steps, Memories.memories[0], Memories.dmemories[0])
    # print("State:")
    # pretty_print_network_state(network, network_params)

    print("Degraded memory two:")
    demonstrate_performance(network, network_params, time_steps, Memories.memories[1], Memories.dmemories[1])
    print("Degraded memory three:")
    demonstrate_performance(network, network_params, time_steps, Memories.memories[2], Memories.dmemories[2])
    print("Degraded memory four:")
    demonstrate_performance(network, network_params, time_steps, Memories.memories[3], Memories.dmemories[3])
    # print("Degraded memory five:")
    # demonstrate_performance(network, network_params, time_steps, Memories.memories[4], Memories.dmemories[4])


    print("Success")

def encode_memories(network, network_params, memories):
    # Loop over all neurons
    for row in range(0, network_params[1]):
        for col in range(0, network_params[0]):
            # For each neuron pass the memories, as symmetricity over the network is enforced at the neuronal level
            # ... the whole network is returned.
            network = network[row][col].encode_memories(memories, network)

    return network

def update_over_time(time_steps, network, network_params):
    # Go through the states and update them depending on their input.
    for i in range(0, time_steps):
        for row in range(0, network_params[1]):
            for col in range(0, network_params[0]):
                x_val = random.randint(0,9)
                y_val = random.randint(0,9)
                network[x_val][y_val] = network[x_val][y_val].update_neuron_state(network, network_params)
                # network[row][col] = network[row][col].update_neuron_state_stochastic(network, network_params)
                # This prunes the whole network and as such returns the whole thing.
                # network = network[row][col].prune(network, network_params)
        # for row in range(0, network_params[1]):
            # for col in range(0, network_params[0]):
                # network[row][col].state = network[row][col].temp_prev_state

    return network

def set_network_state(network, network_params, memory):
    for row in range(0, network_params[1]):
        for col in range(0, network_params[0]):
            network[row][col] = network[row][col].set_state(memory[row][col])
    return network


def demonstrate_performance(network, network_params, time_steps, original_memory, degraded_memory):
    # Set the state of the neuron to a degraded memory
    network = set_network_state(network, network_params, degraded_memory)
    print("Network with degraded inputs and trained weights:")
    pretty_print_network_state(network, network_params)

    network = update_over_time(time_steps, network, network_params)
    print("Network with recovered inputs and trained weights:")
    pretty_print_network_state(network, network_params)
    hamming_dist = hamming_distance(network, original_memory, degraded_memory)
    print("Hamming distance between original_memory and network: " + str(hamming_dist))

def hamming_distance(network, original_memory, degraded_memory):
    hamming_distance = 0
    for row in range(0, size):
        for col in range(0, size):
            if network[row][col].state != original_memory[row][col]:
                hamming_distance += 1

    return hamming_distance


# Printing functions below

def pretty_print_network_weights(network, network_params):
    for row in range(0, network_params[1]):
        for col in range(0, network_params[0]):
            print(str(network[row][col].row) + "," + str(network[row][col].col))
            for wrow in network[row][col].weights:
                print(wrow)
            print("\n")
        print("\n")

def pretty_print_network_state(network, network_params):
    for row in range(0, network_params[1]):
        for col in range(0, network_params[0]):
            state = network[row][col].get_string_state()
            print(state + ",", end = '')
        print("")
    print("\n\n")

if __name__ == "__main__":
    main()
