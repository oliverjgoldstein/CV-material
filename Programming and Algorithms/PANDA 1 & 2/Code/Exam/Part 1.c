//
//  First Lab Test.c
//  
//
/*  Created by Oliver Goldstein on 28/10/2014. og14775 */
//
//

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>



/* USERNAME     NAME    VERSION A
 *
 * This program is not necessarily representative of the style we
 * would like you to adopt when writing programs. It has been written
 * solely for the purpose of the practical exam.
 *
 * This function calculates the probability that  if you
 * flipping a coin 10 times in a row, you will get 10 tails.
 */


double teninrow() {
    int i ;
    double p = 1.0 ;
    i=1;
    while(i<11) {
        p = p * 0.5 ;
        i++;
    }
    return p ;
}

int main() {
    //double heads_p = 0.5;
    printf( "10-in-row: %f\n", teninrow() ) ;
    //printf("Prob of 10 times :: in a row = %f\n", teninrow(1/6.0));
    return 0 ;
}