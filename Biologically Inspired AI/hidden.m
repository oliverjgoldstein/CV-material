%feedforward neural network with a single hidden layer with 10 hidden neurons
[teachinginputs, teachingoutputs]= generatedata(1000)
net = feedforwardnet(2);
net=train(net,teachinginputs',teachingoutputs');
view(net);
y = net(teachinginputs');
plot(teachingoutputs',y,'.');