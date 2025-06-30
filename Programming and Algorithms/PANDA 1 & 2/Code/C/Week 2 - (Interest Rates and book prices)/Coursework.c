//  Coursework.c
//  Created by Bridget Welch
//  Compile with: gcc -o Coursework Coursework.c
//  Run with: ./Coursework

#include <stdio.h>
#include <math.h>

void excerciseOne(int x, int y);
void excerciseTwo();
void excerciseThree();
void excerciseFour();
long double iterateOverFunction(long double iterator);
long double absolute(long double value);

int main(int argc, char *argv[]) {
    excerciseOne(4, 7);
    excerciseTwo();
    excerciseThree();
    excerciseFour();
}

void excerciseOne(int x, int y) {
    // Adds two numbers together.
    int z = x + y;
    // Prints it on the screen.
    printf("%d\n", z);
}

void excerciseTwo() {
    int x;
    int y;
    
    /* Reads in integers x and y.
       Scanf reads into the address of x and y, here the & symbol is used to do this.
       %d is the placeholder for an integer.
    */
    
    scanf("%d", &x);
    scanf("%d", &y);
    
    // Here the decimal is cut off.
    int divisonNoDecimalPlace = x/y;
    // Here x and y must be casted into a float, in order that the division returns a decimal number.
    double divisionWithDecimalPlace = (float)x/(float)y;
    // % is the modulo operator returning the remainder.
    int modulo = x % y;
    
    printf("%d %f %d\n", divisonNoDecimalPlace, divisionWithDecimalPlace, modulo);
}

void excerciseThree() {
    // This will hold the cumulative score.
    int rollingSum = 0;
    
    // This for loop will go from numbers 1 to 100.
    for(int i = 1;i<=100;i++) {
    // This checks to see if the number is odd by checking the last bit.
    // If the last bit is 1 then it is odd.
        if((i & 1) == 1) {
            printf("%d\n", i);
            // It then prints the number and adds it to the rolling sum.
            rollingSum += i;
        }
    }
    // The rollingSum is then printed. This holds the total score.
    printf("%d\n", rollingSum);
}

void excerciseFour() {
    long double previousIteration = 0.1;
    long double iterator = iterateOverFunction(previousIteration);

    long double difference = absolute(iterator-previousIteration);
    while(difference>=0.000000000001) {
        printf("%.12Lf\n\n\n", iterator);
        previousIteration = iterator;
        iterator = iterateOverFunction(previousIteration);
        difference = absolute(iterator-previousIteration);
    }
}

long double iterateOverFunction(long double iterator) {
    return iterator - ((cos(iterator) - (iterator*iterator*iterator))/(-sin(iterator)-3*(iterator*iterator)));
}

long double absolute(long double number) {
    if (number < 0) {
        return -number;
    }
    else {
        return number;
    }
}




