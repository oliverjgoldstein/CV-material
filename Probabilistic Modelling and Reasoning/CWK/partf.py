import numpy as np

p1s = np.array([0.01, 0.01, 0.08, 0.2, 0.7], dtype='f')
s1s = np.array([1, 2, 3, 4, 5], dtype='f')
p2s = np.array([0.02, 0.02, 0.06, 0.3, 0.6], dtype='f')
s2s = np.array([1, 2, 3, 4, 5], dtype='f')
p3s = np.array([0.2, 0.2, 0.2, 0.2, 0.2], dtype='f')
s3s = np.array([1, 2, 3, 4, 5], dtype='f')


def compute_Prbw_r(sb):
    diff = sb - s1s
    return (1.0/10.0) * diff + 0.5

def compute_Prbw(sw):
    diff = s1s - sw
    return (1.0/10.0) * diff + 0.5

def inv_compute_Prbw(sw):
    diff = s2s - sw
    return 1.0 - ((1.0/10.0) * diff + 0.5)

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
    print(np.sum(rv))
    return rv

def partf():
    f1, f2 = (p3s, v_phi_2_tilde_s2(s3s))
    f1 = f1/max(f1)
    f2 = f2/max(f2)
    return (f1,f2)

print(partf())
print(posterior())
