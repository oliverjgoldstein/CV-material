from __future__ import print_function
import numpy as np
import AttemptMem

def sign(value):
    if value > 0:
        return 1
    else:
        return -1

def print_matrix(matrix, size):
    for j in range(0, size):
        for i in range(0, size):
            string_state = ""
            if matrix[j*size + i] == 1:
                print(" 1", end = '')
            else:
                print(matrix[j*size + i], end = '')
        print("\n")

size = 10


T = [[0 for xy in range(0,size*size)] for ij in range(0,size*size)]

memories = [AttemptMem.memory_one,
            AttemptMem.memory_two,
            AttemptMem.memory_three,
            AttemptMem.memory_four,
            AttemptMem.memory_five,
            AttemptMem.memory_six,
            AttemptMem.memory_seven]

degraded = AttemptMem.degraded_memory_seven #[degraded_memory_one, degraded_memory_two, degraded_memory_three, degraded_memory_four, degraded_memory_five]

for x in range(0,size):
    for y in range(0,size):
        for i in range(0,size):
            for j in range(0,size):
                T_xy_ij = 0
                for mem in memories:
                    T_xy_ij += mem[x*size + y] * mem[i*size + j]
                # T_xy_ij /= len(memories)
                T[x*size + y][i*size + j] = T_xy_ij
                T[i*size + j][x*size + y] = T_xy_ij

# Set self weights to zero

for mem in range(0, 1):
    # Now set the state and determine input
    for time in range(0, 1000):
        for j in np.random.permutation(size*size):
            E = 0
            for x in range(0, size):
                for y in range(0, size):
                    E += T[x*size + y][j] * degraded[x*size + y]

            degraded[j] = sign(E)

print_matrix(degraded, size)
