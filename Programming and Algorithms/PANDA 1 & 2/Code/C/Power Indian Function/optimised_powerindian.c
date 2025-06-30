//
//  optimised_powerindian.c
//
//  Created by Oliver Goldstein on 27/12/2014.
//

#include <stdio.h> // Standard buffered input/output.
#include <time.h> // Used to measure the time taken for each function being used.
// <stdio.h> is included to use the printf function. It is an inefficient implementation as only a small amount of the library is being used.
// I therefore researched hte use of pre compiled headers to reduce wasted energy but was unable to implement them.

// All function declarations are before the main part of the program.
float raise_number_to_integer_exponent(float base, int exponent);
float raise_number_to_integer_exponent(float base, int exponent);
double powerloop(double base, long unsigned int exponent);
double powerindian(double base,long unsigned int exponent);

// powerloop as a function implements the power indian algorithm using a while loop.
// raise_number_to_power as a function simply takes a base and raises it to an integer exponent.
// powerindian as a function uses the powerindian algorithm to find powers.
// raise_number


int main() // No command line arguments.
{
    clock_t t; // Used to calculate time taken for the functions.
    
    t = clock();

    float negative_power_result = (10000*(raise_number_to_integer_exponent(1.04,-12))); // Variables and functions renamed to emphasise clarity.

    t = clock() - t; // Measures difference in time.
    double negative_time_taken = ((double)t)/CLOCKS_PER_SEC;
    t = clock(); // Clock reset.

    float interest_power = (raise_number_to_integer_exponent(1.05,25)*1000);

    t = clock() - t; // Measures difference in time.
    double interest_time_taken = ((double)t)/CLOCKS_PER_SEC;
    t = clock(); // Clock reset.

    double indian_implementation = powerindian(1.0000000001,1000000000);

    t = clock() - t; // Measures difference in time.
    double indian_time_taken = ((double)t)/CLOCKS_PER_SEC;
    t = clock(); // Clock reset.

    double while_indian_implementaion = powerloop(1.0000000001,2000000000);

    t = clock() - t; // Measures difference in time.
    double while_time_taken = ((double)t)/CLOCKS_PER_SEC;

    
    printf("\n Negative Power: %f and time taken: %f\n Interest: %f and time taken: %f\n Recursive Power Indian: %f and time taken: %f\n Iterative Power Indian:%f and time taken: %f\n\n", negative_power_result,negative_time_taken, interest_power, interest_time_taken ,indian_implementation, indian_time_taken, while_indian_implementaion, while_time_taken);
    
    
    // Double uses %f as C will promote floats to doubles for functions that take variable arguments.
    // Renaming of variable names and printf reduced to a single line function.
    
    return 0;
}

// Binary search of phone book works in O(nlogn)

float raise_number_to_integer_exponent(float base, int exponent) // Loop counter optimised to take into account the zero case.
{
    // Given base is a real number and an the exponent is a member of the Z set. (Integers).
    register int loop_counter = 0;          // This integer initialises the number of times that the program should multiply the base by. Register is used, as it is processed faster by the CPU.
    int negative_flag = 0;                  // This integer keeps track of whether the original exponent is negative or not so the final result can be correctly displayed.
    const float original_base = base;       // This integer keeps track of the original base, so that each time the base is multiplied the original is not lost. Const was added to stop programming mistakes.
    base = 1;                               // Because any number raised to the power of zero is 1, before starting the base is set to 0. The original is safely stored in original_base.
    
    // Given the negative_flag = 0 and the original base = base and loop counter = 0 and base = 1, a real number and the exponent is a member of the Integers.
    
    if(exponent<0)                          // This if statement here checks to see whether the exponent is less than 0.
    {
        exponent = -(exponent);             // The exponent has its sign changed.
        negative_flag = 1;                  // The negative_flag is initialised to zero, so the program can keep track of what to do at the end to the result.
    }                                       // The exponent is positive so that now the main loop can work.
    else
    {
        negative_flag = 0;                  // The exponent is positive.
    }
    
    for(loop_counter=0;loop_counter < exponent;loop_counter++)          // The loop_counter is always <= as the exponent has to be >= to zero.
    {
        base = original_base*base;          // The base is multiplied by its original self.
        loop_counter++;                     // The loop counter is increased.
    }
    
    if(negative_flag == 1)                  // If the negative flag is 1 then at the end of the program, the reciprocal of the result is taken and returned.
    {
        base = 1/base;
    }
    
    return base;
    
}


double powerindian(double base, long unsigned int exponent)
{

    /*
     Specification of the recursive powerindian algorithm:
            Useful compared to a naive implementation. Can work in nlog(n) time.
            Useful in modular exponentiation for example when implementing a RSA cryptographic system or when primality testing.
     
                        If the exponent is zero, return 1.
                        If the exponent is even, base^exponent  is  the square of (base ^ (exponent/2))
                        If the exponent is odd, base^exponent  equals  base * (base ^ (exponent-1))
     
    */
    
    if(exponent == 0)                                   // If power is zero, answer is 1.
    {
        return 1;
    }
    
    else if((exponent&1) == 0)                          // Modulo changed to bit checker. Increases speed.
    {
        double a = powerindian(base,exponent>>1);       // Dividing by 2 changed to bitwise shift.
        return a*a;                                     // Final part of the recursive call ends up where originally called.
    }
    
    else if(((exponent-1)&1) == 0)                      // Modulo changed to bit checker.
    {
        double b = powerindian(base,(exponent-1));      // Recursively call powerindian with the same base and the exponent - 1
        return base * b;                                // Final part of the recursive call ends up where originally called.
    }
    return 0;
}


double powerloop(double base, long unsigned int exponent)       // A long unsigned int is used here as the function can deal with larger positive numbers in a short space of time.
{
    register double loop_counter = 1;                           // This is the return value that shall be incremented over the course of the program. Register used to make it faster. Uses processors registers.
    
    while(exponent != 0)                                        // If the exponent is zero, the loop_counter is returned which is 1
    {
        if((exponent&1) == 0)                                   // modulo as an expensive operation changed to a bitwise and.
        {
            loop_counter = loop_counter * base;                 // If it is even the loop counter is multiplied by base and exponent reduced.
            exponent--;
        }
        base = base * base;                                     // The base is then squared.
        exponent = exponent>>1;                                 // Dividing by 2 has been changed to a right shift.
    }
    
    return loop_counter;
}



// An extra function here was removed.

// End of the program


