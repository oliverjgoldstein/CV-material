function [ ctree ] = grow_tree( character_s, character_v, character_t, len )
%GROW_TREE Summary of this function goes here
%   Detailed explanation goes here

class_array = []

for n = 1:len
    class_array = [class_array; 's']
end

for n = 1:len
    class_array = [class_array; 'v']
end

for n = 1:len
    class_array = [class_array; 't']
end

array = [character_s; character_v; character_t]

ctree = fitctree(array, class_array,'MinParentSize',10,'MinLeaf',5,'Prune','off')

end

