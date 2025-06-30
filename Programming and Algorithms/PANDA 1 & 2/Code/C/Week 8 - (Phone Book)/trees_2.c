// Part 2
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
    char *number;
    struct BSTNode *left;
    struct BSTNode *right;
};


struct BSTNode* get_new_node(char *name, char *number)
{
    struct BSTNode *new_Node = (struct BSTNode*)malloc(sizeof(struct BSTNode));
    new_Node->name = strdup(name);
    new_Node->number = strdup(number);

    new_Node->left = NULL;
    new_Node->right = NULL;
    return new_Node;
}


struct BSTNode* insert_node(struct BSTNode *rootPTR, char *name, char *number)
{
    if(rootPTR == NULL)
    {
        rootPTR = get_new_node(name,number);
    }
    else if((strcmp(name, rootPTR->name)) <= 0)
    {
        rootPTR->left = insert_node(rootPTR->left, name, number);
    }
    else if((strcmp(name, rootPTR->name)) > 0)
    {
        rootPTR->right = insert_node(rootPTR->right, name, number);
    }
    return rootPTR;
}



struct BSTNode* search(struct BSTNode *rootPTR, char *name)
{
    struct BSTNode *search_name = rootPTR; // Takes in rootPTR and uses search.
    if(search_name == NULL)
    {
        return NULL;
    }
    else if(strcmp(name, search_name->name)==0)
    {
        return rootPTR;
    }
    else if(strcmp(name, search_name->name) <= 0)
    {
        rootPTR = search(search_name->left, name);
    }
    else if((strcmp(name, search_name->name)) > 0)
    {
        rootPTR = search(search_name->right, name);
    }
    return rootPTR;
    
}

void print(struct BSTNode *rootPTR)
{
    if(rootPTR == NULL)
    {
        return;
    }
    printf("NAME: %s\n",rootPTR->name);
    print(rootPTR->left);
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
    int flag = 0;
                                
    do
    {
        fgets(test,name_size,stdin);
        if(test[0] == '.')
        {
            break;
        }
        for(int i=0;i<strlen(test);i++)
        {
            if(test[i]=='\n')
            {
                test[i]='\0';
            }
            name[i] = toupper(name[i]);
        }

        sscanf(test,"%s %s",name,number);
        
        for(int i=0;i<strlen(name);i++)
        {
            name[i] = toupper(name[i]); // capitalise
        }
        
        rootPTR = insert_node(rootPTR, name, number);

    }while(name[0] != '.');

    flag = 0; // re initialise the point loop.
    char *search_name = (char *)malloc(name_size*sizeof(char));
    struct BSTNode *result;

    
    do
    {
        fgets(search_name,name_size,stdin);
        if(search_name[0] == '.')
        {
            break;
        }
        for(int i=0;i<strlen(search_name);i++)
        {
            if(search_name[i]=='\n')
            {
                search_name[i]='\0';
            }
            search_name[i] = toupper(search_name[i]);
        }
        
        result = search(rootPTR,search_name);
        if(result != NULL)
        {
            printf("%s\n", result->number);
        }
        else
        {
            printf("NOT FOUND\n");
        }
    } while(search_name[0] != '.');
    return 0;
}




