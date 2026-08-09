function [x,y] = sample_generator(c, m, n)
xArray = rand(n, 1);
eArray = randn(n, 1)/10;
oneMatrix = ones(n, 1);
x = horzcat(oneMatrix, xArray);
y = c + m*xArray + eArray;
end