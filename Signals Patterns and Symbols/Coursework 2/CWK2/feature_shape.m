% This 

function [ fourier_feature_one, fourier_feature_two ] = feature_shape( name_of_file )
    
    % We first get the fourier transform of the file.

    ft = f_transform(name_of_file);
    
    % This returns a row vector with the sum over each column.
    % First it will return a row vector with the sum over each column.
    % Then it will act along the first non singleton dimension returning
    % a number. As it is summed twice it will return a single number.
    fourier_feature_one = sum(sum(ft(270:320, 480:520)));
    fourier_feature_two = sum(sum(ft(240:300, 310:330)));
    
    %Display feature selection:
    h1 = figure;
    imagesc(ft);
    rectangle('Position',[480, 270, 50, 50],'LineWidth',1,'EdgeColor','w');
    rectangle('Position',[310, 240, 20, 60],'LineWidth',1,'EdgeColor','w');
end