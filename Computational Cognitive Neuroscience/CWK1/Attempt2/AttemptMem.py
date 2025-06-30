import random
a = -1

def random_memory_generator():
    return [random.randint(-1,1) for col in range(0, 100)]

# mem_one = [0, 0, 1, 0, 1, 0, -1, 0, -1, 1, 0, 1, 1, 0, 1, 0, 1, 0, -1, 1, 0, 1, 1, -1, 1, 0, -1, -1, 1, -1, -1, -1, 1, 0, 0, -1, 0, 1, -1, 0, -1, 1, -1, -1, 0, 0, 1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 0, 0, 1, -1, 0, 0, 1, 0, -1, 1, -1, 0, -1, 0, -1, -1, 1, -1, 1, 1, 1, -1, 1, 0, 1, 0, -1, 1, 0, 1, 1, 1, -1, -1, 0, 1, 0, 0, 0, 1, -1, 0, 1, 1]
























memory_one = [
1,1,a,a,a,a,a,a,a,a,
1,1,a,a,a,a,a,a,a,a,
1,1,a,a,a,a,a,a,a,a,
1,1,a,a,a,a,a,a,a,a,
1,1,a,a,a,a,a,a,a,a,
1,1,a,a,a,a,a,a,a,a,
1,1,a,a,a,a,a,a,a,a,
1,1,a,a,a,a,a,a,a,a,
1,1,1,1,1,1,1,1,1,1,
1,1,1,1,1,1,1,1,1,1]

degraded_memory_one = [
1,1,a,a,a,a,a,a,a,a,
1,1,a,a,a,a,a,a,a,a,
1,1,a,a,a,a,1,1,a,a,
1,1,a,a,a,a,a,a,a,a,
1,1,a,a,a,a,a,1,a,a,
1,1,a,a,a,a,a,a,a,a,
1,1,a,a,a,a,a,a,a,a,
1,1,a,a,a,a,a,a,a,a,
1,1,1,a,a,1,1,1,1,1,
1,1,1,1,1,a,1,1,1,1]

memory_two = [
a,a,a,a,a,a,a,a,a,a,
a,1,1,1,1,1,1,1,1,a,
a,1,1,1,1,1,1,1,1,a,
a,1,1,a,a,a,a,1,1,a,
a,1,1,a,a,a,a,1,1,a,
a,1,1,a,a,a,a,1,1,a,
a,1,1,a,a,a,a,1,1,a,
a,1,1,1,1,1,1,1,1,a,
a,1,1,1,1,1,1,1,1,a,
a,a,a,a,a,a,a,a,a,a]

degraded_memory_two = [
a,a,a,a,a,a,a,a,a,a,
a,1,1,1,1,1,1,1,1,a,
a,1,1,1,1,1,1,1,1,a,
a,1,1,a,a,a,a,1,1,a,
a,1,1,1,1,a,a,1,1,a,
a,1,1,a,a,a,a,1,1,a,
a,1,1,a,a,a,a,1,1,a,
a,1,1,1,1,1,a,1,1,a,
a,1,1,1,1,1,1,a,1,a,
a,a,a,a,a,a,a,a,a,a]

memory_three = [
a,a,a,a,1,a,a,a,a,a,
a,a,a,1,a,1,a,a,a,a,
a,a,1,a,a,a,1,a,a,a,
a,1,a,a,a,a,a,1,a,a,
1,1,1,1,1,1,1,1,1,a,
1,a,a,a,a,a,a,a,a,1,
1,a,a,a,a,a,a,a,a,1,
1,a,a,a,a,a,a,a,a,1,
1,a,a,a,a,a,a,a,a,1,
1,a,a,a,a,a,a,a,a,1]

degraded_memory_three = [
a,a,a,a,1,a,a,a,a,a,
a,a,a,1,a,1,a,a,a,a,
a,a,1,a,a,a,1,a,a,a,
a,1,a,a,a,a,a,1,a,a,
1,1,1,1,1,a,1,1,1,a,
1,a,a,a,a,a,a,a,a,1,
1,a,1,a,a,a,a,a,a,1,
1,a,a,a,a,a,a,a,a,1,
1,a,a,a,a,a,a,a,a,1,
1,a,a,a,a,a,a,a,a,1]

memory_four = [
1,a,a,a,a,a,a,a,a,1,
a,1,a,a,a,a,a,a,1,a,
a,a,1,a,a,a,a,1,a,a,
a,a,a,1,a,a,1,a,a,a,
a,a,a,a,1,1,a,a,a,a,
a,a,a,a,1,1,a,a,a,a,
a,a,a,1,a,a,1,a,a,a,
a,a,1,a,a,a,a,1,a,a,
a,1,a,a,a,a,a,a,1,a,
1,a,a,a,a,a,a,a,a,1]

