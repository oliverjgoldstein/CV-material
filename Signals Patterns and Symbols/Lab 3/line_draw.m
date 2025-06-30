function [] = line_draw()
[x y] = sample_generator(0.2, 0.5, 100);
Als = inv(x.'*x)*x.'*y; 
yIntercept = Als(1,1);
gradient = Als(2,1);
x(:,1) = [];
scatter(x, y);
x = [0 1];
y = [yIntercept (yIntercept + gradient*x(1, 2))];
line(x,y);
end

