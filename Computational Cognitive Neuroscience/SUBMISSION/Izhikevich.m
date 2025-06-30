% Created by Eugene M. Izhikevich, February 25, 2003
% Excitatory neurons Inhibitory neurons
Ne=800;
Ni=200;

w_e = 0.5; % 0.5, 0.4, 0.6, 0.55, 1.5
w_i = -1.0;
I_e = 5; %5 8
I_i = 2;

re=rand(Ne,1); ri=rand(Ni,1);
a=[0.02*ones(Ne,1); 0.02+0.08*ri];
b=[0.2*ones(Ne,1); 0.25-0.05*ri];
c=[-65+15*re.^2; -65*ones(Ni,1)];
d=[8-6*re.^2; 2*ones(Ni,1)];
S=[w_e*rand(Ne+Ni,Ne), w_i*rand(Ne+Ni,Ni)];
v=-65*ones(Ne+Ni,1); % Initial values of v
u=b.*v; % Initial values of u
firings=[]; % spike timings
S = preprocess_weights(S);
neuron_count = Ne + Ni;
time_steps = 1000;
raster_plot = zeros(neuron_count, time_steps);

for t=1:time_steps % simulation of 1000 ms
I=[I_e*randn(Ne,1);I_i*randn(Ni,1)]; % thalamic input
if t < 100
for j=1:100
I(j) = 5;
end
end
fired=find(v>=30); % indices of spikes
firings=[firings; t+0*fired,fired];
raster_plot(fired,t) = 1;
v(fired)=c(fired); % Reset the voltage of the neurons that fire to c the reset voltage.
u(fired)=u(fired)+d(fired); % U also changes
I=I+sum(S(:,fired),2);
v=v+0.5*(0.04*v.^2+5*v+140-u+I); % step 0.5 ms
v=v+0.5*(0.04*v.^2+5*v+140-u+I); % for numerical
u=u+a.*(b.*v-u); % stability
end;
plot(firings(:,1),firings(:,2));

time = zeros(1, time_steps);
for x=1:time_steps
time(1, x) = x;
end

spy(raster_plot);
