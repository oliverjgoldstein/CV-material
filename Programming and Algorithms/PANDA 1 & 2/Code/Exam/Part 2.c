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
    
    int flag = 0;
    
    for(looper = 0;looper<11;looper++)
    {
        scanf("%d", &numbers[looper]);
        cumulative += numbers[looper];
    }
    
//    for(looper = 0;looper<11;looper++)
//    {
//        for(second_looper=1;second_looper<=11;second_looper++)
//        {
//            if(numbers[looper] > numbers[second_looper])
//            {
//                
//            }
//            else
//            {
//                flag++;
//            }
//            // Remove all instances of that number
//            
//        }
//    }
//    
    
    for(looper = 0;looper<11;looper++)
    {
        difference = cumulative-numbers[looper];
        printf("\n%d\n", difference);
    }
    return 0;
}

