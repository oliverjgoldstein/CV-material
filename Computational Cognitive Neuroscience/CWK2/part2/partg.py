import numpy as np
import csv
import math
import scipy
from scipy import optimize
from scipy import stats
import matplotlib.pyplot as plt

def logsig(x):
    return 1.0/(1.0 + np.exp(-x))

def is_even(num):
    if (num % 2) == 0:
        return 0
    else:
        return 1

def model_simulate(theta_f, reward_probs):
    epsilon = theta_f[0]
    beta = theta_f[1]
    V = [0, 0]
    VAs = [V[0]]
    VBs = [V[1]]
    choices_f = []
    rewards_f = []
    trial_index = 0
    TI = [trial_index]
    for i in range(0, 50):
        trial_index = is_even(int(int(i)/int(25)))
        p_choose_B = np.exp(beta * V[1])/(np.exp(beta * V[1]) + np.exp(beta * V[0]))
        p_choose_A = np.exp(beta * V[0])/(np.exp(beta * V[0]) + np.exp(beta * V[1]))
        chosenA = np.random.binomial(1, p_choose_B)
        reward = 0
        if chosenA == 0:
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

def nll(theta, choices_v, rewards_v):

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

choices_r = []
rewards_r = []

with open('./ccn_assignment_2_data/choices.csv', 'rb') as csvfile:
    choices_r = list(csv.reader(csvfile))

with open('./ccn_assignment_2_data/rewards.csv', 'rb') as csvfile:
    rewards_r = list(csv.reader(csvfile))

for i in range(0, len(choices_r)):
    choices_r[i] = map(int, choices_r[i])

for i in range(0, len(rewards_r)):
    rewards_r[i] = map(int, rewards_r[i])


theta = [0,0]
epsilons = []
betas = []
for i in range(0, len(choices_r)):
    minimized_theta = scipy.optimize.fmin_bfgs(nll, theta, args=(choices_r[i], rewards_r[i]), disp=False)
    m_eps = logsig(minimized_theta[0])
    m_beta = minimized_theta[1]
    epsilons.append(m_eps)
    betas.append(m_beta)

beta_MDD = (betas[:23])
beta_CRL = (betas[23:])
eps_MDD = (epsilons[:23])
eps_CRL = (epsilons[23:])

eps_mean = np.mean(epsilons)
eps_var = np.var(epsilons)
beta_mean = np.mean(betas)
beta_var = np.var(betas)

# eps_mean_MDD = np.mean(eps_MDD)
# eps_var_MDD = np.var(eps_MDD)
# beta_mean_MDD = np.mean(beta_MDD)
# beta_var_MDD = np.var(beta_MDD)
#
# eps_mean_CRL = np.mean(eps_CRL)
# eps_var_CRL = np.var(eps_CRL)
# beta_mean_CRL = np.mean(beta_CRL)
# beta_var_CRL = np.var(beta_CRL)

# Here sampling occurs

total_eps = [np.random.normal(eps_mean, eps_var) for i in range(0, 10)]
total_beta = [np.random.normal(beta_mean, beta_var) for i in range(0, 10)]


identity = [i for i in range(1, len(total_eps)+1)]
barlist = plt.bar(identity, total_eps)
plt.xlabel('Participant', fontsize=18)
plt.ylabel('Epsilon Value', fontsize=18)
plt.axhline(np.mean(total_eps), color='black', linewidth=2, xmin=0, xmax=1.0)
# plt.show()

identity = [i for i in range(1, len(total_beta)+1)]
barlist = plt.bar(identity, total_beta)
plt.xlabel('Participant ID', fontsize=18)
plt.ylabel('Beta Value', fontsize=18)
plt.axhline(np.mean(total_beta), color='black', linewidth=2, xmin=0, xmax=1.0)
# plt.show()

# Remember to redo the figures on the actual data!

rewards_new = []
choices_new = []
reward_probs = [[0.45, 0.7], [0.7, 0.3]]
for i in range(0, len(total_eps)):
    theta = (total_eps[i], total_beta[i])
    (rewards, choices, VAs, VBs, TI) = model_simulate(theta, reward_probs)
    rewards_new.append(rewards)
    choices_new.append(choices)

predicted_eps = []
predicted_beta = []
init_theta = [0,0]
for i in range(0, len(total_eps)):
    minimized_theta = scipy.optimize.fmin_bfgs(nll, init_theta, args=(choices_new[i], rewards_new[i]), disp=False)
    m_eps = logsig(minimized_theta[0])
    m_beta = minimized_theta[1]
    predicted_eps.append(m_eps)
    predicted_beta.append(m_beta)

# total_eps vs predicted_eps
# total_beta vs predicted_beta

plt.scatter(total_beta, predicted_beta, c = ['k'], marker = 'x')
plt.xlabel('Original Beta', fontsize=18)
plt.ylabel('Recovered Beta', fontsize=18)
# plt.show()
plt.xlabel('Original Epsilon', fontsize=18)
plt.ylabel('Recovered Epsilon', fontsize=18)
plt.legend()
plt.scatter(total_eps, predicted_eps, c = ['c'], marker = 'x')
# plt.show()
crl1 = scipy.stats.pearsonr(total_eps, predicted_eps)
crl2 = scipy.stats.pearsonr(total_beta, predicted_beta)
crl3 = scipy.stats.spearmanr(total_eps, predicted_eps)
crl4 = scipy.stats.spearmanr(total_beta, predicted_beta)

print(crl1)
print(crl2)
print(crl3)
print(crl4)


# (0.25932779299532827, 0.06897317832542599)
# (0.8969883876913501, 1.221815864862783e-18)
# SpearmanrResult(correlation=0.22016806722689078, pvalue=0.12444826771332099)
# SpearmanrResult(correlation=0.8956062424969988, pvalue=1.6556408924552444e-18)
#
#
#




#
