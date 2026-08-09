//
//  Encryption Test.c
//  
//
//  Created by Oliver Goldstein on 03/10/2014.
//
//

#include <stdio.h>
#include <string.h>

void * encryption(char *source)
{
    int i; // Loop variable
    int ascii;
    int add;
    char addTo[strlen(source)];
    
    
    for(i=0;i<strlen(source);i++)
    {
        ascii = source[i];
        add = ascii + 13;
        char number = add; // why no *
        printf("Character = %c, with ASCII Code = %d\n",ascii,ascii);
        printf("Character = %c, with ASCII Code = %d\n",number,number);
        addTo[i] = number;
    }
    for(i=0;i<sizeof(addTo);i++)
    {
        printf("%c \n",addTo[i]);
    }
    return 0;
}

int main(void)
{
    char *encrypted;
    scanf("%s",encrypted);
    char *decryption = encryption(encrypted);
    return 0;
}

