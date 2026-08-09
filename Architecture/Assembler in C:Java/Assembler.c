#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main()
{
    char ch;
    // Character to read line by line.
    
    FILE *fp;
    // File to read.
    
    int instructionLength = 8;
    // How many characters to go through per instruction.
    
    int charCounter = 0;
    // How many characters have we gone through so far.
    
    int lineCount = 0;
    // How many line number are there.
    
    int lineCounter = 0;
    // What line number are we on.
    
    char *instruction = "";
    // Instruction string to read in.
    
    char file_name[20];
    // File path.
    
    char cToStr[2];
    // Append to a \0 and then use strcat on each character.
    cToStr[1] = '\0';
    
    
    
    printf("Enter the name of file you wish to see: \n");
    scanf("%s", file_name);
    fp = fopen(file_name,"r");
    
    if(fp == NULL)
    {
        perror("Error while opening the file. \n");
        exit(EXIT_FAILURE);
    }
    
    
    // Find the number of lines.
    while((ch = fgetc(fp)) != EOF)
    {
        if(ch == '\n')
        {
            lineCount++;
        }
    }
    
    
    while((lineCounter != lineCount))
    {
        while(charCounter != instructionLength)
        {
            ch = fgetc(fp);
            char currentChar = ch;
            cToStr[0] = currentChar;
            switch(ch)
            {
                case ';':
                    charCounter = instructionLength;
                    break;
                default:
                    strcat(instruction, cToStr);
                    charCounter++;
            }
        line
    }
    
    fclose(fp);
    return 0;
}
