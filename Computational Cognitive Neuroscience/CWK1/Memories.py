import random
a = -1
B = -1
# #

memory_one = [
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a]]

degraded_memory_one = [
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,1,a,a,a,a],
[a,a,a,a,1,a,a,a,a,a]]

memory_two = [
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1]]

degraded_memory_two = [
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,1,1],
[a,a,a,a,a,a,a,a,a,1]]


memory_three = [
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a]]

degraded_memory_three = [
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[1,1,a,a,a,a,a,a,a,a],
[a,1,a,a,a,a,a,a,a,a]]

memory_four = [
[a,a,a,a,a,a,a,a,a,a],
[a,a,a,a,a,a,a,a,a,a],
[a,a,a,a,a,a,a,a,a,a],
[a,a,a,1,1,1,a,a,a,a],
[a,a,a,1,1,1,a,a,a,a],
[a,a,a,1,1,1,a,a,a,a],
[a,a,a,a,a,a,a,a,a,a],
[a,a,a,a,a,a,a,a,a,a],
[a,a,a,a,a,a,a,a,a,a],
[a,a,a,a,a,a,a,a,a,a]]

degraded_memory_four = [
[a,a,a,a,a,a,a,a,a,a],
[a,a,a,a,a,a,a,a,a,a],
[a,a,a,a,a,a,a,a,a,a],
[a,a,a,1,1,1,a,a,a,a],
[a,a,a,1,1,a,a,a,a,a],
[a,a,a,1,1,1,a,a,a,a],
[a,a,a,a,a,a,a,a,a,a],
[a,a,a,a,a,a,a,a,a,a],
[a,a,a,a,a,a,a,a,a,a],
[a,a,a,a,a,a,a,a,a,a]]


memory_five = [
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a]]

degraded_memory_five = [
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,a,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a],
[1,1,1,a,1,1,1,1,a,a]]

# memory_one = [
# [a,a,1,a,a,1,a,a,a,1],
# [a,1,1,a,a,1,a,a,a,1],
# [a,a,a,1,a,a,a,1,a,1],
# [a,a,a,a,a,1,a,a,a,a],
# [a,a,a,1,a,a,a,a,a,a],
# [a,a,1,a,a,a,1,a,a,a],
# [a,a,a,a,a,1,a,a,1,a],
# [a,a,a,1,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,1,a,a,a,1,a]]
#
# degraded_memory_one = [
# [a,a,1,a,a,1,a,a,a,1],
# [a,1,B,a,a,1,a,a,a,1],
# [a,a,a,1,a,a,a,1,a,1],
# [a,a,a,a,a,1,a,a,a,a],
# [a,a,a,1,a,a,a,a,a,a],
# [a,a,1,a,a,a,1,a,a,a],
# [a,a,a,a,a,1,a,a,1,a],
# [a,a,a,1,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,1,a,a,a,B,a]]
#
# memory_two = [
# [1,a,a,a,a,a,a,a,a,a],
# [1,1,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,1,a,a,a],
# [a,a,a,a,a,a,a,1,1,a],
# [a,a,1,a,1,a,a,a,a,a],
# [a,1,a,a,a,a,1,a,a,a],
# [a,a,a,1,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,1,a],
# [a,a,1,a,a,a,1,a,a,a],
# [a,a,a,1,a,1,a,a,1,a]]
#
# degraded_memory_two = [
# [1,a,a,a,a,a,a,a,a,a],
# [B,1,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,1,a,a,a],
# [a,a,a,a,a,a,a,1,1,a],
# [a,a,1,a,1,a,a,a,a,a],
# [a,1,a,a,a,a,1,a,a,a],
# [a,a,a,1,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,1,a],
# [a,a,1,a,a,a,1,a,a,a],
# [a,a,a,B,a,1,a,a,1,a]]
#
# memory_three = [
# [a,1,a,a,1,a,a,1,a,a],
# [a,1,a,a,a,a,a,1,a,a],
# [a,a,a,a,a,1,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [1,a,a,1,a,a,a,a,a,a],
# [a,a,a,a,a,1,1,a,a,1],
# [1,1,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,1,a,a,a],
# [a,a,a,1,a,a,a,a,1,1],
# [1,a,a,a,a,a,a,a,1,a]]
#
# degraded_memory_three = [
# [a,1,a,a,B,a,a,1,a,a],
# [a,1,a,a,a,a,a,1,a,a],
# [a,a,a,a,a,B,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [1,a,a,1,a,a,a,a,a,a],
# [a,a,a,a,a,1,1,a,a,1],
# [1,1,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,1,a,a,a],
# [a,a,a,1,a,a,a,a,1,1],
# [1,a,a,a,a,a,a,a,1,a]]
#
# memory_four = [
# [a,a,a,1,a,a,1,a,1,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,1,a,a],
# [a,a,a,a,a,a,1,a,a,a],
# [a,a,1,a,a,a,a,a,a,1],
# [1,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,1,a,a,a,a,a],
# [a,1,a,a,a,a,a,1,a,a],
# [a,a,1,a,a,a,a,a,a,a],
# [a,a,a,a,a,1,1,1,a,a]]
#
# degraded_memory_four = [
# [a,a,a,1,a,a,1,a,1,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,1,a,a],
# [a,a,a,a,a,a,B,a,a,a],
# [a,a,1,a,a,a,a,a,a,1],
# [1,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,B,a,a,a,a,a],
# [a,1,a,a,a,a,a,1,a,a],
# [a,a,1,a,a,a,a,a,a,a],
# [a,a,a,a,a,1,1,1,a,a]]
#
# memory_five = [
# [a,a,a,a,a,a,a,a,a,1],
# [a,a,a,a,a,a,a,a,a,1],
# [a,a,a,a,a,a,a,a,a,1],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a]]
#
# degraded_memory_five = [
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,1],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a],
# [a,a,a,a,a,a,a,a,a,a]]














# memory_one = [
# [-1, 1],
# [-1, -1]
# ]
#
# degraded_memory_one = [
# [-1, 1],
# [-1, 1]
# ]
#
# memory_two = [
# [1, 1],
# [1, -1]
# ]
#
# degraded_memory_two = [
# [1, 1],
# [1, -1]
# ]

memories = [memory_one, memory_two, memory_three, memory_four, memory_five]
dmemories = [degraded_memory_one, degraded_memory_two, degraded_memory_three, degraded_memory_four, degraded_memory_five]

def random_memory_generator():
    return [[random.randint(-1,1) for col in range(0, 10)] for row in range(0, 10)]

# memories_rand = [random_memory_generator(),random_memory_generator(),random_memory_generator()]
