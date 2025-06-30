import pystan

xs = [-5.051905265552104618e-01, -1.718571932218771470e-01, 1.614761401114561679e-01, 4.948094734447895382e-01, 8.150985069051909226e-01]
ys = [1.000000000000000000e+00, 0.000000000000000000e+00, 2.000000000000000000e+00, 1.000000000000000000e+00, 2.000000000000000000e+00]

sm = pystan.StanModel(file='q4.stan')

for i in range(0, 1):
    results = sm.sampling(data={"N": len(xs), "y": ys, "x": xs}, iter=10000, chains = 1)
    alphas = results.extract()["alpha"]
    betas = results.extract()["beta"]
    print(len(alphas))
