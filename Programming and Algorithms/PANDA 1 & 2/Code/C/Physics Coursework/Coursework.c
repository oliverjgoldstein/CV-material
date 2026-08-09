//  Coursework.c
//  Created by Bridget Welch
//  Compile with: gcc -o Coursework Coursework.c
//  Run with: ./Coursework

#include <stdio.h>
#include <math.h>
#include <stdlib.h>

void excerciseOne(int x, int y);
void excerciseTwo();
void excerciseThree();
void excerciseFour();
long double iterateOverFunction(long double iterator);
long double absolute(long double value);
double returnRandomNumber();
void excerciseFive();
void excerciseSix();
void excerciseSeven();
double calculateRandOneAndMinusOne();
void randGauss();
long double returnRandomW();

int main(int argc, char *argv[]) {
    excerciseOne(4, 7);
    excerciseTwo();
    excerciseThree();
    excerciseFour();
    excerciseFive();
    excerciseSix();
    excerciseSeven();
}

void excerciseOne(int x, int y) {
    // Adds two numbers together.
    int z = x + y;
    // Prints it on the screen.
    printf("Added 4 and 7: %d\n", z);
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
    
    printf("Division: %d With Decimal Place: %f Remainder: %d\n", divisonNoDecimalPlace, divisionWithDecimalPlace, modulo);
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
    // This acts as out initial 'guess'.
    long double previousIteration = 0.1;
    long double iterator = iterateOverFunction(previousIteration);

    long double difference = absolute(iterator-previousIteration);
    // The while loop checks that the difference is smaller than epsilon before halting.
    while(difference>=0.000000000001) {
    // This makes sure that it prints to 12 decimal places.
        printf("%.12Lf\n", iterator);
        previousIteration = iterator;
        iterator = iterateOverFunction(previousIteration);
        difference = absolute(iterator-previousIteration);
    }
}

long double iterateOverFunction(long double iterator) {
    // This implements newton's method
    return iterator - ((cos(iterator) - (iterator*iterator*iterator))/(-sin(iterator)-3*(iterator*iterator)));
}

long double absolute(long double number) {
    // This returns the positive number.
    if (number < 0) {
        return -number;
    }
    else {
        return number;
    }
}

void excerciseFive() {
    // This generates a random number.
    double randomNumber = returnRandomNumber();
}

double returnRandomNumber() {
    // This returns a random number between 0 and 1 (when RAND_MAX/RAND_MAX).
    return rand()/(double)RAND_MAX;
}

void excerciseSix() {
    int binNumber = 0;
    int sampleNumber = 0;
    scanf("%d", &binNumber);
    scanf("%d", &sampleNumber);
    int *bins;
    bins = (int*) malloc(binNumber*sizeof(int));
    
    for (int i = 0; i < binNumber; i++) {
        bins[i] = 0;
    }
    
    long double one = 1;
    
    for(int i = 0;i<sampleNumber;i++) {
        long double firstRandomNumber = returnRandomNumber();
        int binToChoose = ceil(firstRandomNumber/(one/(long double)binNumber));
        bins[binToChoose]++;
    }
    
    long double totalFrequency = 0;
    
    for(int i = 1;i <= binNumber;i++) {
        int binCount = bins[i];
        long double individualBinFrequency = (long double)binCount/(long double)sampleNumber;
        printf("Individual bin frequency: %Lf\n", individualBinFrequency);
        totalFrequency+=individualBinFrequency;
    }
    
    printf("Total cumulative freq: %Lf\n", totalFrequency);
    
    free(bins);
}

void excerciseSeven() {
    returnRandomW();
    randGauss();
}

void randGauss() {
    int binNumber = 0;
    int sampleNumber = 0;
    scanf("%d", &binNumber);
    scanf("%d", &sampleNumber);
    int *bins;
    bins = (int*) malloc(binNumber*sizeof(int));
    
    for (int i = 0; i < binNumber; i++) {
        bins[i] = 0;
    }
    
    long double one = 1;
    
    for(int i = 0;i<sampleNumber;i++) {
        long double firstRandomNumber = returnRandomW();
        int binToChoose = ceil(firstRandomNumber/(one/(long double)binNumber));
        bins[binToChoose]++;
    }
    
    long double totalFrequency = 0;
    
    for(int i = 0;i <= binNumber;i++) {
        int binCount = bins[i];
        long double individualBinFrequency = (long double)binCount/(long double)sampleNumber;
        printf("Individual bin frequency: %Lf\n", individualBinFrequency);
        totalFrequency+=individualBinFrequency;
    }
    
    printf("Total cumulative freq: %Lf\n", totalFrequency);
    
    free(bins);
}

double calculateRandOneAndMinusOne() {
    double firstRandomNumber = returnRandomNumber();
    firstRandomNumber = firstRandomNumber*2 - 1;
    return firstRandomNumber;
}

long double returnRandomW() {
    long double z = 2;
    long double x;
    long double y;
    while(!(z<1)) {
        x = calculateRandOneAndMinusOne();
        y = calculateRandOneAndMinusOne();
        z = (x*x)+(y*y);
    }
    long double w = 0.2*x*sqrt(-2*log(z)/z)+0.5;
    return w;
}
