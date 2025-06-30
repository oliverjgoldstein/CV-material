//
//  Binary_Tree.c
//  
//
//  Created by Oliver Goldstein on 16/11/2014.
//
//

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct BSTNode
{
    char *name;
    char *number;
    struct BSTNode *left;
    struct BSTNode *right;
};


struct BSTNode* get_new_node(char *name, char *number)
{
    BSTNode *new_Node = (struct BSTNode*)malloc(sizeof(struct BSTNode));
    new_Node->name = name;
    new_Node->name = number;
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
    else if(name <= rootPTR->name)
    {
        rootPTR->left = insert_node(rootPTR->left, name, number);
    }
    else
    {
        rootPTR->right = insert_node(rootPTR->right, name, number);
    }
    return rootPTR;
}

struct BSTNode* search(struct BSTNode *rootPTR, char *name)
{
    if(rootPTR == NULL)
    {
        return NULL;
    }
    else if(rootPTR->name == name)
    {
        return rootPTR->number;
    }
    else if(name<=rootPTR->name)
    {
        return search(rootPTR->left, name);
    }
    else if(name>rootPTR->name)
    {
        return search(rootPTR->right, name);
    }
    
}

int main()
{
    // Initialise arrays
    char *name;
    char *number;
    
    // Initialise root node.
    struct BSTNode *rootPTR;
    rootPTR = NULL;
    
    while(name[0] != '.')
    {
        scanf("%s %s", name, number);
        rootPTR = insert_node(rootPTR, name, number);
    }
    printf("\n");
    
    char *search_name;
    struct BSTNode *result;

    while(search_name[0] != '.')
    {
        scanf("%s", search_name);
        result = search(rootPTR,search_name);
        if(result != NULL)
        {
            printf("%s", result->number);
        }
        else
        {
            printf("NOT FOUND");
        }
    }
    return 0;
}








