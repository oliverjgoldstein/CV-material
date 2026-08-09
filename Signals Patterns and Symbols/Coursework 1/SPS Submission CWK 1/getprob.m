% Compute probability of (x,y) for a given mu and sigma
function P = getprob(x, y, mu, sigma)
    vec = [x y]';
    P = (vec-mu)' * inv(sigma) * (vec-mu);
end
