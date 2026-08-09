//
//  Recursive_Worksheet_6.c
//  
//
//  Created by Oliver Goldstein on 13/11/2014.
//
//

#include <stdio.h>

int g(int n, int a, int b)
{
    if(n==0)
    {
        return a;
    }
    if(n==1)
    {
        return b;
    }
    if(n>=2)
    {
        return (g(n-1,a,b)+g(n-2,a,b));
    }
    else
    {
        printf("Error");
//        exit();
    }
//    return
}

int h(int n, int a, int b)
{
    if(n==0)
    {
        return a;
    }
    if(n>=1)
    {
        return h(n-1,b,a+b);
    }
}

int main()
{
    int a = g(40,6,1);
    int b = h(40,6,1);
    printf("%d,%d",a,b);
    return 0;
}












