import numpy as np
import matplotlib.pyplot as plt
import math

p1s = np.array([0.01, 0.01, 0.08, 0.2, 0.7], dtype='f')
s1s = np.array([1, 2, 3, 4, 5], dtype='f')
p2s = np.array([0.02, 0.02, 0.06, 0.3, 0.6], dtype='f')
s2s = np.array([1, 2, 3, 4, 5], dtype='f')
p3s = np.array([0.2, 0.2, 0.2, 0.2, 0.2], dtype='f')
s3s = np.array([1, 2, 3, 4, 5], dtype='f')
tanh_f = np.vectorize(math.tanh)

def compute_Prbw_r(sb):
    diff = sb - s1s
    return (1.0/2.0) * (1 + tanh_f(diff))

def compute_Prbw(sw):
    diff = s1s - sw
    return (1.0/2.0) * (1 + tanh_f(diff))

def inv_compute_Prbw(sw):
    diff = s2s - sw
    return 1.0 - ((1.0/2.0) * (1 + tanh_f(diff)))

def phi_1_tilde_s1(s2, s3):
    s2_v = np.array([s2 for i in range(0, len(p1s))])
    s3_v = np.array([s3 for i in range(0, len(p1s))])
    return np.sum(np.multiply(np.multiply(p1s, compute_Prbw(s2_v)), compute_Prbw_r(s3_v)))

new_func = np.vectorize(phi_1_tilde_s1)

def phi_2_tilde_s2(s3):
    s3_v = np.array([s3 for i in range(0, len(p2s))])
    rv = new_func(s2s, s3_v)
    return np.sum(np.multiply(np.multiply(p2s, inv_compute_Prbw(s3_v)), rv))

v_phi_2_tilde_s2 = np.vectorize(phi_2_tilde_s2)

def posterior():
    rv = np.multiply(p3s, v_phi_2_tilde_s2(s3s))
    sum_rv = np.sum(rv)
    rv = rv/sum_rv
    return rv

def partf():
    f1, f2 = (p3s, v_phi_2_tilde_s2(s3s))
    f1 = f1/max(f1)
    f2 = f2/max(f2)
    return (f1,f2)

def parth():
    p1, p2 = (p3s, np.array(posterior()))
    return (p1, p2)

(prior_h, posterior_h) = parth()
posterior_g = [0.03156459, 0.08779157, 0.17200927, 0.28421772, 0.42441687]
print(posterior_h)

# identity1 = [0,2,4,6,8]
# identity2 = [1,3,5,7,9]
# plt.bar(identity1, posterior_h, label = "Posterior")
# plt.bar(identity2, prior_h, label = "Prior")
# plt.title("Prior vs Posterior")
# plt.ylim(top=1)
# plt.ylim(bottom=0)
# plt.xlabel('Skill', fontsize=18)
# plt.ylabel('Probability', fontsize=18)
# plt.legend()
# plt.show()

diff = np.array(posterior_g) - np.array(posterior_h)
print(diff)
identity1 = [0,4,8,12,16]
identity2 = [1,5,9,13,17]
identity3 = [2,6,10,14,18]
plt.bar(identity1, posterior_h, label = "Posterior H")
plt.bar(identity2, posterior_g, label = "Posterior G")
plt.bar(identity3, diff, label = "Difference in posterior")
plt.title("Posterior H vs Posterior G with differences")
plt.ylim(top=1)
plt.ylim(bottom=-0.5)
plt.xlabel('Skill', fontsize=18)
plt.ylabel('Probability', fontsize=18)
plt.legend()
plt.show()
