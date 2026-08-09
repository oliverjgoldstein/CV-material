import matplotlib.pyplot as plt
import random as random
import numpy as np
weight_excitatory = 0.5*random.random()
weight_inhibitory = -1*random.random()

# # Default:
# a = 0.02
# b = 0.2
# c = -65
# d = 2
# v = -65
# u = b * v

# # Regular spiking
# a = 0.02
# b = 0.2
# c = -65
# d = 8
# v = -65
# u = b * v

# # Intrinsically bursting
# a = 0.02
# b = 0.2
# c = -55
# d = 4
# v = -65
# u = b * v

# # Chattering
# a = 0.02
# b = 0.2
# c = -50
# d = 2
# v = -65
# u = b * v

# # Fast spiking
# a = 0.1
# b = 0.2
# c = -65
# d = 2
# v = -65
# u = b * v

# Low spiking threshold
# a = 0.02
# b = 0.25
# c = -65
# d = 2
# v = -65
# u = b * v

# Thalamo cortical right
# a = 0.02
# b = 0.25
# c = -65
# d = 2
# v = -87
# u = b * v
# I = [-100 for i in range(0, 100)]
# I = I + [0 for i in range(0, 200)]

# Thalamo cortical left
# a = 0.02
# b = 0.25
# c = -65
# d = 2
# v = -63
# u = b * v
# I = [0 for i in range(0, 100)]
# I = I + [2 for i in range(0, 200)]

# # Resonator
# a = 0.1
# b = 0.26
# c = -60
# d = -1
# v = -60
# u = b * v
# I = [0 for i in range(0, 10)]
# I = I + [5 for i in range(0, 100)]
# I = I + [10 for i in range(0, 10)]
# I = I + [5 for i in range(0, 180)]

# I = [0 for i in range(0, 100)]
# I = I + [10 for i in range(0, 200)]
time = []
voltage = []
for t in range(0, 300):

    time.append(t)
    v = v+0.5*(0.04 * (v ** 2) + 5 * v + 140 - u + I[t])
    v = v+0.5*(0.04 * (v ** 2) + 5 * v + 140 - u + I[t])
    u = u + a * (b * v - u)

    if v >= 30e-3:
        print(v)
        print("Fired")
        v = c
        u = u + d
        voltage.append(30)
    else:
        voltage.append(v)

plt.plot(time, voltage)
plt.show()
