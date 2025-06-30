 // Code 1.
#include <stdio.h>
#include <string.h>
#include <stdlib.h>


const char * convert_to_upper_case(char *full_name)
{
    int i = 0;
    int length_of_string = strlen(full_name);
    for (i=0;i<length_of_string;i++)
    {
        if((full_name[i]<=122) && (full_name[i]>=97))
        {
            full_name[i] -= 32;
        }
        else if((full_name[i]<=90) && (full_name[i]>=65))
        {
            // Do Nothing
        }
        else
        {
//            printf("Fatal Error: Name not in proper form.");
//            getchar();
//            exit(0);
//            break;
        }
    }
    return full_name;
}



const char * strip_double_letters(char *full_name)
{
//    char remove_symbols[10] = {' ','-'};
//    full_name = strtok(full_name, remove_symbols);
//    char *pch;
//    pch = strtok (full_name," ,.-;'_!@&^%$*/?()");
//    full_name = NULL;
//    char temp_string[200];
//    while (pch != NULL)
//    {
//        
//         strcpy(temp_string,pch);
//        strcat(full_name,temp_string);
//        pch = strtok (NULL, " ,.-");
//    }
    int i = 0;
    int previous_letter = 0;
    int skip_flag = 0;
    int temp_storage_counter = 0;
    char temp_storage[500];
    int length_of_string = strlen(full_name);
    int temp_full_name_int_value = 0;
    
    
    for (i=0;i<=length_of_string;i++)
    {
        temp_full_name_int_value = full_name[i];
        if(previous_letter == temp_full_name_int_value)
        {
            
            skip_flag = 1;
            
        }
        previous_letter = temp_full_name_int_value;
        
        if(skip_flag == 0)
        {
            temp_storage[temp_storage_counter] = full_name[i];
            //printf("%c", temp_storage[i]);
            temp_storage_counter++;
        } // Transfer string from one to the other.
        
        
        //printf("%c\n",full_name[i]);
        skip_flag = 0;
    }
    
    
    // full_name = temp_storage; WHY CANNOT U DO X + Y USING ARRAYS;
    strcpy(full_name,temp_storage);
    return full_name;
}



const char * remove_vowels(char *full_name) // WHY IS IT CONST CHAR.
{
    int length_of_string = strlen(full_name);
    char vowels[] = "AEIOUYWH '-_<>!@£&%()|"; // ARRAY SIZE IS FOR HUMAN.
    int i = 0;
    
    
    
    int i_two = 0;
    int vowel_array_length = strlen(vowels);
    
    
    
    char removed_vowels[100];
    int removed_vowels_counter = 1;
    int ok = 0;
    
    removed_vowels[0] = full_name[0];
    
    
    
    
    
    for (i=1;i<length_of_string;i++)
    {
        i_two=0;
        ok = 1;
        
        while(i_two<=vowel_array_length)
        {
            if(full_name[i]==vowels[i_two])
            {
                ok = 0;
            }
            i_two++;
        }
        if(ok == 1)
        {
            removed_vowels[removed_vowels_counter] = full_name[i];
            removed_vowels_counter++;
        }
        
        
        
        
        
    }
    removed_vowels[removed_vowels_counter] = '\0';
    strcpy(full_name,removed_vowels);
    return full_name;
}


/*
 THINGS TO ENCODE THE FIRST 3 AFTER
 IGNORE Both first letter and vowels
 
 1: B, P, F, V
 2: C, S, K, G, J, Q, X, Z
 3: D, T
 4: L
 5: M, N
 6: R
 */


const char * encode_string(char *full_name)
{
    int length_of_string = strlen(full_name);
    int i = 0;
    int t = 0;
    for (i=1;i<=length_of_string;i++)
    {
        t = full_name[i];
        //printf("%c",full_name[i]);
        
        if((t == 66) || (t == 70) || (t == 80) || (t == 86)) // WHY USE ONLY ONE | STRNCMP TO BE USED 66 or 80 86 70
        {
            full_name[i] = '1';
        }
        if((t == 67)||(t == 83)||(t == 75)||(t == 71)||(t == 74)||(t == 81)||(t == 88)||(t == 90)) // 67 83 75 71 74 81 88 90
        {
            full_name[i] = '2';
        }
        if((t == 68)||(t == 84)) // 68 84
        {
            full_name[i] = '3';
        }
        if(t == 76) // 76
        {
            full_name[i] = '4';
        }
        if((t == 77)||(t == 78)) // 77 78
        {
            full_name[i] = '5';
        }
        if(t == 82) // 82
        {
            full_name[i] = '6';
        }
        
        
        
    }
    // Return it with zeros if needed
    int fourspacelooper = 0;
    for(fourspacelooper = 0;fourspacelooper<=3;fourspacelooper++)
    {
        if(!(full_name[fourspacelooper]))
        {
            strcat(full_name,"0");
        }
        
    }
    char first_four[4];
    strncpy(first_four, full_name,4);
    strcpy(full_name,first_four);
    return full_name;
}


int main()
{
    char full_name[500];
    char temp_name[500];
    printf("Enter the last name: ");
    scanf("%200[0-9a-zA-Z!-@ ]s",temp_name); // fgets only handles spaces. which uses \n
    
    
    strcpy(full_name,temp_name);
    // strcpy(full_name,"ALloydDdEe\0");
    printf("1. %s\n",full_name); // ADd &
    convert_to_upper_case(full_name);
    printf("2. %s\n",full_name);
    strip_double_letters(full_name);
    printf("3. %s\n",full_name);
    remove_vowels(full_name);
    printf("4. %s\n",full_name);
    encode_string(full_name);
    printf("5. %4s\n",full_name);
    // DOES PROGRAM IGNORE PUNCTUATION.
    return 0;
}








