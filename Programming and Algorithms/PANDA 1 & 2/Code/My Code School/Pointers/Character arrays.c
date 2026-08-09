//
//  Character arrays.c
//  
//
//  Created by Oliver Goldstein on 08/11/2014.
//
//

#include <stdio.h>

int main()
{
    if you declare a string make sure that it is the size of the string +1 for the null Character;
    otherwise when printing the array it will continue to print consecutive memory.
    strlen works up to null Character
    double quotes imply \0
    -------------------------------
    Using pointers to arrays.
Declare:
    char c1[6] = "Hello";
    char *c2;
    so this makes a pointer of 1 byte.
    c2 = c1; Says the value of the pointer (value) at c2 is the value of c1. C1 just gives the pointer to the first character so c2 now has the value of the memory address of c1 beggining.
    We can use this c2 pointer to read and write into c1

    char c1[6] = "Hello";
    char *c2;
    print c2[1] (the same as *(c2+1)) gives the second letter in the array which is e
    c2[0] ='A'; (the same as *(c2+0) (c2)) means dereferencing the value of c2 which is the memory address of c1 so it changes c1
    c1[0] = 'H'; ????????????????????????????????????? how does this work? *(c1+0)
so:
    c1+0 gives the second address which is then dereferenced.
    c2+0 gives the second address which is then dereferenced.
    You cannot set c1 = c2 because you are saying 200=200;
    You cannot do c1+c1+1 as that is just wrong obviously.
        c2++; can be done as if it had an address of 200 then it would be 201 and would point to e now.
            
            
    -------------------    -------------------    -------------------    -------------------
            when passing a string to a function:
            char c[20] = "Hello";
    Functions do not copy strings as it is inefficient.
        so doing reorder(C) passes the pointer to the first character
        
        reorder(char *C)
    {
        C = memory value;
        *C = value;
        C[0] = *(C+0)
    }
    says that reorder can take a pointer to a character (in other words beggining of a string)
    
}