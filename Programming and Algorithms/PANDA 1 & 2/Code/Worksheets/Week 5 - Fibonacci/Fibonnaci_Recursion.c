//
//  Fibonnaci_Recursion.c
//  
//
//  Created by Oliver Goldstein on 30/10/2014.
//
//

#include <stdio.h>
int result = 0;
int f(int n)
{
    if(n==0)
    {
        return 0;
    }
    if(n==1)
    {
        return 1;
    }
    if(n>=2)
    {
        
        result = f(n-1)+f(n-2);
        if((result>>31)&1)
        {
            return f(n-1);
        }
        return result;
    }
    return result;
//    011235
}

int g(int n)
{
    int i = 2;
    int a = 0;
    int b = 1;
    int c = 0;
    int previous;
    if(n == 0)
    {
        return 0;
    }
    
    if(n==1)
    {
        return 1;
    }
    
    while(1)
    {
        
        

        c = a + b;
        a = b;
        if((c>>31)&1)
        {
            return b;
            break;
        }
        b = c;
    }
    
    return c;
}



int main()
{
    int result = f((1<<31)-1);
    printf("\n%d\n\n",result);
    result = g((1<<31)-1);
    printf("\n%d\n\n",result);
    return 0;
}
