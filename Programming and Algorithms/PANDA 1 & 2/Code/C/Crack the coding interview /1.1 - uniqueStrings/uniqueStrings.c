//
//  uniqueStrings.c
//  
//
//  Created by Oliver Goldstein on 05/04/2015.
//
//

#include <stdio.h>
#include <stdbool.h>
#include <string.h>

#ifdef DEBUG
/* #if (DEBUG > 0) && (DEBUG < 2) - Debug Level 1.
   #if (DEBUG > 1) && (DEBUG < 3) - Debug Level 2. 
   To compile: gcc -o uniqueStrings uniqueStrings.c -std=c99 -DDEBUG=1
*/

bool areCharsUnique(char stringToTest[]);

int main(int argc, char *argv[]) {

    if(argc != 2) {
        printf("Usage of command line arguments: %s string.\n", argv[0]);
    }
    
    bool isStringUnique = areCharsUnique(argv[1]);

    if(isStringUnique) {
        printf("The characters are all unique.\n");
    }
    else {
        printf("The characters are not all unique.\n");
    }
    
    #if (DEBUG > 0) && (DEBUG < 2)
    printf("The boolean value is: %s", isStringUnique ? "true" : "false");
    #endif

}

bool areCharsUnique(char stringToTest[]) {
    // Here, I make a boolean array to represent the locations of the letters.
    bool charSet[256];
    
    #if (DEBUG > 0) && (DEBUG < 2)
    printf("The size of the string in the command line was %lu: \n", strlen(stringToTest));
    #endif
    
    for(int i = 0;i<strlen(stringToTest);i++) {
        
        // This converts to ascii; Yes, hackish - but all computers are ascii.
        int letterLocationInArray = stringToTest[i] - '0';
        
        if(charSet[letterLocationInArray] == true) {
            return false;
        }
        
        else {
            charSet[letterLocationInArray] = true;
        }
    }
    
    return true;
}

#endif