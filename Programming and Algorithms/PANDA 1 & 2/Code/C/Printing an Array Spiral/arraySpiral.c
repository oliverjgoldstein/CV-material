//
//  arraySpiral.c
//  
//
//  Created by Oliver Goldstein on 26/06/2015.
//
//

#include <stdio.h>

int main() {
    char arraySpiral[4][4] = {

        {'a', 'r', 'r', 'a'},
        {'l', 's', ' ', 'y'},
        {'a', ')', ':', ' '},
        {'r', 'i', 'p', 's'}
    };
    
    int direction = 0;
    
    int top = 0;
    int bottom = 3;
    int left = 0;
    int right = 3;
    
    while((top<=bottom) && (left<=right)) {
        if(direction == 0) {
            for(int i = left;i<=right;i++) {
                printf("%c", arraySpiral[top][i]);
            }
            top++;
            direction = 1;
        } else if (direction == 1) {
            for(int i = top;i<=bottom;i++) {
                printf("%c", arraySpiral[i][right]);
            }
            right--;
            direction = 2;
        } else if (direction == 2) {
            for(int i = right;i>=left;i--) {
                printf("%c", arraySpiral[bottom][i]);
            }
            bottom--;
            direction = 3;
        } else if (direction == 3) {
            for(int i = bottom;i>=top;i--) {
                printf("%c", arraySpiral[i][left]);
            }
            left++;
            direction = 0;
        }
        // End of while loop.
    }
}