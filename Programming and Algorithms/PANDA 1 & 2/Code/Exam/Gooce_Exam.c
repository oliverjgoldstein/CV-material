#include <stdio.h>
int array(int number[]) {
    int i;
    int c=0;
    int d=0;
    int min=100;
    int max;
    
    
    
    
    
    for (i=1; i<=11; i++) {
        printf("Please enter the %d number \n", i);
        scanf("%d", &number[i]);
        c = c + number[i];
    }
    
    
    
    
    printf("Full sum is : %d \n",c);
    
    
    
    int min_multiplier = 0;
    int max_multiplier = 0;
    for (i=1; i<=11; i++) {
        printf("%d\n", number[i]);
        if (number[i]<min) {
            min=number[i];
        }
        if (number[i]>max) {
            max=number[i];
        }
    }
    
    for (i=1; i<=11; i++) {
        if(min==number[i])
        {
            min_multiplier++;
        }
        if(max==number[i])
        {
            max_multiplier++;
        }
    }
    
    
    
    
    printf("Sum without the minimum and maximum is : %d \n",c-((min_multiplier*min)+(max_multiplier*max)));
}
int main () {
    int number[100];
    array(number);
    return 0;
}