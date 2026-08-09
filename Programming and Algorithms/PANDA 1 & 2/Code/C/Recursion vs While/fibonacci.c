//
//  fibonacci.c
//  
//
//  Created by Oliver Goldstein on 26/06/2015.
//
//

#include <stdio.h>

int fibonacciRecursive(int n);
int fibonacciIterative(int n);

int main(int argc, char *argv[]) {
    fibonacciIterative(5);
    fibonacciRecursive(100);
    return 0;
}

int fibonacciRecursive(int n) {
    if(n == 0) {
        return 0;
    } else if(n == 1) {
        return 1;
    } else if (n>1) {
        return fibonacciRecursive(n-1) + fibonacciRecursive(n-2);
    } else {
        return -1;
    }
}

int fibonacciIterative(int n) {
    if(n < 0) {
        return -1;
    } else if(n == 0) {
        return 0;
    }
    int a = 1;
    int b = 1;
    for(int i = 3; i<=n; i++) {
        int c = a+b;
        a = b;
        b = c;
    }
    return b;
}