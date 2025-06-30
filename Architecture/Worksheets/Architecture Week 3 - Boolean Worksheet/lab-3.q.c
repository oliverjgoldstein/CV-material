//
//  lab-3.q.c
//  
//
//  Created by Oliver Goldstein on 20/10/2014.
//
//

#include <stdio.h>
#include "lab-3.q.h"

void rep( int8_t x ) {
    printf( "%4d_{(10)} = ", x );
    
    for( int i = ( BITSOF( x ) - 1 );
        
        
        
        // Taking & compares the bit to the left of the | in the examples below.
        // By right shifting i times 7 through to 1 it takes the bit of the number and does a bitwise and operator to see i it is 1 or zero and prints it.
        
        
        
        i >= 0; i-- )
    {
        
//        SIGNED 7
//        0|0000111 >> 7 == 0 & 1 = 0
//        00|000111 >> 6 == 0 & 1 = 0
//        000|00111 >> 5 == 0 & 1 = 0
//        0000|0111 >> 4 == 0 & 1 = 0
//        00000|111 >> 3 == 0 & 1 = 0
//        000001|11 >> 2 == 1 & 1 = 1
//        0000011|1 >> 1 == 3 & 1 = 1
//        00000111| >> 0 == 7 & 1 = 1
//        SIGNED -7
//        1|1111001 >> 7 == (1 & 1) = 1
//        11|111001 >> 6 == 3 & 1 = 1
//        111|11001 >> 5 == 7 & 1 = 1
//        1111|1001 >> 4 == 15 & 1 = 1
//        11111|001 >> 3 == 31 & 1 = 1
//        111110|01 >> 2 == 62 & 1 = 0
//        1111100|1 >> 1 == 124 & 1 = 0
//        11111001| >> 0 == -7 & 1 = 1
//        Unsigned -7 = 249 gives same.
        
        printf( "%d", ( x >> i ) & 1 );
        
        
        
    }
    
    
    
    
    
    
    
    printf( "_{(2)}\n" );
    
    
    
    
    
}

int main( int argc, char* argv[] ) {
    int8_t t;
    printf( "%d", -1 & 1 );
    t =    0; rep( t );
    t =   +1; rep( t );
    t =   -7; rep( t );
    t =   -1; rep( t );
    t = +127; rep( t );
    t = -128; rep( t );
    t = 255; rep( t );
    
    return 0;
}
