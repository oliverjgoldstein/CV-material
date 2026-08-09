import numpy as np
import random
import matplotlib.pyplot as plt
import math

def is_even(num):
    if (num % 2) == 0:
        return 0
    else:
        return 1

def model_simulate(theta, reward_probs):
    epsilon = theta[0]
    beta = theta[1]
    V = [0, 0]
    VAs = [V[0]]
    VBs = [V[1]]
    choices = []
    rewards = []
    trial_index = 0
    TI = [trial_index]
    for i in range(0, 250):
        trial_index = is_even(int(int(i)/int(25)))
        p_choose_B = np.exp(beta * V[1])/(np.exp(beta * V[1]) + np.exp(beta * V[0]))
        p_choose_A = np.exp(beta * V[0])/(np.exp(beta * V[0]) + np.exp(beta * V[1]))
        print(p_choose_B)
        print(p_choose_A)
        chosenA = np.random.binomial(1, p_choose_B)
        reward = 0
        if chosenA == 0:
            # A has been chosen.
            reward = np.random.binomial(1, reward_probs[trial_index][0])
            V[0] = V[0] + epsilon * (reward - V[0])
            choices.append(0)
        else:
            # B has been chosen
            reward = np.random.binomial(1, reward_probs[trial_index][1])
            V[1] = V[1] + epsilon * (reward - V[1])
            choices.append(1)

        VAs.append(V[0])
        VBs.append(V[1])
        TI.append(trial_index)

        rewards.append(reward)
    return (rewards, choices, VAs, VBs, TI)

reward_probs = [[0.45, 0.7], [0.7, 0.3]]
theta = [0.35, 5.5]
# beta_vs = np.linspace(0, 20, 51)
# epsilon_vs = np.linspace(0, 1, 51)
# rewards_array = [[0 for i in range(0, len(epsilon_vs))] for i in range(0, len(beta_vs))]
# for b in range(0, len(beta_vs)):
#     for e in range(0, len(epsilon_vs)):
rewards = []
for k in range(0, 1):
    (reward_vs, choices, VAs, VBs, TI) = model_simulate(theta, reward_probs)
    diff = np.array(VAs) - np.array(VBs)
    identityA = [i for i in range(0, len(VAs))]
    identityB = [i for i in range(0, len(VBs))]
    plt.plot(identityA, VAs ,'r', label = 'V(A)')
    plt.plot(identityB, VBs, 'c', label = 'V(B)')
    plt.plot(identityB, TI, 'tab:gray', label = 'Switch')
    plt.ylim(top=1)
    plt.ylim(bottom=0)
    plt.xlabel('Iterations', fontsize=18)
    plt.ylabel('Value', fontsize=18)
    plt.legend()
    plt.show()
    plt.plot(identityB, diff, 'm', label='Difference')

    for ti in range(0, len(TI)):
        if TI[ti] == 0:
            TI[ti] = -1
    plt.plot(identityB, TI, 'tab:gray', label = 'Switch')
    plt.xlabel('Iterations', fontsize=18)
    plt.ylabel('Value', fontsize=18)
    plt.ylim(top=1)
    plt.ylim(bottom=-1)
    plt.legend()
    plt.show()
    r = 0
    for i in range(0, len(reward_vs)):
        if reward_vs[i] == 1:
            r += 1
    rewards.append(r)
# rewards_array[b][e] = np.mean(rewards)

# plt.xlabel('Epsilon', fontsize=18)
# plt.ylabel('Beta', fontsize=18)
# # plt.legend()
# # plt.rcParams["figure.figsize"] = [16,9]
# plt.title('Epsilon vs Beta for the reward count')
plt.imshow(rewards_array, cmap='hot', interpolation='nearest', extent=(0, 1, 0, 1))
# # plt.gca().set_aspect('equal', adjustable='box')
# # plt.axis('equal')
# plt.colorbar()
# plt.show()
# # print()

# What is observed
# Expected rewards for random choices
# Generate the rewards with probabilities
