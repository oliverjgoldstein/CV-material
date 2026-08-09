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
    struct Circle Circle_func;
    Circle_func.radius = radius;
    Circle_func.x_center = x_center;
    Circle_func.y_center = y_center;
    return Circle_func;
}

struct Bounding_box create_box(int radius, int x_center, int y_center)
{
    struct Bounding_box Box_func;
    
    Box_func.lower_left_x_coordinate = x_center-radius;
    Box_func.lower_left_y_coordinate = y_center-radius;
    Box_func.upper_right_x_coordinate = x_center+radius;
    Box_func.upper_right_y_coordinate = y_center+radius;
    
    return Box_func;
}

struct Bounding_box add_boxes(int B1LX, int B1LY, int B1UX, int B1UY, int B2LX, int B2LY, int B2UX, int B2UY)
{
    struct Bounding_box Box_func;
    Box_func.lower_left_x_coordinate = 0;
    Box_func.lower_left_y_coordinate = 0;
    Box_func.upper_right_x_coordinate = 0;
    Box_func.upper_right_y_coordinate = 0;
    if(B1LX<=B2LX)
    {
        Box_func.lower_left_x_coordinate = B1LX;
    }
    else
    {
        Box_func.lower_left_x_coordinate = B2LX;
    }
    if(B1LY<=B2LY)
    {
        Box_func.lower_left_y_coordinate = B1LY;
    }
    else
    {
        Box_func.lower_left_x_coordinate = B2LY;
    }
    if(B1UY>=B2UY)
    {
        Box_func.upper_right_y_coordinate = B1UY;
    }
    else
    {
        Box_func.upper_right_y_coordinate = B2UY;
    }
    if(B1UX>=B2UX)
    {
        Box_func.upper_right_x_coordinate = B1UX;
    }
    else
    {
        Box_func.upper_right_x_coordinate = B2UX;
    }
    return Box_func;
}

void print_box(int lower_left_x_coordinate,int lower_left_y_coordinate,int upper_right_x_coordinate,int upper_right_y_coordinate)
{
    printf("(%d,%d) (%d,%d)\n",lower_left_x_coordinate,lower_left_y_coordinate,upper_right_x_coordinate,upper_right_y_coordinate);
}

int main(void)
{
    int radius[2];
    int x_center[2];
    int y_center[2];
    scanf("%d %d %d %d %d %d", &radius[0], &radius[1], &x_center[0], &x_center[1], &y_center[0], &y_center[1]); // x y r
//    int radius[2] = {3,2};
    struct Circle Circle_one = create_circle(radius[0],x_center[0],y_center[0]);
    struct Bounding_box Box_one = create_box(Circle_one.radius,Circle_one.x_center,Circle_one.y_center);
    struct Circle Circle_two = create_circle(radius[1],x_center[1],y_center[1]);
    struct Bounding_box Box_two = create_box(Circle_two.radius,Circle_two.x_center,Circle_two.y_center);
    struct Bounding_box Box_three = add_boxes(Box_one.lower_left_x_coordinate, Box_one.lower_left_y_coordinate, Box_one.upper_right_x_coordinate, Box_one.upper_right_x_coordinate, Box_two.lower_left_x_coordinate,Box_two.lower_left_y_coordinate,Box_two.upper_right_x_coordinate,Box_two.upper_right_y_coordinate);
    
    
    
    print_box(Box_three.lower_left_x_coordinate,Box_three.lower_left_y_coordinate,Box_three.upper_right_x_coordinate,Box_three.upper_right_y_coordinate);
    
    return 0;
}




