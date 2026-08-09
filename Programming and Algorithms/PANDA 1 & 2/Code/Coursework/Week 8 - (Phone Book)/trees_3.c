// Part 3/4
//  Binary_Tree.c
//  
//
//  Created by Oliver Goldstein on 16/11/2014.
//
//

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

struct BSTNode
{
    char *name;
    
    struct BSTNode *left;
    struct BSTNode *right;
};

struct Number
{
    char *number;
    struct Number *next;
};

struct list
{
    struct Number *head;
    struct Number *tail;
};



//struct Number *Insert_nthposition(char *number, int n, struct Number* head)
//{
//    struct Number* temp1 = (struct Number*)calloc(1,sizeof(struct Number));
////    printf("Number before: %s\n",head->number);
//    temp1->number = number;
//    temp1->next = NULL;
//    
//    if(n==1)
//    {
//        temp1->next = head;
//        head = temp1; //(address of head = address of temp1)
//        return head;
//    }
//    
//    struct Number *temp2 = head;
//    for(int i = 0;i<n-2;i++)
//    {
//        temp2 = temp2->next;
//    }
//    temp1->next = temp2->next;
//    temp2->next = temp1;
//    return head;
//}






struct BSTNode *get_new_node(char *name, char *number)
{
    struct BSTNode *new_Node = (struct BSTNode*)malloc(sizeof(struct BSTNode));
    new_Node->name = strdup(name);
    
    new_Node->headPTR = NULL; //(struct Number*)malloc(sizeof(struct Number));
    
    new_Node->headPTR = Insert_nthposition(number,1,new_Node->headPTR);
     // if NULL OR update function
    new_Node->left = NULL;
    new_Node->right = NULL;
    
    return new_Node;
}






struct BSTNode *search(struct BSTNode *search_result, char *name)
{
    if(search_result == NULL)
    {
        return NULL;
    }
    else if(strcmp(name, search_result->name)==0)
    {
        return search_result;
    }
    else if(strcmp(name, search_result->name) <= 0)
    {
        search_result = search(search_result->left, name);
    }
    else if((strcmp(name, search_result->name)) > 0)
    {
        search_result = search(search_result->right, name);
    }
    search_result = NULL;
    return search_result;
    
}






struct BSTNode *update_number(struct BSTNode *rootPTR, char *number)
{
    printf("2. Number before: %s\n", rootPTR->headPTR->number);
    printf("Inserting number: %s\n", number);
    printf("Pointer of head before: %p\n", rootPTR->headPTR);
    
    rootPTR->headPTR = Insert_nthposition(number,1,rootPTR->headPTR);
    
    printf("Pointer of head after: %p\n", rootPTR->headPTR);
    
    return rootPTR;
}







struct BSTNode* insert_node(struct BSTNode *rootPTR, char *name, char *number)
{
    if(rootPTR == NULL)
    {
        rootPTR = get_new_node(name, number);
        return rootPTR;
    }

    struct BSTNode *search_result = rootPTR;
    search_result = search(search_result, name);
    

    
    printf("Search: %p \n", search_result);
    
    
    if(search_result != NULL)
    {
        printf("Searching\n");
        printf("Number before: %s\n", rootPTR->headPTR->number);
        rootPTR = update_number(rootPTR, number);
        return rootPTR;
    }
    
    else if((strcmp(name, rootPTR->name)) < 0)
    {
    printf("Searching to the left ie input was smaller\n");
        rootPTR->left = insert_node(rootPTR->left, name, number);
    }
    else if((strcmp(name, rootPTR->name)) > 0)
    {
    printf("Searching to the right ie input was larger\n");
        rootPTR->right = insert_node(rootPTR->right, name, number);
    }
    return rootPTR;
}








// printing
void recursive_reverse_print(struct Number *headPTR)
{
    if(headPTR == NULL)
    {
        return;
    }
    printf("%s\n",headPTR->number);
    recursive_reverse_print(headPTR->next);
}

void print(struct BSTNode *rootPTR)
{
    if(rootPTR == NULL)
    {
        return;
    }
    recursive_reverse_print(rootPTR->headPTR);
    print(rootPTR->left);
    printf("NAME: %s \n",rootPTR->name);
    print(rootPTR->right);
}


int main()
{
    // Initialise arrays
    int name_size = 500;
    char *name = (char *)malloc(name_size*sizeof(char));
    char *number = (char *)malloc(50*sizeof(char));
    char *test = (char *)malloc(name_size*sizeof(char));
    
    
    // Initialise root node.
    struct BSTNode *rootPTR;
    rootPTR = NULL;
    do
    {
        fgets(test,name_size,stdin);
        if(test[0] == '.')
        {
            break;
        }
        
        if(test[0] == '\n')
        {
            printf("Please enter input\n");
            break;
        }
        
        if(test[strlen(test)]=='\n')
        {
            test[strlen(test)]='\0';
        }
        
        sscanf(test,"%s %s",name,number);
        
        for(int i=0;i<strlen(name);i++)
        {
            name[i] = toupper(name[i]); // capitalise
        }
        printf("Inserting number: %s\n", number);
        rootPTR = insert_node(rootPTR, name, number);

    }while(name[0] != '.');


    char *search_name = (char *)malloc(name_size*sizeof(char));
    struct BSTNode *result;
    print(rootPTR);
    printf("Printed. \n");
    do
    {
        fgets(search_name,name_size,stdin);
        if(search_name[0] == '.')
        {
            break;
        }
        
        if(search_name[0] == '\n')
        {
            printf("Please enter input\n");
            break;
        }
        
        if(search_name[strlen(search_name)]=='\n')
        {
            search_name[strlen(search_name)]='\0';
        }
        
        for(int i=0;i<strlen(search_name);i++)
        {
            search_name[i] = toupper(search_name[i]);
        }
        
        result = search(rootPTR,search_name);
        if(result != NULL)
        {
            print(rootPTR); // ONLY ONE ROOT PTR?
        }
        else
        {
            printf("NOT FOUND\n");
        }
    } while(search_name[0] != '.');
    return 0;
}




