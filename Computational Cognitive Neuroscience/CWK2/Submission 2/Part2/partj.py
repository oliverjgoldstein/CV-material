import numpy as np
import csv
import math
import scipy
from scipy import optimize
from scipy import stats
import sklearn
from sklearn.metrics import confusion_matrix

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
    epsilon = logsig(theta_f[0])
    beta = theta_f[1]
    V = [0, 0]
    VAs = [V[0]]
    VBs = [V[1]]
    choices_f = []
    rewards_f = []
    trial_index = 0
    TI = [trial_index]
    for i in range(0, 250):
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
    epsilon = logsig(theta_f[0])
    rho = theta_f[1]
    V = [0, 0]
    VAs = [V[0]]
    VBs = [V[1]]
    choices_f = []
    rewards_f = []
    trial_index = 0
    TI = [trial_index]
    for i in range(0, 250):
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
    epsilon = logsig(theta_f[0])
    beta = theta_f[1]
    V = [theta_f[2], theta_f[2]]
    VAs = [V[0]]
    VBs = [V[1]]
    choices_f = []
    rewards_f = []
    trial_index = 0
    TI = [trial_index]
    for i in range(0, 250):
        trial_index = is_even(int(int(i)/int(25)))
        p_choose_A = (np.exp(beta * V[0]))/(np.exp(beta * V[0]) + np.exp(beta * V[1]))
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

with open('./ccn_assignment_2_data/choices.csv', 'rb') as csvfile:
    choices_r = list(csv.reader(csvfile))

with open('./ccn_assignment_2_data/rewards.csv', 'rb') as csvfile:
    rewards_r = list(csv.reader(csvfile))

for i in range(0, len(choices_r)):
    choices_r[i] = map(int, choices_r[i])

for i in range(0, len(rewards_r)):
    rewards_r[i] = map(int, rewards_r[i])

epsilons_m1 = []
betas_m1 = []
theta = [0,0]


for i in range(0, len(choices_r)):
    minimized_theta = scipy.optimize.fmin_bfgs(nll_model1, theta, args=(choices_r[i], rewards_r[i]), full_output=True, disp=False)
    fopt = minimized_theta[1]
    m_eps = minimized_theta[0][0]
    m_beta = minimized_theta[0][1]
    epsilons_m1.append(m_eps)
    betas_m1.append(m_beta)

m1_eps_mean = np.mean(epsilons_m1)
m1_eps_var = np.var(epsilons_m1)
m1_beta_mean = np.mean(betas_m1)
m1_beta_var = np.var(betas_m1)

total_eps_m1 = [np.random.normal(m1_eps_mean, m1_eps_var) for i in range(0, 100)]
total_beta_m1 = [np.random.normal(m1_beta_mean, m1_beta_var) for i in range(0, 100)]

# Everything for model 1 here:

rewards_new = []
choices_new = []
reward_probs = [[0.45, 0.7], [0.7, 0.3]]
for i in range(0, len(total_eps_m1)):
    theta = (total_eps_m1[i], total_beta_m1[i])
    (rewards, choices, VAs, VBs, TI) = model1(theta, reward_probs)
    rewards_new.append(rewards)
    choices_new.append(choices)

# Then do a fit again:!

theta1 = [0,0]
theta2 = [0,0]
theta3 = [0,0,0]
# Ask: which model most suits the data generated here?

m1_true = [0 for i in range(0, len(choices_new))]
m1_pred = []

for i in range(0, len(choices_new)):
    minimized_theta1 = scipy.optimize.fmin_bfgs(nll_model1, theta1, args=(choices_new[i], rewards_new[i]), full_output=True, disp=False)
    minimized_theta2 = scipy.optimize.fmin_bfgs(nll_model2, theta2, args=(choices_new[i], rewards_new[i]), full_output=True, disp=False)
    minimized_theta3 = scipy.optimize.fmin_bfgs(nll_model3, theta3, args=(choices_new[i], rewards_new[i]), full_output=True, disp=False)
    predicted = np.argmin([round(compute_AIC(minimized_theta1[1], 2), 2), round(compute_AIC(minimized_theta2[1], 2), 2), round(compute_AIC(minimized_theta3[1], 3), 2)])
    m1_pred.append(predicted)



















epsilons_m2 = []
rhos_m2 = []
theta = [0,0]


for i in range(0, len(choices_r)):
    minimized_theta = scipy.optimize.fmin_bfgs(nll_model2, theta, args=(choices_r[i], rewards_r[i]), full_output=True, disp=False)
    fopt = minimized_theta[1]
    m_eps = minimized_theta[0][0]
    m_rho = minimized_theta[0][1]
    epsilons_m2.append(m_eps)
    rhos_m2.append(m_rho)

m2_eps_mean = np.mean(epsilons_m2)
m2_eps_var = np.var(epsilons_m2)
m2_rho_mean = np.mean(rhos_m2)
m2_rho_var = np.var(rhos_m2)

