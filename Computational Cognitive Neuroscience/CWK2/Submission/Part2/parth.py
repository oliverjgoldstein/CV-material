import numpy as np
import csv
import math
import scipy
from scipy import optimize
from scipy import stats
import matplotlib.pyplot as plt

def compute_AIC(NLL, p):
    return 2.0*NLL + 2.0*p

def compute_BIC(NLL, p, n):
    return 2.0*NLL + p * math.log(n)

def logsig(x):
    return 1.0/(1.0 + np.exp(-x))

def is_even(num):
    if (num % 2) == 0:
        return 0
    else:
        return 1

def model1(theta_f, reward_probs):
    epsilon = theta_f[0]
    beta = theta_f[1]
    V = [0, 0]
    VAs = [V[0]]
    VBs = [V[1]]
    choices_f = []
    rewards_f = []
    trial_index = 0
    TI = [trial_index]
    for i in range(0, len(choices)):
        trial_index = is_even(int(int(i)/int(25)))
        p_choose_A = np.exp(beta * V[0])/(np.exp(beta * V[0]) + np.exp(beta * V[1]))
        chosenA = np.random.binomial(1, p_choose_A)
        reward = 0
        if chosenA == 1:
            # A has been chosen.
            reward = np.random.binomial(1, reward_probs[trial_index][0])
            V[0] = V[0] + epsilon * (reward - V[0])
            choices_f.append(0)
        else:
            # B has been chosen
            reward = np.random.binomial(1, reward_probs[trial_index][1])
            V[1] = V[1] + epsilon * (reward - V[1])
            choices_f.append(1)

        VAs.append(V[0])
        VBs.append(V[1])
        TI.append(trial_index)

        rewards_f.append(reward)
    return (rewards_f, choices_f, VAs, VBs, TI)

def model2(theta_f, reward_probs):
    epsilon = theta_f[0]
    rho = theta_f[1]
    V = [0, 0]
    VAs = [V[0]]
    VBs = [V[1]]
    choices_f = []
    rewards_f = []
    trial_index = 0
    TI = [trial_index]
    for i in range(0, len(choices)):
        trial_index = is_even(int(int(i)/int(25)))
        p_choose_A = np.exp(V[0])/(np.exp(V[0]) + np.exp(V[1]))
        chosenA = np.random.binomial(1, p_choose_A)
        reward = 0
        if chosenA == 1:
            # A has been chosen.
            reward = np.random.binomial(1, reward_probs[trial_index][0])
            V[0] = V[0] + epsilon * (rho * reward - V[0])
            choices_f.append(0)
        else:
            # B has been chosen
            reward = np.random.binomial(1, reward_probs[trial_index][1])
            V[1] = V[1] + epsilon * (rho * reward - V[1])
            choices_f.append(1)

        VAs.append(V[0])
        VBs.append(V[1])
        TI.append(trial_index)

        rewards_f.append(reward)
    return (rewards_f, choices_f, VAs, VBs, TI)

def model3(theta_f, reward_probs):
    epsilon = theta_f[0]
    beta = theta_f[1]
    V = [theta_f[2], theta_f[2]]
    VAs = [V[0]]
    VBs = [V[1]]
    choices_f = []
    rewards_f = []
    trial_index = 0
    TI = [trial_index]
    for i in range(0, len(choices)):
        trial_index = is_even(int(int(i)/int(25)))
        p_choose_A = np.exp(beta * V[0])/(np.exp(beta * V[0]) + np.exp(beta * V[1]))
        chosenA = np.random.binomial(1, p_choose_A)
        reward = 0
        if chosenA == 1:
            # A has been chosen.
            reward = np.random.binomial(1, reward_probs[trial_index][0])
            V[0] = V[0] + epsilon * (reward - V[0])
            choices_f.append(0)
        else:
            # B has been chosen
            reward = np.random.binomial(1, reward_probs[trial_index][1])
            V[1] = V[1] + epsilon * (reward - V[1])
            choices_f.append(1)

        VAs.append(V[0])
        VBs.append(V[1])
        TI.append(trial_index)

        rewards_f.append(reward)
    return (rewards_f, choices_f, VAs, VBs, TI)


def nll_model1(theta, choices_v, rewards_v):

    choices = choices_v
    rewards = rewards_v
    epsilon = logsig(theta[0])
    beta    = theta[1]
    V       = [0, 0]
    nll_sum = 0

    for i in range(0, len(choices)):
        p_choose_A = np.exp(beta * V[0])/(np.exp(beta * V[0]) + np.exp(beta * V[1]))
        p_choose_B = 1-p_choose_A
        probs = [p_choose_A, p_choose_B]
        if choices[i] == 1: # A one indexed array.
            V[0] = V[0] + epsilon * (rewards[i] - V[0])
        else:
            V[1] = V[1] + epsilon * (rewards[i] - V[1])

        nll_sum += -math.log(probs[choices[i] - 1])
    return nll_sum

def nll_model2(theta, choices_v, rewards_v):

    choices = choices_v
    rewards = rewards_v
    epsilon = logsig(theta[0])
    rho    = theta[1]
    V       = [0, 0]
    nll_sum = 0

    for i in range(0, len(choices)):
        p_choose_A = np.exp(V[0])/(np.exp(V[0]) + np.exp(V[1]))
        p_choose_B = 1-p_choose_A
        probs = [p_choose_A, p_choose_B]
        if choices[i] == 1: # A one indexed array.
            V[0] = V[0] + epsilon * (rho * rewards[i] - V[0])
        else:
            V[1] = V[1] + epsilon * (rho * rewards[i] - V[1])

        nll_sum += -math.log(probs[choices[i] - 1])
    return nll_sum

