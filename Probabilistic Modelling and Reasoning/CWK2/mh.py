import random
import scipy
import matplotlib.pyplot as plt
import numpy as np
import operator
from scipy import stats

def pdf(value, mean, variance):
    return scipy.stats.norm(mean, variance).pdf(value)

def mh(p_star, param_init, warmup, num_samples=5000, stepsize=1.0):
    points = [np.array(param_init)]
    variance = [stepsize for i in range(0, len(param_init))]

    for i in range(1, num_samples):
        x_l_minus_1 = points[i-1]
        x_cand = np.random.normal(x_l_minus_1, variance)
        a = p_star(x_cand)/p_star(x_l_minus_1)
        if a >= 1.0: # If the candidiate is a better guess then just add it
            points.append(x_cand)
        else: # Randomly sample to see if it at least has a decent prob dist.
            u = random.uniform(0.0,1.0)
            if u < a:
                points.append(x_cand)
            else: # Just probabilistically take the same point again.
                points.append(x_l_minus_1)

    return points[warmup:]

def partb_p_star(x_y):
    return pdf(x_y[0],0,1) * pdf(x_y[1],0,1)

def unzip(samples):
    xs = []
    ys = []
    for i in range(0, len(samples)):
        xs.append(samples[i][0])
        ys.append(samples[i][1])
    return (xs, ys)

def part_b():
    samples = mh(partb_p_star, [0.0,0.0], 0)
    (xs, ys) = unzip(samples)
    plt.title("Initialisation at [0,0]")
    plt.xlabel('X', fontsize=18)
    plt.ylabel('Y', fontsize=18)
    plt.scatter(xs[20:], ys[20:], c='b', marker='x', label='Other samples')
    plt.scatter(xs[:20], ys[:20], c='r', marker='x', label='First twenty samples')
    plt.legend()

    plt.show()

    samples = mh(partb_p_star, [7.0,7.0], 0)
    (xs, ys) = unzip(samples)
    plt.title("Initialisation at [7,7]")
    plt.xlabel('X', fontsize=18)
    plt.ylabel('Y', fontsize=18)
    plt.scatter(xs[20:], ys[20:], c='b', marker='x', label='Other samples')
    plt.scatter(xs[:20], ys[:20], c='r', marker='x', label='First twenty samples')
    plt.legend()

    plt.show()

def product(xs):
    return reduce(operator.mul, xs, 1)

def posterior_alpha_beta(alpha_beta):
    xs = [-5.051905265552104618e-01, -1.718571932218771470e-01, 1.614761401114561679e-01, 4.948094734447895382e-01, 8.150985069051909226e-01]
    ys = [1.000000000000000000e+00, 0.000000000000000000e+00, 2.000000000000000000e+00, 1.000000000000000000e+00, 2.000000000000000000e+00]

    alpha = alpha_beta[0]
    beta = alpha_beta[1]
    prior_term = scipy.stats.norm(0, 100).pdf(alpha) * scipy.stats.norm(0, 100).pdf(beta)
    mus = [np.exp(alpha*x+beta) for x in xs]
    likelihoods = scipy.stats.poisson.pmf(ys, mus)
    return product(likelihoods)

def part_c():
    samples = mh(posterior_alpha_beta, [0.0,0.0], 1000)
    (alphas, betas) = unzip(samples)
    plt.title("Initialisation at [0,0]")
    plt.xlabel('alpha', fontsize=18)
    plt.ylabel('beta', fontsize=18)
    plt.scatter(alphas, betas, c='b', marker='x', label='Samples')
    plt.legend()
    plt.show()
    print("Alpha mean: " + str(np.mean(alphas)))
    print("Beta mean: " + str(np.mean(betas)))
    print("Correlation: " + str(scipy.stats.pearsonr(alphas, betas)))

# part_b()
part_c()