degraded_memory_four = [
1,a,a,1,a,a,a,a,a,a,
a,1,a,a,1,a,a,a,1,a,
a,a,1,a,a,a,a,a,a,a,
a,a,a,1,a,a,1,a,a,a,
a,a,a,a,1,1,a,a,a,a,
a,a,a,a,1,1,a,a,a,a,
a,a,a,1,a,a,1,a,a,a,
a,a,1,a,a,a,a,1,a,a,
a,1,a,a,a,a,a,a,1,a,
1,a,a,a,a,a,a,a,a,1]


memory_five = [
1,1,a,a,a,a,a,a,1,1,
1,1,a,a,a,a,a,a,1,1,
1,1,a,a,a,a,a,a,1,1,
1,1,a,a,a,a,a,a,1,1,
1,1,1,1,1,1,1,1,1,1,
1,1,1,1,1,1,1,1,1,1,
1,1,a,a,a,a,a,a,1,1,
1,1,a,a,a,a,a,a,1,1,
1,1,a,a,a,a,a,a,1,1,
1,1,a,a,a,a,a,a,1,1]

degraded_memory_five = [
1,1,a,a,1,a,a,a,1,1,
1,1,a,a,a,a,a,a,1,1,
1,1,a,a,a,a,1,a,1,1,
1,1,a,a,a,a,a,a,1,1,
1,1,1,1,1,1,1,1,1,1,
1,1,1,1,1,1,a,1,1,1,
1,1,a,a,a,a,a,a,a,1,
1,1,a,a,a,a,a,a,1,1,
1,1,a,a,a,1,a,a,1,1,
1,1,a,a,a,a,a,a,1,1]

memory_six = [
a,a,a,a,1,1,a,a,a,a,
a,a,a,a,1,1,a,a,a,a,
1,1,1,1,1,1,1,1,1,1,
1,1,1,1,1,1,1,1,1,1,
a,a,a,a,1,1,a,a,a,a,
a,a,a,a,1,1,a,a,a,a,
a,a,a,a,1,1,a,a,a,a,
a,a,a,a,1,1,a,a,a,a,
a,a,a,a,1,1,a,a,a,a,
a,a,a,a,1,1,a,a,a,a]

degraded_memory_six = [
a,a,a,a,1,1,a,a,a,a,
a,a,a,a,1,1,a,a,a,a,
1,1,1,1,a,1,1,1,1,1,
1,1,a,1,1,1,a,1,1,1,
a,a,a,a,1,1,a,a,a,a,
a,a,1,a,1,1,a,a,1,a,
a,a,a,a,1,1,a,a,a,a,
a,1,a,a,1,1,a,a,a,a,
a,a,a,a,1,1,a,a,a,a,
a,a,a,a,1,1,a,a,a,a]

memory_seven = [
a,a,a,a,a,a,a,a,a,a,
a,a,a,a,a,a,a,a,a,a,
a,a,a,a,a,a,a,a,a,a,
a,a,1,1,a,a,a,a,1,1,
a,a,1,1,1,1,1,1,1,1,
a,a,1,1,a,a,a,a,1,1,
a,a,1,1,a,a,a,a,1,1,
a,a,a,a,a,a,a,a,1,1,
a,a,a,a,a,a,a,a,a,a,
a,a,a,a,a,a,a,a,a,a]

degraded_memory_seven = [
a,a,a,a,a,a,a,a,a,a,
a,a,a,a,1,a,a,a,a,a,
a,a,a,a,a,a,a,a,a,a,
a,a,1,1,a,a,a,a,1,1,
a,a,1,1,1,1,1,a,a,1,
a,a,a,1,a,a,a,a,1,1,
a,a,1,1,a,a,a,a,1,1,
a,a,a,a,a,a,a,a,1,1,
a,a,a,a,a,1,a,a,a,a,
a,a,a,a,1,a,a,a,a,a]





memory_eight = [
a,a,a,a,a,a,a,a,a,a,
a,a,a,a,a,a,a,a,a,a,
a,a,a,a,1,a,1,a,a,a,
a,a,a,a,1,a,1,a,a,a,
a,a,a,a,1,a,1,a,a,a,
a,a,a,a,1,a,1,a,a,a,
a,a,1,1,1,a,1,a,a,a,
a,a,1,1,1,a,1,a,a,a,
a,a,1,1,1,a,1,a,a,a,
a,a,1,1,1,a,1,a,a,a]
