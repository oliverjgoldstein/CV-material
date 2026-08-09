//
//  First Lab Test.c
//  
//
/*  Created by Oliver Goldstein on 28/10/2014. og14775 */
//
//

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>


int main() {
    int numbers[11];
    int looper = 0;
    int difference = 0;
    int cumulative = 0;
    int second_looper = 0;
    int ignore[11];
    int flag = 0;
    int copies = 1;
    
    for(looper = 0;looper<11;looper++)
    {
        scanf("%d", &numbers[looper]);
        cumulative += numbers[looper];
    }
    
    for(looper = 0;looper<11;looper++)
    {
        for(second_looper=0;second_looper<=10;second_looper++)
        {
            
            
            if(second_looper!=looper)
            {
            
            
            if(numbers[looper] >= numbers[second_looper])
            {
                flag++;
            }
            if(numbers[looper]==numbers[second_looper])
            {
                copies++;
            }
            else if(numbers[looper]<numbers[second_looper])
            {
                
            }
            // Remove all instances of that number
            }
            
            
            
            
        }
        if(flag==10)
        {
           // printf("Cumulative: %d", cumulative);
            cumulative -= (copies*numbers[looper]);
            //printf("Cumulative: %d", cumulative);
            break;
        }
        flag = 0;
        copies = 1;
    }
    
    
    
    // sijldfjlksdjflkjshdlkfjhasldjfhlksadjf
    flag = 0;
    copies = 1;
    
    for(looper = 0;looper<11;looper++)
    {
        //printf("Hi");
        for(second_looper=0;second_looper<=10;second_looper++)
        {
            if(second_looper!=looper)
            {
            //printf("Hi2");
            if(numbers[looper] <= numbers[second_looper])
            {
                //printf("1");
                flag++;
            }
            if(numbers[looper]==numbers[second_looper])
            {
                  //              printf("2");
                copies++;
            }
            else if(numbers[looper]>numbers[second_looper])
            {
                    //            printf("3");
            }
            // Remove all instances of that number
            }
            
        }
        if(flag==10)
        {
            //printf("\nCumulative: %d", cumulative);
            cumulative -= (copies*numbers[looper]);
            //printf("Cumulative: %d", cumulative);
            break;
        }
        flag = 0;
        copies = 1;
    }
    
    
    for(looper = 0;looper<11;looper++)
    {
        difference = cumulative-numbers[looper];
        if(difference<0)
        {
            difference = difference*-1;
        }
        printf("\n%d\n", difference);
    }
    return 0;
}

