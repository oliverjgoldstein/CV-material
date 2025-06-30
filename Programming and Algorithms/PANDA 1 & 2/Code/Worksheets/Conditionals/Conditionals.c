//
//  Conditionals.c
//  
//
//  Created by Oliver Goldstein on 09/10/2014.
//
//

#include <math.h>
#include <stdio.h>

int a = 5 ;
int b = 7 ;
int c = 11;

if( (a <= 5) && (b > 7) ) {
    printf("Fragment 1 False\n" ) ;
}

if( ((a < 5) && (b > 7)) || (a < b) || (b > 1000) ) {
    printf("Fragment 2 True\n" ) ;
}

if( !(!(a <= 5) && !(b > 7)) ) {
    printf("Fragment 3 True\n" ) ;
}

if( ((a <= 5) && (b > 7)) != 0 || ( c >= a*b ) ) {
    printf("Fragment 4 False\n" ) ;
}

if( (c = 1) || (b > 9) || (a < -3) ) {
    printf("Fragment 5 True\n" ) ;
}

int func( int a, int b, int c ) {
    int x0 = (a < 7) ;
    int x1 = (a <= 5) || (b > 7) ;
    int x2 = (a>5)&&(a<7);
}

// FOR I

void shapes(double x, double y)
{
    if( (x<=3 && x>=1) && (y<=2 && y>=1))
    {
        printf("Shape I");
    }
    if( sqrt(     ((x-1)*(x-1)) + (y-1)*(y-1)) <= 1)
    {
        printf("Shape II");
    }
    if( !((x<=3 && x>=1) && (y<=2 && y>=1)) && (x>=0 && Y>=0))
    {
        printf("Shape III");
    }
    if ((y<=x+2) && (y>=-x) && (y>=x) && (y<=-x+2)) {
        printf("Shape IIII");
    }
}


a(int y) {
    while( y >=0 ) {
        printf( "%d", y ) ;
        y = y - 1 ;
    }
}

5
4
3
2
1
0

b(int y) {
    if( y >=0 ) {
        b( y - 1 ) ;
        printf( "%d", y ) ;
    }
}

0
1
2
3
4
5

c(int y) {
    printf( "%d", y ) ;
    c( y - 1 ) ;
}

-> infinite

d(int y) {
    while( y > 0 ) {
        printf( "%d", y ) ;
        d( y - 1 ) ;
    }
    printf( "%d", y ) ;
}

5
4
3
2
1
0
1
0
1
0
.
.
.
.

e(int y) {
    while( y > 0 ) {
        printf( "%d", y ) ;
        y = y - 1 ;
    }
    e( y - 1 ) ;
    printf( "%d", y ) ;
}



f(int y) {
    if( y >=0 ) {
        printf( "%d", y ) ;
    }
    f( y - 1 ) ;
}

























































