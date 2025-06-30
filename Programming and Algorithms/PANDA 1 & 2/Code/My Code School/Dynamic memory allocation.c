//
//  Dynamic memory allocation.c
//  
//
//  Created by Oliver Goldstein on 08/11/2014.
//
//

#include <stdio.h>

void *malloc(size); // function returns a void pointer. Returns address of first byte of memory. Give me a block of memory with these many bytes. On the heap.
e.g. void *P = malloc(4) // Give us 4 bytes of memory. P will now be the address of the first *byte*
e.g. void *x = malloc(4*sizeof(int)); // sizeof is good practice as size of each data type always changes.
// VOID POINTERS CANNOT BE DEREFEReNCED but they are good as they GIVE US FLEXIBILITY E.G. ON THE EXAMPLE ABOVE AN INT TYPE WOULD NOT WORK AS NOT RIGHT SIZE FOR DEREFERENCING.
*p = 2 for example does not make sense
 The reason why a void pointer has to be used with malloc is because the memory that is allocated is dynamic and so no type can neceseserily fit the correct size.

To create a variable integer pointer to the heap:

-> int *p = (int*)malloc(3*sizeof(int)); -> when DEREFERENCING the pointer can be incremented in integer sizes.
p[0] = 2;
p[1] = 65; // This is incrementing the pointer.

p[2] = 200; // THis is a bit like an array.

Typecasting (int *)c;
malloc does not initialise the bytes when the memory is first allocated.
------------------------------------------------------------------------------
calloc

int *p = (int*)calloc(3,sizeof(int)); // 3 here is the number of elements with type int and the next parameter is the size of each element.

---------------------------------------------------------------------------------

realloc // used for changing the size of the memory block.

void *realloc(void* pointertostartofblock,sizeofnewblock); // if free memory is available next to the block it will be extended, else new memory will be created and copied over.

REALLOC IS THE ONLY WAY THAT MEMORY IS DYNAMIC.

A USE OF MALLOC FUNDAMENTALLY IS HERE:
you CANNOT DO THE FOLLOWING
int main()
{
    int n;
    print enter size
    scanf n
    int A[n];// CANNOT DO
    int *A = (int*)malloc(n*sizeof(int)); // can do THIS IS ALSO the few places where malloc is dynamic on input.
    int *A = (int*)calloc(n,sizeof(int)); // initialises to 0
    for(int i = 0;i<n;i++)
    {
        A[i] = i+1;
    }
    free(A); // data is not necesserily erased, depends on machine.
    VITAL INFO: After freeing A the data that a had is allocated to somewhere else on the operating system. However A[i] still returns the pointer value to that location. If you now modify the values at the address of A[i] it can create memory leaks or compile problems etc etc danger!! The values are still modifiable.
        The solution to this is to also give away the pointer memory by doing:
        A = NULL;
}


TO USE REALLOC TO ACTUALLY CHANGE SIZES OF MEMORY:


in the above example:
int *B = (int*)realloc(A,2*n*sizeof(int)); // either extends or new one and copies over.
int *B = (int*)realloc(A,(n/2)*sizeof(int)); // allocated elements are deallocated and assigned to garbage values.
int *A = (int*)realloc(A,0);// equivalent to free
int *B = (int*)realloc(NULL,n*sizeof(int)); // equivalent to calling malloc. Creates a new block as no pointers are given.



WE NEED TO KNOW SIZE OF AN ARRAY.