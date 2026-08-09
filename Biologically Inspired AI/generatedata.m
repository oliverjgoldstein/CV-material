function [teachinginputs, teachingoutputs] = generatedata(n)

teachinginputs = []
for i = 1:n
    x1=rand;
    x2=rand;
    teachinginputs=[teachinginputs; -1 x1 x2];
end

teachingoutputs = [teachinginputs(:,2).*sin(teachinginputs(:,3))];