//
//  Count Occurrence in a Sorted Array.c
//  
//
//  Created by Oliver Goldstein on 26/06/2015.
//
//

#include <stdio.h>

int iterativeBinarySearch(int A[], int arraySize, int x) {
    int low = 0, high = (arraySize-1);
    while(
    int mid = (low+high)/2;
    if(A[mid] == x) {
        return mid;
    } else if (x<A[mid]) {
        high = mid - 1;
    } else if (x>A[mid]) {
        low = mid + 1;
    }
}

int recursiveBinarySearch(int A[], int low, int high, int x) {
    if(low>high) {
        return -1;
    }
    int mid = low + (high-low)/2;
    if(x==A[mid]) {
        return mid;
    } else if (x<A[mid]) {
        return recursiveBinarySearch(A,low,mid-1,x);
    } else if (x>A[mid]) {
        return recursiveBinarySearch(A,mid+1,high,x);
    }
}

int main() {
    int A[] = {1,2,4,7,9,11,21,100};
    int numberToSearchFor;
    scanf(
}