total_eps_m2 = [np.random.normal(m2_eps_mean, m2_eps_var) for i in range(0, 100)]
total_rho_m2 = [np.random.normal(m2_rho_mean, m2_rho_var) for i in range(0, 100)]

# Everything for model 1 here:

rewards_new = []
choices_new = []
reward_probs = [[0.45, 0.7], [0.7, 0.3]]
for i in range(0, len(total_eps_m2)):
    theta = (total_eps_m2[i], total_rho_m2[i])
    (rewards, choices, VAs, VBs, TI) = model2(theta, reward_probs)
    rewards_new.append(rewards)
    choices_new.append(choices)

# Then do a fit again:!

theta1 = [0,0]
theta2 = [0,0]
theta3 = [0,0,0]
# Ask: which model most suits the data generated here?

m2_true = [1 for i in range(0, len(choices_new))]
m2_pred = []

for i in range(0, len(choices_new)):
    minimized_theta1 = scipy.optimize.fmin_bfgs(nll_model1, theta1, args=(choices_new[i], rewards_new[i]), full_output=True, disp=False)
    minimized_theta2 = scipy.optimize.fmin_bfgs(nll_model2, theta2, args=(choices_new[i], rewards_new[i]), full_output=True, disp=False)
    minimized_theta3 = scipy.optimize.fmin_bfgs(nll_model3, theta3, args=(choices_new[i], rewards_new[i]), full_output=True, disp=False)
    predicted = np.argmin([round(compute_AIC(minimized_theta1[1], 2), 2), round(compute_AIC(minimized_theta2[1], 2), 2), round(compute_AIC(minimized_theta3[1], 3), 2)])
    # print([compute_AIC(minimized_theta1[1], 2), compute_AIC(minimized_theta2[1], 2), compute_AIC(minimized_theta3[1], 3)])
    m2_pred.append(predicted)

# Everything for model 3 here:






















epsilons_m3 = []
betas_m3 = []
vs_m3 = []
theta = [0,0,0]


for i in range(0, len(choices_r)):
    minimized_theta = scipy.optimize.fmin_bfgs(nll_model3, theta, args=(choices_r[i], rewards_r[i]), full_output=True, disp=False)
    fopt = minimized_theta[1]
    epsilons_m3.append(minimized_theta[0][0])
    betas_m3.append(minimized_theta[0][1])
    vs_m3.append(minimized_theta[0][2])

m3_eps_mean = np.mean(epsilons_m3)
m3_eps_var = np.var(epsilons_m3)
m3_beta_mean = np.mean(betas_m3)
m3_beta_var = np.var(betas_m3)
m3_v_mean = np.mean(vs_m3)
m3_v_var = np.var(vs_m3)

total_eps_m3 = [np.random.normal(m3_eps_mean, m3_eps_var) for i in range(0, 100)]
total_beta_m3 = [np.random.normal(m3_beta_mean, m3_beta_var) for i in range(0, 100)]
total_vs_m3 = [np.random.normal(m3_v_mean, m3_v_var) for i in range(0, 100)]

# Everything for model 1 here:

rewards_new = []
choices_new = []
reward_probs = [[0.45, 0.7], [0.7, 0.3]]
print(theta)
for i in range(0, len(total_eps_m3)):
    theta = (total_eps_m3[i], total_beta_m3[i], total_vs_m3[i])
    (rewards, choices, VAs, VBs, TI) = model3(theta, reward_probs)
    rewards_new.append(rewards)
    choices_new.append(choices)

# Then do a fit again:!

theta1 = [0,0]
theta2 = [0,0]
theta3 = [0,0,0]
# Ask: which model most suits the data generated here?

m3_true = [2 for i in range(0, len(choices_new))]
m3_pred = []

for i in range(0, len(choices_new)):
    minimized_theta1 = scipy.optimize.fmin_bfgs(nll_model1, theta1, args=(choices_new[i], rewards_new[i]), full_output=True, disp=False)
    minimized_theta2 = scipy.optimize.fmin_bfgs(nll_model2, theta2, args=(choices_new[i], rewards_new[i]), full_output=True, disp=False)
    minimized_theta3 = scipy.optimize.fmin_bfgs(nll_model3, theta3, args=(choices_new[i], rewards_new[i]), full_output=True, disp=False)
    predicted = np.argmin([round(compute_AIC(minimized_theta1[1], 2),2), round(compute_AIC(minimized_theta2[1], 2),2), round(compute_AIC(minimized_theta3[1], 3),2)])
    # print([round(compute_AIC(minimized_theta1[1], 2), 2), round(compute_AIC(minimized_theta2[1], 2), round(compute_AIC(minimized_theta3[1], 3), 2)])
    # print(predicted)
    m3_pred.append(predicted)

print(m1_true)
print(m1_pred)
print(m2_true)
print(m2_pred)
print(m3_true)
print(m3_pred)
true = m1_true + m2_true + m3_true
pred = m1_pred + m2_pred + m3_pred

print(confusion_matrix(true, pred))
