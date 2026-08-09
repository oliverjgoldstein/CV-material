function [S] = preprocess_weights(S)
for x=1:size(S,1)
    for y=1:size(S,1)
        prop = 0;
        if (x-y == 0)
            prop = 229.5;
        else
            prop = 27.5/(norm(x-y));
        end
        S(x,y) = prop - 12;
    end
end