//
//  Bounding Boxes Part 1.c
//  
//
//  Created by Oliver Goldstein on 01/11/2014.
//
//

#include <stdio.h>

// int c = 4;
// int x = 4*c;

struct Circle
{
    int radius;
    int x_center;
    int y_center;
};

struct Bounding_box
{
    int lower_left_x_coordinate;
    int lower_left_y_coordinate;
    int upper_right_x_coordinate;
    int upper_right_y_coordinate;
};

struct Circle create_circle(int radius, int x_center, int y_center)
{
    struct Circle Circle_one;
    Circle_one.radius = radius;
    Circle_one.x_center = x_center;
    Circle_one.y_center = y_center;
    return Circle_one;
}

struct Bounding_box create_box(int radius, int x_center, int y_center)
{
    struct Bounding_box Box_one;
    
    Box_one.lower_left_x_coordinate = x_center-radius;
    Box_one.lower_left_y_coordinate = y_center-radius;
    Box_one.upper_right_x_coordinate = x_center+radius;
    Box_one.upper_right_y_coordinate = y_center+radius;
    
    return Box_one;
}

void print_box(lower_left_x_coordinate,lower_left_y_coordinate,upper_right_x_coordinate,upper_right_y_coordinate)
{
    printf("(%d,%d) (%d,%d)\n",lower_left_x_coordinate,lower_left_y_coordinate,upper_right_x_coordinate,upper_right_y_coordinate);
}

int main(void)
{
    int radius = 3;
    int x_center = 1;
    int y_center = 2;
    struct Circle Circle_one = create_circle(radius,x_center,y_center);
    struct Bounding_box Box_one = create_box(Circle_one.radius,Circle_one.x_center,Circle_one.y_center);
    print_box(Box_one.lower_left_x_coordinate,Box_one.lower_left_y_coordinate,Box_one.upper_right_x_coordinate,Box_one.upper_right_y_coordinate);
    
    return 0;
}




