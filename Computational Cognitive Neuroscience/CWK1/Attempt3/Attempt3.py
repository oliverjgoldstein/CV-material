from __future__ import print_function
import numpy as np
import math
from scipy.stats import bernoulli
import random
from scipy.spatial.distance import euclidean
import Again
import copy

size = 10

def generate_mem():
    return [[(-1 if random.randint(0,1) == 0 else 1) for i in range(0, size)] for j in range(0,size)]

def sign(value):
    if value > 0:
        return 1
    else:
        return -1

def print_matrix(matrix):
    for i in range(0, size):
        for j in range(0, size):
            string_state = ""
            if matrix[i][j] == 1:
                print(" 1", end = '')
            else:
                print(matrix[i][j], end = '')

        print("\n")

def hamming_distance(degraded_mem, original):
    dist = 0
    for row in range(0, len(degraded_mem[0])):
        for col in range(0, len(degraded_mem[1])):
            if degraded_mem[row][col] != original[row][col]:
                dist += 1
    return dist

def flip(memory, prob):
    bernoulli_var = bernoulli.rvs(size=1,p=prob)[0]
    if bernoulli_var == 1:
        if memory == 1:
            return -1
        else:
            return 1
    else:
        return memory

def degrade_memory(memory, p):
    degraded = copy.deepcopy(memory)
    for row in range(0, len(memory)):
        for col in range(0, len(memory)):
            degraded[row][col] = flip(memory[row][col], p)
    return degraded

T = [[[[0 for x in range(0,size)]
      for y in range(0, size)]
      for i in range(0, size)]
      for j in range(0, size)]
# pruning_factor = 0.6
# degradation_factor = 0.0
for degradation_factor in [0, 0.2, 0.33]:
    for pruning_factor in [2.0]:
        for mems_used in range(9,10):
            memories = [generate_mem() for i in range(0, mems_used)]
            for x in range(0,size):
                for y in range(0, size):
                    for i in range(0, size):
                        for j in range(0, size):
                            T_xy_ij = 0
                            for mem in range(0, mems_used):
                                T_xy_ij += memories[mem][i][j] * memories[mem][x][y]

                            # T_xy_ij /= len(memories)

                            T[x][y][i][j] = T_xy_ij
                            T[i][j][x][y] = T_xy_ij

            # Set self weights to zero

            for x in range(0, size):
                for y in range(0, size):
                    for i in range(0, size):
                        for j in range(0, size):
                            if (x,y) == (i,j):
                                T[x][y][i][j] = 0
                                T[i][j][x][y] = 0

            for x in range(0, size):
                for y in range(0, size):
                    for i in range(0, size):
                        for j in range(0, size):
                            if abs(T[x][y][i][j]) < (pruning_factor * euclidean([x,y], [i,j])):
                                # print("Pruned!")
                                T[x][y][i][j] = 0
                                T[i][j][x][y] = 0

            total = 0
            total_runs = 10
            # Now set the state and determine input
            for runs in range(0, total_runs):
                for mem in range(0, mems_used):

                    degraded = degrade_memory(memories[mem], degradation_factor) #degraded_mems[mem]
                    # print_matrix(degraded)
                    # print_matrix(memories[mem])
                    for time in range(0, 20):
                        for x in np.random.permutation(size):
                            for y in np.random.permutation(size):
                                E = 0
                                for i in range(0, size):
                                    for j in range(0, size):
                                        E += T[i][j][x][y] * degraded[i][j]

                                prob_activation = 1/(1 + math.exp(-E/4))
                                bernoulli_var = bernoulli.rvs(size=1,p=prob_activation)[0]
                                if (bernoulli_var == 1):
                                    degraded[x][y] = 1
                                else:
                                    degraded[x][y] = -1
                                # degraded[x][y] = sign(E)

                    # print_matrix(degraded)
                    # print(hamming_distance(degraded, memories[mem]))
                    total += hamming_distance(degraded, memories[mem])
            print("degradation_factor:" + str(degradation_factor))
            print("pruning_factor:" + str(pruning_factor))
            if total_runs * mems_used == 0:
                print(0)
            else:
                total /= (total_runs * mems_used)
                print(total)
