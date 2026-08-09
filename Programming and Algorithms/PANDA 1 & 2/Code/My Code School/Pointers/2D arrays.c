//
//  2D arrays.c
//  
//
//  Created by Oliver Goldstein on 08/11/2014.
//
//

#include <stdio.h>

int main()
{
    effectively int *p = A;
    is the same as P=A;
    int A[4];
    A[0] is an integer.
    ------------------------
    int B[2][3]; // 2times 1D arrays with 3 int within them.
Here:
    B[0] is a 1D array of 3 integers.
    Doing int *p = B will give a compilation error as p is an integer pointer of 4 bytes not of 3*4 bytes.
    int (*P)[3] = B; // This makes an integer pointer big enough to hold the array of 3 integers.
    print B or &B[0] -> will give us the  address of the first element in the 1d array of 3 integers.
    print *B or B[0] will give us the dereference of the 2D array which will give us the first 1D array. What is the first 1D array? It is a pointer to the first variable o again will give us the address of the first element in the 1d array of 3 integers.
    
    
    
    
Here:
    printing B+1 or &B[1] gives the address of the beggining of the second 1D array. which is also the first element of the array
    print *(B+1) or B[1] will give you the dereference of the second element in the set of 3 integer arrays well that is just another 1d array with 3 elements.  if you then get this array back the array in turn is just a pointer to its first element so you get the address of the beggining of the 4th integer in.
        THis can also be expressed as &B[1][0]
        *(B+1)+2 is therefore the address of the second arrays first element + 8 bytes (for int).
        *(*B+1) will give the dereference of the first element of B which is the first 1d 3 int array +1 will give the second element in the first array dereferenced to give its value.
        
        For any 2d array:
        B[i][j] can be rewritten as *(B[i]+j);
}