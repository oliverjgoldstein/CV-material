//
//  reverseCString.c
//  
//
//  Created by Oliver Goldstein on 09/06/2015.
//
//

#include <stdio.h>
#ifdef DEBUG
/* #if (DEBUG > 0) && (DEBUG < 2) - Debug Level 1.
   #if (DEBUG > 1) && (DEBUG < 3) - Debug Level 2. 
   To compile: gcc -o uniqueStrings uniqueStrings.c -std=c99 -DDEBUG=1
*/

char * reverseCString(char *stringToReverse);

int main(int argc, char *argv[]) {
    if(argc!=2) {
        printf("Usage of command line arguments: %s string.\n", argv[0]);
    }

    char * reversedString = reverseCString(argv[1]);
    printf("Reversed string: %s \n", reversedString);
}

char * reverseCString(char *stringToReverse) {
    char * beggining = stringToReverse;
    char * end = stringToReverse;
    char tmp;
    if(stringToReverse) {
        while(*end) {
            ++end;
        }
        --end;
        while(stringToReverse<end) {
            tmp = *stringToReverse;
            *stringToReverse++ = *end;
            *end-- = tmp;
        }
    }
    return beggining;
}
#endif