//
//  Arrays and pointers.c
//  
//
//  Created by Oliver Goldstein on 08/11/2014.
//
//

#include <stdio.h>
int main()
{
    int A[5];
    int x = 5;
    int *p;
    p = &x;
    print p; // gives the address of x
    print *p; // gives the value of the address of x
    p = p+1; // increments the memory of p by 4 bytes.
    // Address is &A[i] or (A+i)
    // Value is A[i] or *(A+i)
    ----------------- as function arguments... -----------------------
    sizeof can only be accessed by the stack frame of main when the array is declared so must be passed
    When you pass an array to a function it only passes the pointer to the start which would be 4 bytes .
    pointers can be 6 bytes long but int the pointer there will be a seuence of bits to declare the dereferencing
    amount!
    
    PASSING int A[] is the same as passing int *A;
    ONCE you are in the function however saying *A is now dereferencing rather than getting the address. i.e. it is different from the typecast. 
    
}