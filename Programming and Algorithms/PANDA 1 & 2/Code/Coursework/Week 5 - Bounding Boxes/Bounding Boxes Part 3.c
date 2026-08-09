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



struct Bounding_box
{
    int lower_left_x_coordinate;
    int lower_left_y_coordinate;
    int upper_right_x_coordinate;
    int upper_right_y_coordinate;
};

struct Shape
{
    int tag;
    union
    {
        struct Line
        {
            int x_origin;
            int y_origin;
            int x_end;
            int y_end;
        }Line_func;
        struct Circle
        {
            int radius;
            int x_center;
            int y_center;
        }Circle_func;
    }Shape_choice;
};


struct Bounding_box create_shape_box(struct Shape A)
{
    struct Bounding_box Box_func;
    
    if(A.tag == 0)
    {
    Box_func.lower_left_x_coordinate = A.Shape_choice.Circle_func.x_center  -  A.Shape_choice.Circle_func.radius;
    Box_func.lower_left_y_coordinate = A.Shape_choice.Circle_func.y_center  -  A.Shape_choice.Circle_func.radius;
    Box_func.upper_right_x_coordinate = A.Shape_choice.Circle_func.x_center  +  A.Shape_choice.Circle_func.radius;
    Box_func.upper_right_y_coordinate = A.Shape_choice.Circle_func.y_center  +  A.Shape_choice.Circle_func.radius;
//        printf("Box created: Lower left x: %d, Lower left y: %d, Upper right x: %d, Upper right y: %d\n", Box_func.lower_left_x_coordinate, Box_func.lower_left_y_coordinate, Box_func.upper_right_x_coordinate, Box_func.upper_right_y_coordinate);
    }
    else if (A.tag == 1)
    {
        if(A.Shape_choice.Line_func.x_origin <= A.Shape_choice.Line_func.x_end)
        {
            Box_func.lower_left_x_coordinate = A.Shape_choice.Line_func.x_origin;
            Box_func.upper_right_x_coordinate = A.Shape_choice.Line_func.x_end;
        }
        else
        {
            Box_func.upper_right_x_coordinate = A.Shape_choice.Line_func.x_origin;
            Box_func.lower_left_x_coordinate = A.Shape_choice.Line_func.x_end;
        }
        if(A.Shape_choice.Line_func.y_origin <= A.Shape_choice.Line_func.y_end)
        {
            Box_func.lower_left_y_coordinate = A.Shape_choice.Line_func.y_origin;
            Box_func.upper_right_y_coordinate = A.Shape_choice.Line_func.y_end;
        }
        else
        {
            Box_func.lower_left_y_coordinate = A.Shape_choice.Line_func.y_end;
            Box_func.upper_right_y_coordinate = A.Shape_choice.Line_func.y_origin;
        }
//        printf("Box created: Lower left x: %d, Lower left y: %d, Upper right x: %d, Upper right y: %d\n", Box_func.lower_left_x_coordinate, Box_func.lower_left_y_coordinate, Box_func.upper_right_x_coordinate, Box_func.upper_right_y_coordinate);

    }
    
    return Box_func;
}

struct Bounding_box add_boxes(struct Bounding_box Shape, struct Bounding_box Aggregate)
{
    
    
    // Comparison box to make bigger i.e. aggregate box.
//    assign zero values
    
    if(Shape.lower_left_x_coordinate < Aggregate.lower_left_x_coordinate)
    {
        Aggregate.lower_left_x_coordinate = Shape.lower_left_x_coordinate;
    }
    
    if(Shape.lower_left_y_coordinate < Aggregate.lower_left_y_coordinate)
    {
        Aggregate.lower_left_y_coordinate = Shape.lower_left_y_coordinate;
    }
    
    if(Shape.upper_right_x_coordinate > Aggregate.upper_right_x_coordinate)
    {
        Aggregate.upper_right_x_coordinate = Shape.upper_right_x_coordinate;
    }
    
    if(Shape.upper_right_y_coordinate > Aggregate.upper_right_y_coordinate)
    {
        Aggregate.upper_right_y_coordinate = Shape.upper_right_y_coordinate;
    }
    
    return Aggregate;
}

void print_box(struct Bounding_box Aggregate)
{
    printf("(%d,%d) (%d,%d)\n",Aggregate.lower_left_x_coordinate,Aggregate.lower_left_y_coordinate,Aggregate.upper_right_x_coordinate,Aggregate.upper_right_y_coordinate);
}

int main(void)
{
    
    struct Shape A;
    
    char shape;
//    char shape2;
    int x1;
    int y1;
    int x2;
    int y2;
    
    
    struct Bounding_box Box_one;
    struct Bounding_box Aggregate;
    
     // Setting a comparison box to pass in to add boxes.
    
    // Read first shape
    scanf("%c",&shape);
    if(shape == 'L')
    {
        scanf("%d %d %d %d",&x1, &y1, &x2, &y2);
        A.tag = 1;
        A.Shape_choice.Line_func.x_origin = x1;
        A.Shape_choice.Line_func.y_origin = y1;
        A.Shape_choice.Line_func.x_end = x2;
        A.Shape_choice.Line_func.y_end = y2;
        Box_one = create_shape_box(A);
        Aggregate.lower_left_x_coordinate = Box_one.lower_left_x_coordinate;
        Aggregate.lower_left_y_coordinate = Box_one.lower_left_y_coordinate;
        Aggregate.upper_right_x_coordinate = Box_one.upper_right_x_coordinate;
        Aggregate.upper_right_y_coordinate = Box_one.upper_right_y_coordinate;
//        Aggregate = add_boxes(Box_one,Box_one);
    }
    else if (shape == 'C')
    {
        scanf("%d %d %d",&x1, &y1, &x2);
        A.tag = 0;
        A.Shape_choice.Circle_func.x_center = x1;
        A.Shape_choice.Circle_func.y_center = y1;
        A.Shape_choice.Circle_func.radius = x2;
        Box_one = create_shape_box(A);
        Aggregate.lower_left_x_coordinate = Box_one.lower_left_x_coordinate;
        Aggregate.lower_left_y_coordinate = Box_one.lower_left_y_coordinate;
        Aggregate.upper_right_x_coordinate = Box_one.upper_right_x_coordinate;
        Aggregate.upper_right_y_coordinate = Box_one.upper_right_y_coordinate;
//        Aggregate = add_boxes(Box_one,Box_one);
    }
    
    while(1)
    {
        scanf("%c", &shape);
        if(shape == 'L')
        {
            scanf("%d",&x1);
            scanf("%d",&y1);
            scanf("%d",&x2);
            scanf("%d",&y2);
            A.tag = 1;
            A.Shape_choice.Line_func.x_origin = x1;
            A.Shape_choice.Line_func.y_origin = y1;
            A.Shape_choice.Line_func.x_end = x2;
            A.Shape_choice.Line_func.y_end = y2;
            Box_one = create_shape_box(A);
            Aggregate = add_boxes(Box_one, Aggregate);
        }
        else if (shape == 'C')
        {
            scanf("%d",&x1);
            scanf("%d",&y1);
            scanf("%d",&x2);
            A.tag = 0;
            A.Shape_choice.Circle_func.x_center = x1;
            A.Shape_choice.Circle_func.y_center = y1;
            A.Shape_choice.Circle_func.radius = x2;
            Box_one = create_shape_box(A);
            Aggregate = add_boxes(Box_one, Aggregate);
        }
        if(shape == '\0'||shape == '\n')
        {
            break;
        }
    }
    print_box(Aggregate);
    return 0;
}




