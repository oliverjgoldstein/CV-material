
data {
        int<lower=0> N; // Number of samples
	int<lower=0> D; // The original dimension
	int<lower=0> K; // The latent dimension
	matrix[N, D] X; // The data matrix
	real mu_W;      // Mean of prior of W
	real alpha0;    // hyperparam for gamma distr of alpha
	real beta0;     // hyperparam for gamma distr of alpha
}

parameters {
	matrix[N, K] Z; // The latent matrix
	matrix[D, K] W; // The weight matrix
	real<lower=0> tau; // Precision of X (noise term) 
	vector<lower=0>[K] alpha; // Precision of W
}

transformed parameters{
	vector<lower=0>[K] t_alpha;
	real<lower=0> t_tau;
	t_alpha = 1 ./ sqrt(alpha);
	t_tau = 1 ./ sqrt(tau);
}

model {
      tau ~ gamma(1, 1);
      alpha ~ gamma(alpha0, beta0);				
      for(k in 1:K) {
        W[,k] ~ normal(mu_W, t_alpha[k]);
      }	
      for(n in 1:N){
      	Z[n] ~ normal(0, 1);
	X[n] ~ normal(W * Z[n]', t_tau);
      }
} 

