//
//  Fibonacci_Week_7.c
//  
//
//  Created by Oliver Goldstein on 13/11/2014.
//
//

#include <stdio.h>

struct Pair
{
    double a, b;
};

struct Pair h(int n)
{
    struct Pair F;
    if(n==0)
    {
        return (F) {0,1};
    }
    if((n&1) == 0)
    {
        F = h(n/2);
        double a = F.a, b = F.b;
        return (F) {2*a*b-a*a,a*a+b*b};
    }
    if(n%2==1)
    {
        F = h(n-1);
        double a = F.a, b = F.b;
        return (F) {b,a+b};
        
    }
    return F;
}

int main()
{
    int n;
    scanf("%d",&n);
    struct Pair result = h(n);
    printf("%d,%d", (int)result.a,(int)result.b);
    return 0;
}