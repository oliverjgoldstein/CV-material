% This reads in the file, applies a fourier transform on the matrix, then
% applies the property of conjugate symmetry on it putting u = 0,
% v = 0 in the centre and then finally returns the magnitude spectrum.

function [feature] = f_transform(filename)

    file_matrix         = imread(filename);                     
    fourier_matrix      = fft2(double(file_matrix));            
    conjugate_matrix    = fftshift(fourier_matrix);             
    feature             = abs(log(abs(conjugate_matrix)+1));    
    
end