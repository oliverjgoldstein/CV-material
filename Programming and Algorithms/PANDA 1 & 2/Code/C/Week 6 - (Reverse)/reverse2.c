//
//  reverse1.c
//  
//
//  Created by Oliver Goldstein on 14/11/2014.
//
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct Node
{
    char data[1000];
    int line_number;
    struct Node *next;
};

struct Node *Insert_nthposition(char *data, int n, struct Node* head, int line_number)
{
    struct Node* temp1 = (struct Node*)calloc(1,sizeof(struct Node));
    strcpy(temp1->data,data);
    temp1->line_number = line_number;
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
        printf("%s",temp->data);
        temp = temp->next;
    }
}

void recursive_reverse_print(struct Node *head)
{
    if(head == NULL)
    {
        return;
    }
//    printf("%d",head->line_number);
    printf("%s",head->data);
    recursive_reverse_print(head->next);
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

int main()
{
    struct Node* head = NULL;
    int i = 0;
    int j = 0;
    int line_number = 0;
    char character[1000];
    while(1)
    {
        
        
        character[i] = getchar();
        
        
        if(character[i] == '.')
        {
            if(i == 0)
            {
                character[0] = '\0';
//              printf("%c",character[i]);
                character[1] = '\0';
                character[2] = '\0';
                character[3] = '\0';
            }
            else
            {
                if(i > 0)
                {
                    character[i] = '\n';
                }
                head = Insert_nthposition(character,1,head,line_number);
            }
            do
            {
                character[j] = '\0';
                j++;
            }while(j<=i);
            i = -1;
            break;
        }
        
        
        
        
        
            if(character[i] == '\n')
            {
                if(i==0)
                {
                    
                    
                }
                else
                {
                head = Insert_nthposition(character,1,head,line_number);
                line_number++;
//                character[i+1] = '\0';
                }
                while(j<=i)
                {
                    character[j] = '\0';
                    j++;
                }
                i = -1;
            }
        i++;
    }
//    print(
    recursive_reverse_print(head);
    
    return 0;
}
