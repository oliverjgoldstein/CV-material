//
//  sieve.c
//  
//
//  Created by Oliver Goldstein on 23/11/2014.
//
//

#include <stdio.h>
#include <stdlib.h>
#define LIMIT 1000000000

int *spawn_array(int n)
{
    int *array = malloc((n-1)*sizeof(int));
    int i;
    for(i=2;i<=n;i++)
    {
        array[i-2] = i;
    }
    return array;
}

void delete(int *unfiltered, int n)
{
    int check = 1;
    int step = 2;
    int i;
    int last = 0;
    while(1)
    {
        check = 0;
        for(i=last;i<(n-1);i=i+step)
        {
            if((unfiltered[i]!=step) && (unfiltered[i]!=0))
            {
                unfiltered[i] = 0;
                check = 1;
            }
        }
        if(check == 0)
        {
            break;
        }
        for(i=last+1;i<(n-1);i++)
        {
            if(unfiltered[i]!=0)
            {
                last = i;
                printf("Step: %d\n", step);
                step = unfiltered[i];
                break;
            }
        }
    }
    
}

void print_column(int *array, int n)
{
    int column_number = 0;
    for(int i = 0;i<(n-1);i++)
    {
        if(array[i]!=0)
        {
            printf("%-11d", array[i]);
            column_number++;
        }
        if(column_number == 10)
        {
            printf("\n");
            column_number=0;
        }
    }
    printf("\n");
}

int main()
{
    int *array = spawn_array(LIMIT);
    delete(array,LIMIT);
    print_column(array,LIMIT);
    return 0;
}
