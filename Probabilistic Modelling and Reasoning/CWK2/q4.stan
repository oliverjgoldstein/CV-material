data {
    int N;
    int y[N];
    int x[N];
}
parameters {
    real alpha;
    real beta;
}

model {
    alpha ~ normal(0, 100);
    beta ~ normal(0, 100);
    for (n in 1:N){
      y[n] ~ poisson(exp(alpha * x[n] + beta));
    }
}
