//
//  Recursion versus While Loops.c
//  
//
//  Created by Oliver Goldstein on 16/10/2014.
//
//

#include <stdio.h>



unsigned greatest_common_divisor (unsigned a, unsigned b)
{
    while (a != b)
    {
        if (a > b)
        {
            a -= b;
        }
        else if (b > a)
        {
            b -= a;
        }
    }
}

unsigned greatest_common_divisor (unsigned a, unsigned b)
{
    if (a > b)
    {
        return greatest_common_divisor ( a-b, b );
    }
    else if (b > a)
    {
        return greatest_common_divisor ( a, b-a );
    }
    else // (a == b)
    {
        return a; 
    }
}