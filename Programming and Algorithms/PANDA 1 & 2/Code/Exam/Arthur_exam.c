#include <stdio.h>
#include <string.h>

int i;
int numbers[8];


int small (int numbers[8])  {
    
    int p=numbers[0];
    int j=0;
    int test[11];
    i=0;
    
    while (i<8) {
        if (p<numbers[i]) {
            test[j]=numbers[i];
            i++;
            j++;
        }
        else {
            p=test[j];
            p=numbers[i];
            i++;
            j++;
        }
    }
    
    test[j]='\0';
    numbers[7] = test[7];
    
    return *numbers;
    
}

int main () {
    
    for (i=0; i<8; i++) {
        scanf("%d", &numbers[i]);
    }
    small (numbers);
    
    for (i=0; i<7; i++){
        printf("%d", numbers[i]);
    }
    return 0;
    
    
}