def nll_model3(theta, choices_v, rewards_v):

    epsilon = logsig(theta[0])
    beta    = theta[1]
    V       = [theta[2], theta[2]]
    nll_sum = 0

    for i in range(0, len(choices_v)):
        p_choose_A = (np.exp(beta * V[0]))/(np.exp(beta * V[0]) + np.exp(beta * V[1]))
        p_choose_B = 1 - p_choose_A

        # if p_choose_A == 0.0:
        #     # p_choose_A = 0.000001
        #     # p_choose_B = 1-p_choose_A
        #     if choices_v[i] == 1:
        #         return float("inf")
        #
        # if p_choose_B == 0.0:
        #     # p_choose_B = 0.000001
        #     # p_choose_A = 1-p_choose_B
        #     if choices_v[i] == 2:
        #         return float("inf")

        probs = [p_choose_A, p_choose_B]
        if choices_v[i] == 1: # A one indexed array.
            V[0] = V[0] + epsilon * (rewards_v[i] - V[0])
        else:
            V[1] = V[1] + epsilon * (rewards_v[i] - V[1])

        nll_sum += -math.log(probs[choices_v[i] - 1])
    return nll_sum

def plot(params, str_v):
    identity = [i for i in range(1, len(params)+1)]
    barlist = plt.bar(identity, params)
    MDD = (params[:23])
    CRL = (params[23:])
    plt.xlabel('Participant ID', fontsize=18)
    plt.ylabel(str_v + ' Value', fontsize=18)
    plt.axhline(np.mean(MDD), color='black', linewidth=2, xmin=0, xmax=0.48)
    plt.axhline(np.mean(CRL), color='black', linewidth=2, xmin=0.48, xmax=1)
    for i in range(0, 23):
        barlist[i].set_color('r')
    plt.show()
    return (MDD, CRL)


choices_r = []
rewards_r = []

with open('../ccn_assignment_2_data/choices.csv', 'rb') as csvfile:
    choices_r = list(csv.reader(csvfile))

with open('../ccn_assignment_2_data/rewards.csv', 'rb') as csvfile:
    rewards_r = list(csv.reader(csvfile))

for i in range(0, len(choices_r)):
    choices_r[i] = map(int, choices_r[i])

for i in range(0, len(rewards_r)):
    rewards_r[i] = map(int, rewards_r[i])

# epsilons_m1 = []
# betas_m1 = []
AICs = []
BICs = []
theta = [0,0]

# exit()
for i in range(0, len(choices_r)):
    minimized_theta = scipy.optimize.fmin_bfgs(nll_model1, theta, args=(choices_r[i], rewards_r[i]), full_output=True, disp=False)
    fopt = minimized_theta[1]
    AICs.append(compute_AIC(fopt, 2))
    BICs.append(compute_BIC(fopt, 2, 250))
    # m_eps = logsig(minimized_theta[0])
    # m_beta = minimized_theta[1]
    # epsilons_m1.append(m_eps)
    # betas_m1.append(m_beta)
# plot(epsilons_m1, 'epsilon')
# plot(betas_m1, 'beta')

print("AIC and BIC score for Model 1")
print(np.sum(AICs))
print(np.sum(BICs))

AICs = []
BICs = []


# epsilons_m2 = []
# rhos_m2 = []
theta = [0,0]
for i in range(0, len(choices_r)):
    minimized_theta = scipy.optimize.fmin_bfgs(nll_model2, theta, args=(choices_r[i], rewards_r[i]), full_output=True, disp=False)
    fopt = minimized_theta[1]
    AICs.append(compute_AIC(fopt, 2))
    BICs.append(compute_BIC(fopt, 2, 250))
    # m_eps = logsig(minimized_theta[0])
    # m_rho = minimized_theta[1]
    # epsilons_m2.append(m_eps)
    # rhos_m2.append(m_rho)
# plot(epsilons_m2, 'epsilon')
# plot(rhos_m2, 'rho')

print("AIC and BIC score for Model 2")
print(np.sum(AICs))
print(np.sum(BICs))


AICs = []
BICs = []



# epsilons_m3 = []
# betas_m3 = []
# v0s_m3 = []
theta = [0,0,0]
for i in range(0, len(choices_r)):
    minimized_theta = scipy.optimize.fmin_bfgs(nll_model3, theta, args=(choices_r[i], rewards_r[i]),  full_output=True, disp=False)
    fopt = minimized_theta[1]
    AICs.append(compute_AIC(fopt, 3))
    BICs.append(compute_BIC(fopt, 3, 250))
    # m_eps = logsig(minimized_theta[0])
    # m_beta = minimized_theta[1]
    # m_v0 = minimized_theta[2]
    # epsilons_m3.append(m_eps)
    # betas_m3.append(m_beta)
    # v0s_m3.append(m_v0)
# plot(epsilons_m3, 'epsilon')
# plot(betas_m3, 'beta')
# plot(v0s_m3, 'v0')

print("AIC and BIC score for Model 3")
print(np.sum(AICs))
print(np.sum(BICs))




#
