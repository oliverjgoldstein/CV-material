//
//  Lab3Q3.c
//  
//
//  Created by Oliver Goldstein on 25/10/2014.
//
//

#include <stdio.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#define BITSOF(x) ( sizeof(x) * 8 )


int sign(int8_t x)
{
    if((x>>7)==-1)
    {
        printf("\n\nNegative\n");
    }
    else
    {
        printf("\n\nPositive\n");
    }
    return 0;
}




int negation(int8_t x)
{
    int i;
    int reversed_index = 0;
    int reversed[8];
    for(i=(BITSOF(x)-1);i>=0;i--)
    {
        reversed[reversed_index] = ((x >> i) & 1);
        printf("%d",((x >> i) & 1));
        reversed_index++;
    }
    i = 0;
    printf("\n-----Reversed------\n");
    for(i=0;i<reversed_index;i++)
    {
        if(reversed[i]==1)
        {
            reversed[i]=0;
        }
        else
        {
            reversed[i]=1;
        }
        printf("%d",reversed[i]);
    }
    printf("\n\n");
    return 0;
}



uint8_t mod(uint8_t x, int n)
{
    int i = 0;
    int two_power_of_n = 1;
    for(i=0;i<n;i++)
    {
        two_power_of_n = two_power_of_n << 1;
        printf("Multiplying by 2: %d\n",two_power_of_n);
    }
    int result = x&(two_power_of_n - 1);
    printf("Result of SO formula of x & (2^n -1) gives: %d\n\n",result);
    return result;
} // Used a formula online for this. http://stackoverflow.com/questions/3072665/bitwise-and-in-place-of-modulus-operator







//int int2seq(bool *X, int8_t x)
//{
//    // Extract and store each ith bit of x in the ith element of array X then return number of elements stored.
//    int i = 0;
//    int element_number = 7;
//    int general_counter = 0;
//
////    scanf("%d",&element_number); // CANNOT DO SCANF AS MEMORY CHANGES EACH TIME FUNCTION IS CALLED.
//    
//    int element_number_counter = 1;
//    bool values[10];
//    
//    for(i=(BITSOF(x)-1);i>=0;i--)
//    {
//        values[general_counter] = ((x>>i)&1);
//        
//        
//        if((element_number == (i+1)) || ( /*(element_number!=0) && */  ((i+1)%element_number)==0)     ) // e.g.
//            // Take 3rd element of array well that is 2 (3rd element) + 1 to adjust for user input
//            // Then we take any further multiples of that e.g. 6%3 = 0 therefore that as well.
//            // Then add it to the array in the 3,6 place respectively e.g.
//        {
//            X[element_number_counter*element_number] = values[i];
//            element_number_counter++;
//        }
//        
//        
//        general_counter++;
//    }
//    printf("%d",element_number_counter);
//    return element_number_counter;
//} REDO FUNCTION








int int2seq(bool *X, int8_t x)
{
    int i = 0;
    int ith_element = 2;
    bool values[8];
    int number_of_elements_stored = 0;
    int i_from_zero_counter = 0;
    
    
    
    
    for(i=(BITSOF(x)-1);i>=0;i--)
    {
        values[i_from_zero_counter] = ((x>>i)&1);
        i_from_zero_counter++;
    }
    i = 0; // Reset so now we can add the values
    for(i=ith_element;i<=(BITSOF(x)-1);i+=ith_element)
    {
        X[i] = values[i];
        if(X[i] == 0)
        {
            printf("\nA zero was added. \n");
        }
        else
        {
            printf("\nA one was added. \n");
        }
        number_of_elements_stored++;
    }
    printf("\n%d number of elements stored in total\n\n",number_of_elements_stored);
    return number_of_elements_stored;
}




int raise_number_to_power(int base, unsigned int exponent)
{
    if(exponent == 0)
    {
        return 1;
    }
    int loop_counter = 1;
    int original_base = base;
    while(loop_counter<exponent)
    {
        base = original_base*base;
        loop_counter++;
    }
    return base;
}




int8_t seq2int(bool *X, int n)
{
    printf("\n\n----------------------\n\n");
    unsigned int i = 0;
    unsigned int ith_element = 2;
    bool binary_values[10]; // Take every ith element out of n elements from X and put it into an array called binary values.
    unsigned int i_from_zero_counter = 0; // We then iterate through this and turn the 1s or 0s into an integer number.
    unsigned int binary_cursor = 0;
    int power;
    for(i=0;i<n;i+=ith_element)
    {
        binary_values[i_from_zero_counter] = X[i];  // Array counting needs to start from zero.
        i_from_zero_counter++;
        //printf("%d",i_from_zero_counter);
    }
    
    for(i=0;i<=i_from_zero_counter;i++)
    {
        printf("\nBinary Value: %d",binary_values[i]);
        if(binary_values[i] == 1)
        {
            
                power = raise_number_to_power(2,i);
                binary_cursor = binary_cursor + power;
                printf("\nAdded %d to make  %d   \n",power,binary_cursor);
            
//                printf("i: %d\n",i);
            
        }
        else
        {
            if(binary_values[i]==0)
            {
                //printf("ZERO: %d\n",binary_cursor);
            }
        }
        
    }
    printf("\n\n%d\n\n",binary_cursor);
    return binary_cursor;
}

void add(bool *R, bool *X, bool *Y, int n)
{
    
}


int main(void)
{
    sign(-10);
    negation(12);
    mod(12,3);
    bool Y[8];
    int2seq(Y,3);
    bool G[8] = {0,0,0,0,1,1,1,1}; // BIG ENDIAN.
    seq2int(G,8);
//  printf("%d", 12&16); // And between two numbers returns the point at which they are the same.
    return 0;
}