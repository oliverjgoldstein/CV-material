//
//  fibonacci.c
//  
//
//  Created by Oliver Goldstein on 24/11/2014.
//
//

#include <stdio.h>
#include <stdlib.h>

int i(int n) {
                                //n>=
    int x=0, y=1;
    while (n>0) {
    printf("n is :%d\n",n);
    printf("x is :%d\n",x);
    printf("y is :%d\n",y);
    
int z=x+y;
        printf("z is :%d\n",z);
    x=y;
    y=z;
    n--;
    }
    printf("result is :%d\n",x);
    return x;
}

int main()
{
    i(10);
    return 0;
}