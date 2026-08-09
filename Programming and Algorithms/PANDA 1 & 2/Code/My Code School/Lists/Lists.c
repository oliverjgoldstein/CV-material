//
//  Lists.c
//  
//
//  Created by Oliver Goldstein on 08/11/2014.
//
//

#include <stdio.h>

needs to:
empty list has size 0
insert
remove
count
read/modify element at a certain position
specify data type/
Dynamic list.

to make this dynamic list we could use a really large array:

 ARRAYS BASICALLY SUCK AS MODIFYING requires moving all elements to the left/right.
int A[MAXSIZE];
int e = -1; // if e is -1 list is empty?
with arrays even with malloc when using realloc an array double size is made and everything is copied over.
accessing elements is easy as O(1) it is constant time to read or write but when inserting, removing or adding to the array the time it take is proportional to the length of the array. COSTLY.
Also alot of the array is often unused. INEFFICIENT.
advantages of arrays is that it takes constant time to access any member of an array.
memory manager does not expect array extension.
FOR AN ARRAY USING MALLOC -> IF SPACE IS AVAILABLE ADJACENT TO THE ARRAY THEN IT WILL BE ALLOCATED ELSE A NEW BLOCK WILL BE ALLOCATED.


---- To solve this we can use linked lists.

variable arrays = char name[n] = upon compilation a variable is used to keep track of the size ON CREATIOn so cannot change after that.
variable arrays have a hidden variable in compiler basically. Cannot be changed after compile time.
e.g.
void f(int n)
char name[n]

as opposed
void f()
char name[10]

void f(int n)
char *name
name = malloc(n) // n is the number of bytes.
constant arrays = char name[10]

char *name = malloc(n);
creates a pointer to a char.
name = malloc i.e. the value of the pointer is arrowed to somehwere on the heap.
can do void sum(int n, int list[n]);

to avoid a rubbish pointer by doing char *name; do char *name = NULL; that means when you initialise it it points to memory thatt does not belong to you. therefore cannot accidentally be used.
char *stringreturn(int s)
is a function that returns a pointer.

----------------------------------------------------------------------------------------------------------------------------------------------------

Implementation
struct Node
{
    int data;
    struct Node *link;
};

















