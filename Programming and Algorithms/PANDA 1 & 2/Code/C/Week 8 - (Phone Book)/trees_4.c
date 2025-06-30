//
//  Part 4
//  
//
//  Created by Oliver Goldstein on 20/11/2014.
//
//

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

typedef struct Number
{
    char *number;
    struct Number *next;
}Number_node;

typedef struct List
{
    Number_node *head;
    Number_node *tail;
}List;

typedef struct Tree
{
    char *name;
    List phones;
    struct Tree *left;
    struct Tree *right;
}Tree;

void insert_phone(List *L, char *phone_number)
{
    Number_node *node = (Number_node *)malloc(sizeof(Number_node));
    node->next = NULL;
    node->number = phone_number;
    if((L->head == NULL) && (L->tail == NULL))
    {
        L->head = node;
        L->tail = node;
    }
    else
    {
        L->tail->next = node;
        L->tail = node;
    }
}

void print_list(List *L)
{
    Number_node *temp_move = L->head;
    while(temp_move != NULL)
    {
        printf("%s\n",temp_move->number);
        temp_move = temp_move->next;
    }
}



Tree *get_new_node(char *name)
{
    List new_List;
    Tree *new_Node = (Tree*)malloc(sizeof(Tree));
    new_Node->name = name;
    new_List.head = NULL;
    new_List.tail = NULL;
    new_Node->phones = new_List;
    new_Node->left = NULL;
    new_Node->right = NULL;
    return new_Node;
}



Tree* insert(Tree *root, char *name, char *number)
{
    if(root == NULL)
    {
        root = get_new_node(name);
        insert_phone(&(root->phones),number);
        return root;
    }
    else if(strcmp(name, root->name)==0)
    {
        insert_phone(&(root->phones),number);
    }
    else if(strcmp(name, root->name) < 0)
    {
         root->left = insert(root->left, name, number);
    }
    else
    {
         root->right = insert(root->right, name, number);
    }
    return root;
}

void capitalise(char *name)
{
    for(int i = 0;i<strlen(name);i++)
    {
        name[i] = toupper(name[i]);
    }
}

Tree *search(Tree *search_result, char *name)
{
    capitalise(name);
    if(search_result == NULL)
    {
        return NULL;
    }
    else if(strcmp(name, search_result->name)==0)
    {
//        printf("name: %s, search result: %s\n", name, search_result->name);
        return search_result;
    }
    else if(strcmp(name, search_result->name) < 0)
    {
//        printf("name: %s, search result: %s\n", name, search_result->name);
        return search(search_result->left, name);
    }
    else if((strcmp(name, search_result->name)) > 0)
    {
//        printf("name: %s, search result: %s\n", name, search_result->name);
         return search(search_result->right, name);
    }
    else
    {
        return NULL;
    }
}

void myfgets(char *s,size_t n, FILE *ptr){
    fgets(s,n,ptr);
    if(s[strlen(s)-1]=='\n')
    {
        s[strlen(s)-1]='\0';
    }
}

int main()
{
    char line[2002];
    char name[1000];
    char number[1000];
    char *name_final=NULL;
    char *number_final=NULL;
    
    Tree *The_tree = NULL;
    Tree *The_search = NULL;
    myfgets(line,202,stdin);
    while(strcmp(line,".") != 0) // fgets reads new line
    {
        sscanf(line, "%s %s", name, number); // adds in a null character.
        name_final=malloc(strlen(name)+1);
        strcpy(name_final,name);
        number_final=malloc(strlen(number)+1);
        strcpy(number_final,number);
        capitalise(name_final);
        The_tree = insert(The_tree, name_final, number_final);
        myfgets(line,202,stdin);
    }
// Search // only typecast your own structures as cc does not know about them


    char n[100];
    myfgets(n, 100, stdin);
    while(strcmp(n,".") != 0) // scanf does not read new line.
    {
        The_search = search(The_tree, n);
        if(The_search != NULL)
        {
            print_list(&(The_search->phones));
        }
        else
        {
            printf("NOT FOUND\n");
        }
        myfgets(n, 100, stdin);
    }
    return 0;
}







