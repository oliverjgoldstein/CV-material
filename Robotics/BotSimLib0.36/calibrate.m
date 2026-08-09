function [degree_angle] = calibrate(radian_angle)
degree_angle = radian_angle * (180 / pi);
degree_angle = degree_angle * (540/90);
end