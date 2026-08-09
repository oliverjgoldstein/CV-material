//
//  List_Implementation.c
//  
//
//  Created by Oliver Goldstein on 12/11/2014.
//
//

#include <stdio.h>
#include <stdlib.h>
struct Node
{
    char data;
    struct Node *next;
};

struct Node *Insert_nthposition(int data, int n, struct Node* head)
{
    struct Node* temp1 = (struct Node*)calloc(sizeof(struct Node));
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

void Insert_end();


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


// For deletion: Fix Links and Free Space.
struct Node* delete_node(int position, struct Node* head)
{
    struct Node* temp1 = head;
    if(position == 1)
    {
        head = temp1->next;
        free(temp1);
        return head;
    }
    else
    {
        int i;
        for(i = 0;i<position-2;i++)
        {
            temp1 = temp1->next;
        }
        struct Node* temp2 = temp1->next;
        temp1->next = temp2->next;
        free(temp2);
        temp2 = NULL;
        return head;
    }
}


struct Node *iterative_reverse(struct Node *head)
{
    struct Node *current, *prev, *next;
    current = head;
    prev = NULL;
    while(current!=NULL)
    {
        next = current->next;
        current->next = prev;
        prev = current;
        current = next;
    }
    head = prev;
    return head;
}

//struct Node *recursive_reverse_print(struct Node *head)
//{
//    if(head == NULL)
//    {
//        return;
//    }
//    recursive_reverse_print(head->next);
//    printf("%d",head->data);
//}
//
//struct Node *recursive_reverse_print(struct Node *x,struct Node *head)
//{
//    if(x->next == NULL)
//    {
//        head = x;
//        return;
//    }
//    recursive_reverse_print(head->next);
//    struct Node* cool = x->next;
//    cool->next = x;
//    x->next = NULL;
//}


//struct Node *delete_node

int main()
{
    struct Node* head = NULL;
    head = Insert_nthposition('a',1, head);
    head = Insert_nthposition('b',2, head);
//    head = delete_node(2,head);
    head = iterative_reverse(head);
    Print(head);
    return 0;
}

















