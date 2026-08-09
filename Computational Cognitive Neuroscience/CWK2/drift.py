import numpy as np
import matplotlib.pyplot as plt
from matplotlib import pyplot

v = [0.19, 0.9] # Increasing V decreases reaction times
a = 0.1 # Decreasing a decreases reaction times
z = a/2 # Increasing z decreases reaction times
s = 1.85 # This changes the noise and will reduce accuracy
dt = 0.001 # This is just the time constant
multiple_simulations = []
multiple_reactions_plus = []
multiple_reactions_minus = []


reaction_times_hplus = []
reaction_times_hminus = []
finished = False
cols = ['b','r']
for val in range(0,2):
    reaction_times_hplus = []
    reaction_times_hminus = []
    # We care about accuracy and reaction times.
    for i in range(0, 500):
        time = 0
        times = [time]
        Wt = z
        Wv = [Wt]
        finished = False
        while(finished != True):
            Wt = Wt + v[val] * dt + s * np.random.normal(0, dt)
            time += dt

            times.append(time)
            Wv.append(Wt)

            if Wt > a:
                reaction_times_hplus.append(time)
                finished = True
                multiple_simulations.append((times, Wv, cols[val]))
                # print("H+!")
                break
            if Wt < 0:
                reaction_times_hminus.append(time)
                finished = True
                multiple_simulations.append((times, Wv, cols[val]))
                # print("H-!")
                break
    multiple_reactions_plus.append(reaction_times_hplus)
    multiple_reactions_minus.append(reaction_times_hminus)
    accuracy = float(len(reaction_times_hplus))/float((len(reaction_times_hplus)+len(reaction_times_hminus)))
    print("Accuracy: " + str(accuracy))

cols = ['b','r','g','y']

bins = np.linspace(0.00, 0.6, 100)
for i in range(0, len(multiple_reactions_plus)):
    pyplot.hist(multiple_reactions_plus[i], bins, alpha=0.5, label='H+', color=cols[i*2])
    pyplot.hist(multiple_reactions_minus[i], bins, alpha=0.5, label='H-', color=cols[(i*2)+1])

pyplot.legend(loc='upper right')
plt.show()


for i in range(0, len(multiple_simulations)):
    plt.plot(multiple_simulations[i][0], multiple_simulations[i][1], multiple_simulations[i][2])
plt.show()
