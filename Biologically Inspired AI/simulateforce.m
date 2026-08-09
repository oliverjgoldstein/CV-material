function simulateforce()
t=[0:1:100]
a=deg2rad(45)
v=10
g=9.81
x=v*cos(a)*t
y=v*sin(a)*t-1/2*g*t.^2
values = [x',y',t']
save('values.txt','values','-ascii')