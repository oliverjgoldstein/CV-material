//
//  checkIfBinaryTree.c
//  
//
//  Created by Oliver Goldstein on 04/07/2015.
//
//

#include <stdio.h>

struct Node {
    int data;
    Node *left;
    Node *right;
};

int isSubtreeLesser(Node *root, int value) {
    if(root == NULL) {
        return true;
    }
    if(root->data <= value && isSubtreeLesser(root->left, value) && isSubtreeGreater(root->right, value)) {
        return true;
    } else {
        return false;
    }
}
int isSubtreeGreater(Node *root, int value);

int isBinarySearchTree(Node *root) {
    if(root == NULL) {
        return true;
    }
    if(isSubtreeLesser(root->left, root->data) && isSubtreeGreater(root->right, root->data)
       && isBinarySearchTree(root->left) && isBinarySearchTree(root->right)) {
        return true;
    } else {
        return false;
    }
}