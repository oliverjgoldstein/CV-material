import numpy as np
import csv
import math
import scipy
from scipy import optimize
from scipy import stats
import matplotlib.pyplot as plt

def logsig(x):
    return 1.0/(1.0 + np.exp(-x))

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

nll_patient_2 = nll([0.4, 5.0], choices_r[1], rewards_r[1])
print(nll_patient_2)
print(len(rewards_r))
min_theta_array = [[]]
# theta = [[0, 0], [0.5, 5], [1, 10]]
theta = [0,0]
epsilons = []
betas = []
# for t in range(0, len(theta)):
for i in range(0, len(choices_r)):
    minimized_theta = scipy.optimize.fmin_bfgs(nll, theta, args=(choices_r[i], rewards_r[i]), disp=False)
    min_theta_array[0].append((logsig(minimized_theta[0]), minimized_theta[1]))
    m_eps = logsig(minimized_theta[0])
    m_beta = minimized_theta[1]
    # print("Patient " + str(i) + " has eps: " + str(m_eps) + " and beta: " + str(m_beta))
    epsilons.append(m_eps)
    betas.append(m_beta)

# for t in range(0, len(choices_r)):
#     diff_eps1 = min_theta_array[0][t][0] - min_theta_array[1][t][0]
#     diff_beta1 = min_theta_array[0][t][1] - min_theta_array[1][t][1]
#     diff_eps2 = min_theta_array[0][t][0] - min_theta_array[2][t][0]
#     diff_beta2 = min_theta_array[0][t][1] - min_theta_array[2][t][1]
#
#     print("Difference between theta1 and theta2: eps: " + str(diff_eps1) + " beta: " + str(diff_beta1))
#     print("Difference between theta1 and theta3: eps: " + str(diff_eps2) + " beta: " + str(diff_beta2))

# identity = [i for i in range(1, len(betas)+1)]
# barlist = plt.bar(identity, betas)
beta_MDD = (betas[:23])
beta_CRL = (betas[23:])
# plt.xlabel('Participant ID', fontsize=18)
# plt.ylabel('Beta Value', fontsize=18)
# plt.axhline(beta_MDD, color='black', linewidth=2, xmin=0, xmax=0.48)
# plt.axhline(beta_CRL, color='black', linewidth=2, xmin=0.48, xmax=1)
# for i in range(0, 23):
#     barlist[i].set_color('r')
# plt.show()


# barlist = plt.bar(identity, epsilons)
eps_MDD = (epsilons[:23])
eps_CRL = (epsilons[23:])
# plt.xlabel('Participant ID', fontsize=18)
# plt.ylabel('Epsilon Value', fontsize=18)
# plt.axhline(eps_MDD, color='black', linewidth=2, xmin=0, xmax=0.48)
# plt.axhline(eps_CRL, color='black', linewidth=2, xmin=0.48, xmax=1)
# for i in range(0, 23):
#     barlist[i].set_color('r')
# plt.show()
# plt.show()

data_MDD = zip(beta_MDD, eps_MDD)
data_CRL = zip(beta_CRL, eps_CRL)

# print(scipy.stats.normaltest(betas))
# print(scipy.stats.normaltest(epsilons))
# print(scipy.stats.normaltest(eps_CRL))
# print(scipy.stats.normaltest(eps_MDD))

between_grp_correlation_p = scipy.stats.pearsonr(epsilons, betas)
within_grp_correlation_pD = scipy.stats.pearsonr(beta_MDD, eps_MDD)
within_grp_correlation_pC = scipy.stats.pearsonr(beta_CRL, eps_CRL)
between_grp_correlation_s = scipy.stats.spearmanr(epsilons, betas)
within_grp_correlation_sD = scipy.stats.spearmanr(beta_MDD, eps_MDD)
within_grp_correlation_sC = scipy.stats.spearmanr(beta_CRL, eps_CRL)

# print(within_grp_correlation_pC)
# print(within_grp_correlation_sC)

# print(eps_MDD)
# print(eps_CRL)

# print(beta_MDD)
# print(beta_CRL)

t_test_scores_eps = scipy.stats.ttest_ind(eps_MDD, eps_CRL, equal_var = False)
t_test_scores_beta = scipy.stats.ttest_ind(beta_MDD, beta_CRL, equal_var = False)

# print(t_test_scores_eps)
# print(t_test_scores_beta)

eps_mean = np.mean(epsilons)
eps_var = np.var(epsilons)
beta_mean = np.mean(betas)
beta_var = np.var(betas)

eps_mean_MDD = np.mean(eps_MDD)
eps_var_MDD = np.var(eps_MDD)
beta_mean_MDD = np.mean(beta_MDD)
beta_var_MDD = np.var(beta_MDD)

print(eps_mean_MDD)
print(eps_var_MDD)
print(beta_mean_MDD)
print(beta_var_MDD)

eps_mean_CRL = np.mean(eps_CRL)
eps_var_CRL = np.var(eps_CRL)
beta_mean_CRL = np.mean(beta_CRL)
beta_var_CRL = np.var(beta_CRL)


# Here sampling occurs

eps_estimate = np.random.normal(eps_mean, eps_var)
beta_estimate = np.random.normal(beta_mean, beta_var)
















#
