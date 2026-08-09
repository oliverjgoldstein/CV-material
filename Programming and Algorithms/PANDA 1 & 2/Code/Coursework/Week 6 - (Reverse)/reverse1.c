//
//  reverse1.c
//  
//
//  Created by Oliver Goldstein on 14/11/2014.
//
//

#include <stdio.h>
#include <stdlib.h>
struct Node
{
    char data;
    struct Node *next;
};

struct Node *Insert_nthposition(char data, int n, struct Node* head)
{
    struct Node* temp1 = (struct Node*)calloc(1,sizeof(struct Node));
    temp1->data = data;
    temp1->next = NULL;
    if(n==1)
    {
        temp1->next = head;
        head = temp1; //(address of head = address of temp1)
        return head;
    }
    
    
        struct Node *temp2 = head;
        for(int i = 0;i<n-2;i++)
        {
            temp2 = temp2->next;
        }
        temp1->next = temp2->next;
        temp2->next = temp1;
        return head;
}

void Print(struct Node* head)
{
    struct Node *temp = head;
    // this program is only for first iteration
    while(temp!=NULL)
    {
        printf("%c",temp->data);
        temp = temp->next;
    }
}

void recursive_reverse_print(struct Node *head)
{
    if(head == NULL)
    {
        return;
    }
    printf("%c",head->data);
    recursive_reverse_print(head->next);
}

int main()
{
//    printf("%d",sizeof(struct Node));
    struct Node* head = NULL;
    char character = 'a';
    // sets variable, length and input stream
    while(character!='.')
    {
//      struct Node* Node1;
        character = getchar();
//      Node1->data = character;
        head = Insert_nthposition(character,1,head);
    }
    
    recursive_reverse_print(head);
    
    return 0;
}