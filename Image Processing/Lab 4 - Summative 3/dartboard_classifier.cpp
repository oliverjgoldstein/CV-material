#include <stdlib.h>
#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/opencv.hpp"
#include "opencv2/core/core.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <iostream>
#include <stdio.h>
#include <string.h>
#include <string>

// These are not parameters:
#define MAX_IMAGE_NAME_LENGTH      400 // This should not be touched.
#define COLOUR                     1
#define BW                         0
#define PI                         3.14159265358
#define MAX_DOUBLE                 1000000.0
#define SMALLEST_AREA              10000000
#define MAX_COMPARISON_NUMBER      1000000.0

// These are parameters:
#define SMALLEST_CIRCLE_WIDTH      4
#define LARGEST_CIRCLE_WIDTH       1
#define MAGNITUDE_THRESHOLD        120.0
#define SCALINGFACTOR              1.20
#define HOUGH_THRESHOLD            95
#define HOUGH_LINE_THRESHOLD       40
#define INTERSECTION_MINIMUM       55
#define X_CIRCLE_PROXIMITY         130
#define Y_CIRCLE_PROXIMITY         130
#define DIMINISH                   0.58
#define VIOLA_SCALING_AMOUNT       1.1
#define GROUND_TRUTH_THRESHOLD     0.45
#define ELLIPSE_FAC                2.0
#define ELLIPSE_HIGH               170
#define ELLIPSE_LOW                10
#define REDUCTION_PARAMETER        7

#define PERCENT_OF_BOARD_IS_CIRCLE 0.32
#define PERCENT_OF_CIRCLE_IN_BOARD 0.65

#define WEIGHTINTERSECTION         17
#define WEIGHTAREA                 5
#define WEIGHT_CENTRED             3
#define FINAL_SCORE_THRESHOLD      5.5
#define I_COUNT_THRESHOLD          4
#define IS_DIST_CLOSE              2.0
#define DIST_FAR                   0.5

typedef struct {

    int img_id;
    int number_of_db;
    int *x_end;
    int *y_end;
    int *x_start;
    int *y_start;

} ground_truth;

using namespace cv;

typedef struct {
    int x_coord;
    int y_coord;
    int radius;
} circle_object;

typedef struct {
    int x_coord;
    int y_coord;
    int number_of_intersections;
} intersection;


typedef struct {

    float           area_with_circle;
    circle_object  *circle_covering;
    intersection   *intersections;
    int             index_of_highest_intersection;
    int             is_candidate;
    int             i_count;
    float           score;

} box;

String cascade_name = "NegativeImages/dartcascade/cascade.xml";
CascadeClassifier cascade;

// using namespace std;
void convolve                               ( Mat &original_image, Mat &kernel, Mat &output_image );
void convolveEdgeXDerivative                ( Mat &input_image, Mat &output_image );
void convolveEdgeYDerivative                ( Mat &input_image, Mat &output_image );
void sobel                                  ( Mat &input_image, Mat &x_derivative, Mat &y_derivative, Mat &magnitude, Mat &gradient_direction_image, Mat &magnitude_thresholded);
void calculateMagnitudeImage                ( Mat &x_derivative, Mat &y_derivative, Mat &magnitude );
void calculateDirectionOfGradient           ( Mat &x_derivative, Mat &y_derivative, Mat &direction );
void thresholdFloatImage                    ( Mat &input_image, float threshold, Mat &output_image );
circle_object *hough_circles                ( Mat &thresholded_magnitude_image, Mat &gradient_direction_image, Mat &input_image, int min_radius, int max_radius, int iteration, int *number_of_circles );
void scaleFloatImage                        ( Mat &image, Mat &scaled_image );
void findLargestCircleRadius                ( std::vector<Rect> dart_squares, int* radii_tuple );
void deleteOutsideOfRectangle               ( std::vector<Rect> dart_squares, Mat &input_image, Mat &deleted_non_darts, float factor );
void wr_image                               ( const char *folder_name, Mat &image, int img_id );
void get_truth                              ( ground_truth *darts );
void free_darts                             ( ground_truth *darts );
intersection *houghSpaceLineIntersection    ( Mat &magnitude_image, int *intersection_count );
float findLargestMatrixValue                ( Mat &image );
float findSmallestMatrixValue               ( Mat &image );
float circleDartBoard                       ( std::vector<Rect> dart_squares, int iteration, circle_object *circles, int iteration_circ, Mat &image, int flag );
float f1_Score                              ( std::vector<Rect> viola_squares, Mat &input_image, ground_truth *darts, int *TP, box *dart_boxes, int *number_of_size);
Mat divide                                  ( std::vector<Rect> dart_squares , Mat input_image, int iteration);
Mat rd_image                                ( const char *folder_name, int img_id, int col );
std::vector<Rect> violaJonesDarts           ( Mat &frame );
int isCentreInCircle                        ( std::vector<Rect> dart_squares, int iteration, circle_object *circles, int iteration_circ, Mat &input_image );
float euclideanDistance                     ( int x_coord_one, int x_coord_two, int y_coord_one, int y_coord_two );
int getHighestIntersectionCount             ( intersection* intersections, int intersection_count, int *index );
int is_removable                            ( circle_object *individual_circle, std::vector<Rect> dart_squares );



int main( int argc, char** argv ) {
    
    if( !cascade.load( cascade_name ) ) { 
        printf("--(!)Error loading\n"); 
        return -1; 
    }

    std::vector<Rect> dart_squares;

    ground_truth darts[16];
    int number_of_circles[1];
    int radii_tuple[2];
    int intersection_count[1];
    int index[1];

    int size = 0;
    int total_TP = 0;
    int total_box = 0;
    int total_dart = 0;
    int number1 = 0;

    get_truth( darts );

    // for(int d_id = 0; d_id < 16; d_id++) {
        // int num = darts[d_id].number_of_db;
        Mat image                = imread( argv[1], 1 );
        Mat img                  = imread( argv[1], CV_LOAD_IMAGE_GRAYSCALE );

        Mat x_derivative            ( img.rows, img.cols, CV_32F );
        Mat y_derivative            ( img.rows, img.cols, CV_32F );
        Mat magnitude_image         ( img.rows, img.cols, CV_32F );
        Mat gradient_direction_image( img.rows, img.cols, CV_32F );
        Mat magnitude_thresholded   ( img.rows, img.cols, CV_32F );
        Mat deleted_non_darts       ( img.rows, img.cols, CV_32F );

        sobel( img, x_derivative, y_derivative, magnitude_image, gradient_direction_image, magnitude_thresholded );

        dart_squares             = violaJonesDarts( img );
        findLargestCircleRadius( dart_squares, radii_tuple );
        int min_radius           = radii_tuple[0];
        int max_radius           = radii_tuple[1];
        circle_object *circles   = hough_circles( magnitude_thresholded, gradient_direction_image, img, min_radius, max_radius, 20000, number_of_circles );
        
        // Reset the circles coordinate back to the original image space.
        for(int i = 0; i < number_of_circles[0]; i++) {
            circles[i].x_coord -= max_radius;
            circles[i].y_coord -= max_radius;
        }




        box *dart_boxes = (box*)malloc(sizeof(box)*dart_squares.size());

        for(int i = 0; i < dart_squares.size(); i++) {
            dart_boxes[i].is_candidate                  = 0;
            dart_boxes[i].i_count                       = 0;
            dart_boxes[i].index_of_highest_intersection = 0;
            dart_boxes[i].score                         = 0.2f;
            dart_boxes[i].area_with_circle              = 0;
        }

        for( unsigned int t = 0; t < dart_squares.size(); t++ ) {
            Mat input_box                               = divide( dart_squares, img, t );
            Mat mag_thresh_box                          = divide( dart_squares, magnitude_thresholded, t );
            Mat grad_dir_box                            = divide( dart_squares, gradient_direction_image, t );
            intersection *intersections                 = houghSpaceLineIntersection( mag_thresh_box, intersection_count );
            index[0]                                    = 0;
            int i_count                                 = getHighestIntersectionCount(intersections, intersection_count[0], index);
            dart_boxes[t].i_count                       = i_count;
            dart_boxes[t].index_of_highest_intersection = index[0];
            dart_boxes[t].intersections                 = intersections;
        }

        /* 
            
            The following is the scoring section:
            
        */

        int c_index = -1;
        for(int i = 0; i < dart_squares.size(); i++) {
            float highest_so_far = 0.0f;
            for(int c = 0; c < number_of_circles[0]; c++) {

                float temp_score = 0.0f;

                float a1         = circleDartBoard(dart_squares, i, circles, c, img, 1);
                float a2         = circleDartBoard(dart_squares, i, circles, c, img, 1);
                float a3         = circleDartBoard(dart_squares, i, circles, c, img, 0);
                int centred      = isCentreInCircle(dart_squares, i, circles, c, img);
     
                temp_score += WEIGHT_CENTRED * centred;

                if( a1 >= PERCENT_OF_BOARD_IS_CIRCLE
                    && a2 >= PERCENT_OF_CIRCLE_IN_BOARD) {
                    temp_score += temp_score * WEIGHTAREA * ((a1 + a2 + a3)/2);

                    if( temp_score > highest_so_far ) {
                        highest_so_far = temp_score;
                        c_index = c;
                    }
                }
            }

            if(c_index != -1) {
                dart_boxes[i].circle_covering = &circles[c_index];
                dart_squares[i].height = circles[c_index].radius * 3;
                dart_squares[i].width = circles[c_index].radius * 3;
                dart_squares[i].x     = (dart_squares[i].x + 4*(circles[c_index].x_coord - circles[c_index].radius))/5 - circles[c_index].radius/2;
                dart_squares[i].y     = (dart_squares[i].y + 4*(circles[c_index].y_coord - circles[c_index].radius))/5 - circles[c_index].radius/2;
            } else {
                dart_boxes[i].circle_covering = NULL;
            }

            dart_boxes[i].score += highest_so_far;

            if(dart_boxes[i].i_count >= I_COUNT_THRESHOLD) {
                dart_boxes[i].score += dart_boxes[i].score * (WEIGHTINTERSECTION * dart_boxes[i].i_count);
            }

            int box_centre_x = (dart_squares[i].x + (dart_squares[i].x + dart_squares[i].width))/2;
            int box_centre_y = (dart_squares[i].y + (dart_squares[i].y + dart_squares[i].height))/2;
            float distance   = 0.0f;

            if(c_index != -1) {
                distance = euclideanDistance( circles[c_index].x_coord, box_centre_x, circles[c_index].y_coord, box_centre_y );
            }

            if((distance/dart_squares[i].width) < 0.5) {
                dart_boxes[i].score += dart_boxes[i].score * IS_DIST_CLOSE;
            } else {
                dart_boxes[i].score += dart_boxes[i].score * DIST_FAR;
            }
        }

        for( unsigned int t = 0; t < number_of_circles[0]; t++ ) {
            int x = circles[t].x_coord;
            int y = circles[t].y_coord;
            // circle( image, Point( x,y ), circles[t].radius, Scalar( 200, 255, 0 ), 2, 8 );
        }

        for(int i = 0; i < dart_squares.size(); i++) {
            if( dart_boxes[i].i_count >= I_COUNT_THRESHOLD ) {

                int x = dart_boxes[i].intersections[dart_boxes[i].index_of_highest_intersection].x_coord + dart_squares[i].x;
                int y = dart_boxes[i].intersections[dart_boxes[i].index_of_highest_intersection].y_coord + dart_squares[i].y;

                // circle( image, Point( x,y ), 5.0, Scalar( 120, 255, 0 ), 4, 8 );
            }
        }

        // Let's now find the candidates:

        for(int i = 0; i < dart_squares.size(); i++) {
            if(dart_boxes[i].score > FINAL_SCORE_THRESHOLD) {
                dart_boxes[i].is_candidate = 1;
            }
        }

        // Here we merge the boxes that use the same circle.

        for(int i = 0; i < dart_squares.size(); i++) {
            if(dart_boxes[i].is_candidate == 1 && dart_boxes[i].circle_covering != NULL) {
                for(int j = 0; j < dart_squares.size(); j++) {
                    if(dart_boxes[j].is_candidate == 1 && i != j && dart_boxes[j].circle_covering != NULL) {
                        if(dart_boxes[j].circle_covering == dart_boxes[i].circle_covering) {
                            if(dart_boxes[j].score > dart_boxes[i].score) {
                                dart_boxes[i].is_candidate = 0;
                            } else {
                                dart_boxes[j].is_candidate = 0;
                            }
                        }
                    }
                }
            }
        }

        // Here we take boxes that have the same intersecting points, choose the one with the best score.


        // int *indices    = (int*)malloc(sizeof(int)*darts[0].number_of_db);
        // memset( indices, 0, sizeof(indices) );
        // int num_TP[1];
        // int number_of_size[1];
        // int index_we_need = -1;
        // for(int i = 0; i < 16; i++) {
        //     if(d_id == darts[i].img_id) {
        //         index_we_need = i;
        //     }
        // }

        // printf("F1 Score for image id %i: %f\n", 0, f1_Score(dart_squares, image, &darts[index_we_need], num_TP, dart_boxes, number_of_size));

        // size = num_TP[0];
        // number1 = number_of_size[0];

        // total_TP = total_TP+size;
        // total_box =  number1 + total_box;
        // total_dart = total_dart+num;




        for(int i = 0; i < dart_squares.size(); i++) {
            if(dart_boxes[i].is_candidate == 1) {
                rectangle(image, Point(dart_squares[i].x, dart_squares[i].y), Point(dart_squares[i].x + dart_squares[i].width, dart_squares[i].y + dart_squares[i].height), Scalar( 255, 140, 0 ), 2);
            }                
        }

        wr_image( "detected", image, -1 );

        free(dart_boxes);
        // free(indices);
        free(circles);
    // }

    // printf("\nTotal number of true positives: %i\n", total_TP);
    // printf("\nTotal number of predicted boxes: %i\n", total_box);
    // printf("\nTotal number of ground truths: %i\n", total_dart);
    // float overall_f1score = 2*total_TP;

    // overall_f1score = overall_f1score/(total_dart+total_box);

    // printf("F1 Score for entire image id %f\n", overall_f1score);

    free_darts( darts );
}

int getHighestIntersectionCount(intersection* intersections, int intersection_count, int *index) {

    int highest_so_far = 0;

    for( int i = 0; i < intersection_count; i++ ) {
        if( intersections[i].number_of_intersections > highest_so_far ) {
            highest_so_far = intersections[i].number_of_intersections;
            index[0]       = i;
        }
    }

    return highest_so_far;
}

int isCentreInCircle  ( std::vector<Rect> dart_squares, int iteration, circle_object *circles, int iteration_circ, Mat &input_image ) {

    if( circles[iteration_circ].x_coord <= dart_squares[iteration].x + dart_squares[iteration].width ) {
        if( circles[iteration_circ].x_coord >= dart_squares[iteration].x ) {
            if( circles[iteration_circ].y_coord <= dart_squares[iteration].y + dart_squares[iteration].height ) {
                if( circles[iteration_circ].y_coord >= dart_squares[iteration].y ) {
                    return 1;
                }
            }
        }
    }

    return 0;
}


float circleDartBoard  ( std::vector<Rect> dart_squares, int iteration, circle_object *circles, int iteration_circ, Mat &input_image, int flag ) {


    int radius = circles[iteration_circ].radius;
    int x_cent = circles[iteration_circ].x_coord;
    int y_cent = circles[iteration_circ].y_coord;

    Mat calculation(input_image.rows, input_image.cols, CV_8UC1);

    int upper_x = dart_squares[iteration].x + dart_squares[iteration].width;
    int upper_y = dart_squares[iteration].y + dart_squares[iteration].height;

    // Set the image to zero.
    for (int y = 0; y < calculation.rows; y++) {
        for (int x = 0; x < calculation.cols; x++) {
            calculation.at<uchar>(y,x) = 0;
        }
    }

    // Fill in the square.
    for(int y = dart_squares[iteration].y; y < upper_y; y++ ) {
        for(int x = dart_squares[iteration].x; x < upper_x; x++ ) {
            calculation.at<uchar>(y,x) = 1;
        }
    }

    int lower_y_circ = circles[iteration_circ].y_coord - radius;

    if(lower_y_circ < 0) {
        lower_y_circ = 0;
    }

    int upper_y_circ = circles[iteration_circ].y_coord + radius;

    if (upper_y_circ > upper_y) {
        upper_y_circ = upper_y;
    }

    int lower_x_circ = circles[iteration_circ].x_coord - radius;

    if(lower_x_circ < 0) {
        lower_x_circ = 0;
    }

    int upper_x_circ = circles[iteration_circ].x_coord + radius;

    if(upper_x_circ > upper_x) {
        upper_x_circ = upper_x;
    }

    // Fill in the circle making 1s in the squares 2s as you go.
    float potential_circle_count = 0.0f;
    float within_circle_count    = 0.0f;
    for(int y = lower_y_circ; y < upper_y_circ; y++ ) {
        for(int x = lower_x_circ; x < upper_x_circ; x++ ) {
            if (euclideanDistance(x, x_cent, y, y_cent) <= radius) {
                potential_circle_count += 1.0f;
                if(calculation.at<uchar>(y,x) == 1) {
                    within_circle_count += 1.0f;
                    calculation.at<uchar>(y,x) = 2;
                }
            }
        }
    }

    // Let's find the potential total circle if the flag is one.

    // find the proportion of 2s over 1s.

    float circle_count = 0.0f;
    float square_count = 0.0f;

    for(int y = dart_squares[iteration].y; y < upper_y; y++ ) {
        for(int x = dart_squares[iteration].x; x < upper_x; x++ ) {
            if(calculation.at<uchar>(y,x) == 2) {
                circle_count+=1.0f;
            }
            square_count+=1.0f;
        }
    }


    if(flag == 0) {
        if(square_count == 0) {
            return 0.0f;
        }
        return circle_count/square_count;
    } else {
        if(within_circle_count == 0) {
            return 0.0f;
        }
        return within_circle_count / potential_circle_count;
    }
}

float euclideanDistance( int x_coord_one, int x_coord_two, int y_coord_one, int y_coord_two ) {
    float x = (x_coord_two-x_coord_one);
    float y = (y_coord_two-y_coord_one);
    if(((x*x) + (y*y)) <= 0 ) {
        return 0;
    } else {
        return sqrt((x*x) + (y*y));
    }
}

Mat divide( std::vector<Rect> dart_squares, Mat input_image, int iteration) {
    Mat roi = input_image( Rect(dart_squares[iteration].x,dart_squares[iteration].y,dart_squares[iteration].width,dart_squares[iteration].height) );
    return roi;
}

void wr_image( const char *folder_name, Mat &image, int img_id ) {

    char fn[MAX_IMAGE_NAME_LENGTH];
    memset(  fn, 0, sizeof(fn) );
    strcat(  fn, folder_name );
    if(img_id != -1) {
        sprintf( fn + strlen(fn), "%i", img_id );
    }
    strcat(  fn, ".jpg");
    imwrite(fn, image);

}

Mat rd_image( const char *folder_name, int img_id, int col ) {

    char fn[MAX_IMAGE_NAME_LENGTH];
    memset(  fn, 0, sizeof(fn) );
    strcat(  fn, folder_name );
    sprintf( fn + strlen(fn), "%i", img_id );
    strcat(  fn, ".jpg" );

    Mat image;

    if( col == BW ) {
        image = imread( fn, CV_LOAD_IMAGE_GRAYSCALE );
    } else {
        image = imread( fn, 1 );
    }

    return image;
}

float f1_Score(std::vector<Rect> viola_squares, Mat &input_image, ground_truth *darts, int *numb_TP, box *dart_boxes , int *number_of_size) {
    
    int num             = darts->number_of_db;
    double f1_score     = 0;
    float T             = 0.0f;
    float TP              = 0;
    float number1         = 0;
    float best_ratio    = 0.0f;
    float number_of_bestbox = 0;

    for(unsigned int i = 0; i < num; i++) {

        int x_end    = darts->x_end[i];
        int x_start  = darts->x_start[i];
        int y_end    = darts->y_end[i];
        int y_start  = darts->y_start[i];
            number1 = 0;
        for( int j = 0; j < viola_squares.size(); j++) {


            if(dart_boxes[j].is_candidate == 1) {

                number1 = number1+1;

                int endx = max(x_end,viola_squares[j].x + viola_squares[j].width);  
                int startx = min(x_start,viola_squares[j].x);  
                int width = x_end-x_start+viola_squares[j].width-(endx-startx);

                int endy = max(y_end,viola_squares[j].y + viola_squares[j].height);  
                int starty = min(y_start,viola_squares[j].y);  
                int height = y_end-y_start+viola_squares[j].height-(endy-starty);

                float ratio_threshold_1 = 0.35f;
                float ratio_threshold_2 = 0.15f;
                float box_area = viola_squares[j].width * viola_squares[j].height;

                float area = (y_end - y_start)*(x_end - x_start);
                float interact_area = width*height;
                float ratio_1 = interact_area/area;
                float ratio_2 = interact_area/box_area;

                if(width > 0 && height > 0) {

                        // printf("ratio: %f\n", ratio);
                        if(ratio_1 >= ratio_threshold_1 && ratio_2 > ratio_threshold_2) {
                            T = T+1;
                            if (ratio_1 > best_ratio) {
                                best_ratio = ratio_1;
                                number_of_bestbox = j;
                            }
                        }
                    }
                }
                
                best_ratio = 0;

                if( T != 0){
                    TP = TP+1;
                    T = 0;                   
                }

                }
            }
        if(TP == 0) {
            f1_score = 0;
        } else {
            float P = 0;
            float R = 0;
            printf("TP: %f\n", TP);
             printf("number1: %f\n", number1);
                numb_TP[0] = TP;
                number_of_size[0] = number1;

            P = TP/number1;

            R = TP/num;
            f1_score = 2*R*P/(R+P);
            printf("R: %f \n",R);
            printf("P: %f \n",P);

        }

    return f1_score;
}


intersection *houghSpaceLineIntersection( Mat &magnitude_image, int *intersection_count ) {

    Mat hough_space_lines( magnitude_image.rows, magnitude_image.cols, CV_8UC1 );
    Mat color_edge;
    scaleFloatImage(magnitude_image, hough_space_lines);
 
    threshold(hough_space_lines, hough_space_lines, (float)HOUGH_LINE_THRESHOLD, 255.0, THRESH_BINARY);
    Mat blank_space(hough_space_lines.rows,hough_space_lines.cols,CV_8UC3,  Scalar(0,0,0));
    Mat blank_space_use(hough_space_lines.rows,hough_space_lines.cols,CV_8UC1,Scalar(0));
   
    cvtColor( hough_space_lines, color_edge, CV_GRAY2BGR );
 
    vector<Vec4i> lines;
    HoughLinesP(hough_space_lines, lines, 1, CV_PI/180, INTERSECTION_MINIMUM, 30, 18 );

    int i_count = 0;
    for( size_t i = 0; i < lines.size(); i++ ) {
        Vec4i l = lines[i];
        line( color_edge , Point(l[0], l[1]), Point(l[2], l[3]), Scalar(0,0,255), 1);
        line( blank_space, Point(l[0], l[1]), Point(l[2], l[3]), Scalar(0,0,255), 1);

        for(int blank_y = 0;blank_y < hough_space_lines.rows ;blank_y++){
            for(int blank_x = 0;blank_x < hough_space_lines.cols ;blank_x++){
                if(blank_space.at<Vec3b>(blank_y,blank_x)[2] == 255) {

                    blank_space_use.at<uchar>(blank_y,blank_x) = blank_space_use.at<uchar>(blank_y,blank_x) + 1;
                    blank_space.at<Vec3b>(blank_y,blank_x)[2] = 0;
                    i_count++;
                }
            }
        }
    }

    intersection *intersections = (intersection*)malloc(sizeof(intersection)*i_count);
 
    intersection_count[0] = 0;

    for(int blank_y = 0;blank_y < blank_space_use.rows ;blank_y++){
        for(int blank_x = 0;blank_x < blank_space_use.cols ;blank_x++){
            if (blank_space_use.at<uchar>(blank_y,blank_x) > 1) {
               intersections[intersection_count[0]].x_coord                 = blank_x;
               intersections[intersection_count[0]].y_coord                 = blank_y;
               intersections[intersection_count[0]].number_of_intersections = blank_space_use.at<uchar>(blank_y,blank_x);
               intersection_count[0] += 1;
            }
        }
    }

    return intersections;
}


void findLargestCircleRadius(std::vector<Rect> dart_squares, int* radii_tuple) {

    // The minimum is the max(width, height)/4 of the box with the smallest area.
    // The maximum is the max(width, height) of the box with the largest area.

    // First calculate the average square size:
    int average_square_width  = 0;
    int average_square_height = 0;

    for (unsigned int d = 0; d < dart_squares.size(); d++) {
        average_square_width  += dart_squares[d].width;
        average_square_height += dart_squares[d].height;
    }

    average_square_width  /= dart_squares.size();
    average_square_height /= dart_squares.size();

    radii_tuple[0] = (average_square_width + average_square_height)/8; // minimum value
    radii_tuple[1] = (average_square_width + average_square_height)/2;        // Maximum value it votes for.
}



void sobel ( Mat &input_image, Mat &x_derivative, Mat &y_derivative, Mat &magnitude_image, Mat &gradient_direction_image, Mat &magnitude_thresholded ) {

    convolveEdgeXDerivative( input_image, x_derivative );
    convolveEdgeYDerivative( input_image, y_derivative );
    calculateMagnitudeImage( x_derivative, y_derivative, magnitude_image );
    calculateDirectionOfGradient( x_derivative, y_derivative, gradient_direction_image );
    thresholdFloatImage( magnitude_image, MAGNITUDE_THRESHOLD, magnitude_thresholded );

    Mat scaled_x_derivative          ( x_derivative.rows, x_derivative.cols, CV_8UC1 );
    Mat scaled_y_derivative          ( y_derivative.rows, y_derivative.cols, CV_8UC1 );
    Mat scaled_magnitude             ( magnitude_image.rows, magnitude_image.cols, CV_8UC1 );
    Mat scaled_gradient_direction    ( gradient_direction_image.rows, gradient_direction_image.cols, CV_8UC1 );
    Mat scaled_magnitude_thresholded ( magnitude_thresholded.rows, magnitude_thresholded.cols, CV_8UC1 );

    scaleFloatImage( x_derivative, scaled_x_derivative );
    scaleFloatImage( y_derivative, scaled_y_derivative );
    scaleFloatImage( magnitude_image, scaled_magnitude );
    scaleFloatImage( gradient_direction_image, scaled_gradient_direction );
    scaleFloatImage( magnitude_thresholded, scaled_magnitude_thresholded );

    // imwrite("OutputImages/3. x_derivative.jpg", scaled_x_derivative);
    // imwrite("OutputImages/4. y_derivative.jpg", scaled_y_derivative);
    // imwrite("OutputImages/5. magnitude_image.jpg", scaled_magnitude);
    // imwrite("OutputImages/6. gradient_direction_image.jpg", scaled_gradient_direction);
    // imwrite("OutputImages/7. magnitude_thresholded.jpg", scaled_magnitude_thresholded);
}


circle_object* hough_circles( Mat &thresholded_magnitude_image, Mat &gradient_direction_image, Mat &input_image, int min_radius, int max_radius, int iteration, int *number_of_true_circles ) {

    int extend = 2 * max_radius;
    int row    = (thresholded_magnitude_image.rows + extend);
    int col    = (thresholded_magnitude_image.cols + extend);

    int dimensions[3] = { row, col, (max_radius - min_radius) };
    Mat hough_space( 3, dimensions, CV_32S );

    for( int y = 0; y < row; y++ ) {
        for( int x = 0; x < col; x++ ) {
            for( int radius = 0; radius < (max_radius - min_radius); radius++ ) {
                hough_space.at<int>(y,x,radius) = 0;
            }
        }
    }

    // Loop through image and calculate votes for a circle.


    for( int y = max_radius; y < row - max_radius; y++ ) {
        for( int x = max_radius; x < col - max_radius; x++ ) {

            int y_index = y - max_radius;
            int x_index = x - max_radius;

            if( thresholded_magnitude_image.at<float>(y_index,x_index) > 0.0f ) {

                // Go through all possible circles and increment the circles centre.

                for ( int radius = 0; radius < (max_radius - min_radius); radius++ ) {

                    float radians_gradient = gradient_direction_image.at<float>(y_index,x_index);
                    float inverse_gradient = PI + radians_gradient;
                    
                    int x_centre_one = x + (int)( (radius + min_radius) * cos (radians_gradient));
                    int x_centre_two = x + (int)( (radius + min_radius) * cos (inverse_gradient));
                    int y_centre_one = y + (int)( (radius + min_radius) * sin (radians_gradient));
                    int y_centre_two = y + (int)( (radius + min_radius) * sin (inverse_gradient));

                    hough_space.at<int>(y_centre_one,x_centre_one,radius) += 1;
                    hough_space.at<int>(y_centre_two,x_centre_two,radius) += 1;

                    if(thresholded_magnitude_image.at<float>(y_index,x_index) > 200) {
                        hough_space.at<int>(y_centre_one,x_centre_one,radius) += 1;
                        hough_space.at<int>(y_centre_two,x_centre_two,radius) += 1;
                    }
                }
            }
        }
    }

    // If less than 0 then recalculate.

    // Loop through image and calculate votes for an oval.

    for( int y = max_radius; y < row - max_radius; y++ ) {
        for( int x = max_radius; x < col - max_radius; x++ ) {

            int y_index = y - max_radius;
            int x_index = x - max_radius;

            if( thresholded_magnitude_image.at<float>(y_index,x_index) > 0.0f ) {

                // Go through all possible circles and increment the circles centre.

                for ( int radius = 0; radius < (max_radius - min_radius); radius++ ) {

                    float radians_gradient = gradient_direction_image.at<float>(y_index,x_index);
                    float inverse_gradient = PI + radians_gradient;

                    float degrees = radians_gradient * (180/PI);

                    int x_centre_one;   
                    int y_centre_one;
                    int x_centre_two;
                    int y_centre_two;

                    if( abs(degrees) < ELLIPSE_LOW  ) {
                        x_centre_one = x + (int)( ((float)radius/ELLIPSE_FAC + min_radius) * cos (radians_gradient));
                        y_centre_one = y + (int)( ((float)radius/ELLIPSE_FAC + min_radius) * sin (radians_gradient));
                    } else {
                        x_centre_one = x + (int)( ((float)radius + min_radius) * cos (radians_gradient));
                        y_centre_one = y + (int)( ((float)radius + min_radius) * sin (radians_gradient));
                    }

                    if( abs(degrees) > ELLIPSE_HIGH  ) {
                        x_centre_two = x + (int)( ((float)radius*ELLIPSE_FAC + min_radius) * cos (inverse_gradient));
                        y_centre_two = y + (int)( ((float)radius*ELLIPSE_FAC + min_radius) * sin (inverse_gradient));

                        if(x_centre_two > col) {
                            x_centre_two = col;
                            break;
                        }

                        if(y_centre_two > row) {
                            y_centre_two = row;
                            break;
                        }

                        if(y_centre_two < 0) {
                            y_centre_two = 0;
                            break;
                        }

                    } else {
                        x_centre_two = x + (int)( ((float)radius + min_radius) * cos (inverse_gradient));
                        y_centre_two = y + (int)( ((float)radius + min_radius) * sin (inverse_gradient));
                    }

                    hough_space.at<int>(y_centre_one,x_centre_one,radius) += 1;
                    hough_space.at<int>(y_centre_two,x_centre_two,radius) += 1;
                }
            }
        }
    }

    Mat flattened_hough_space( row, col, CV_32F );
    Mat scaled_flattened_hough_space( row, col, CV_8UC1 );

    for( int y = 0; y < row; y++ ) {
        for( int x = 0; x < col; x++ ) {
            
            float count = 0;
            
            for( int r = 0; r < (max_radius-min_radius); r++ ) {
                count += hough_space.at<int>(y,x,r);
            }

            flattened_hough_space.at<float>(y,x) = count;
        }
    }


    // Here we find the top value:

    float top_value = 0.0f;

    for( int y = 0; y < flattened_hough_space.rows; y++ ) {
        for ( int x = 0; x < flattened_hough_space.cols; x++ ) {
            if( flattened_hough_space.at<float>(y,x) > top_value ) {
                top_value = flattened_hough_space.at<float>(y,x);
            }
        }
    }

    for( int y = 0; y < flattened_hough_space.rows; y++ ) {
        for ( int x = 0; x < flattened_hough_space.cols; x++ ) {
            if( flattened_hough_space.at<float>(y,x) < DIMINISH*top_value ) {
                flattened_hough_space.at<float>(y,x) = flattened_hough_space.at<float>(y,x)/REDUCTION_PARAMETER;
            }
        }
    }

    // Here the hough space is flattened such that brightness represents a vote.

    Mat thresholded_flattened_hough_space( row, col, CV_32F );
    Mat scaled_thresholded_flattened_hough_space( row, col, CV_8UC1 );
    scaleFloatImage( flattened_hough_space, scaled_flattened_hough_space );

    // Here we threshold the hough space:

    thresholdFloatImage( flattened_hough_space, HOUGH_THRESHOLD, thresholded_flattened_hough_space );
    scaleFloatImage( thresholded_flattened_hough_space, scaled_thresholded_flattened_hough_space );


    // wr_image( "DartBoxes/HOUGH               8:", scaled_flattened_hough_space, iteration );
    // wr_image( "DartBoxes/THRESHOLDED_HOUGH   9:", scaled_thresholded_flattened_hough_space, iteration );




    // Lets make a list of the potential circle centres.
    // 1. How many candidates are there?

    int number_of_candidates = 0;

    for( int y = 0; y < thresholded_flattened_hough_space.rows; y++ ) {
        for ( int x = 0; x < thresholded_flattened_hough_space.cols; x++ ) {
            if( thresholded_flattened_hough_space.at<float>(y,x) > 0 ) {
                // Consider as a candidate and add to count variable:
                number_of_candidates++;
            }
        }
    }

    // 2. Let's get the coordinates in the thresholded image.
    
    int *y_coordinates   = (int*)malloc(sizeof(int)*number_of_candidates);
    int *x_coordinates   = (int*)malloc(sizeof(int)*number_of_candidates);
    
    int possible_circle_object_count = 0;

    for( int y = 0; y < thresholded_flattened_hough_space.rows; y++ ) {
        for ( int x = 0; x < thresholded_flattened_hough_space.cols; x++ ) {
            if( thresholded_flattened_hough_space.at<float>(y,x) > 0 ) {
                // Consider as a potential circle centre:
                y_coordinates[possible_circle_object_count] = y;
                x_coordinates[possible_circle_object_count] = x;
                possible_circle_object_count++;
            }
        }
    }


    circle_object *circles  = (circle_object*)malloc(sizeof(circle_object)*possible_circle_object_count);;

    // Initialise the circle_object structure:

    for( int num = 0; num < possible_circle_object_count; num++ ) {
        circles[num].x_coord = -1;
        circles[num].y_coord = -1;
        circles[num].radius  = -1;
    }




    // Let's go to each local minima in the thresholded region, find the most voted for pixel and find its most voted for radius.

    int x_proximity_threshold = X_CIRCLE_PROXIMITY; // Pixel count of the minima of the circles.
    int y_proximity_threshold = Y_CIRCLE_PROXIMITY;

    int number_of_circles = 0;

    for( int outer_index = 0; outer_index < number_of_candidates; outer_index++ ) {
        int current_index               = 0;
        int circle_object_found         = 0;
        int best_x                      = -1;
        int best_y                      = -1;
        int best_sum                    = 0;

        for( int inner_index = 0; inner_index < number_of_candidates; inner_index++ ) {
            int current_x = x_coordinates[current_index];
            int current_y = y_coordinates[current_index];

            
            // If there is at least some value greater than zero in the region.

            if ( thresholded_flattened_hough_space.at<float>(current_y, current_x) > 0 ) {

                circle_object_found = 1;

                // First initialise it here, any solution is better than -1.
                if( (best_x == -1) && (best_y == -1) ) {
                    best_x = current_x;
                    best_y = current_y;
                }

                // If it is within the proximity.

                if( ( abs(best_x - current_x) <= x_proximity_threshold ) && ( abs(best_y - current_y) <= y_proximity_threshold ) ) {

                    // If it is within the proximity and it is better!

                    if( best_sum < thresholded_flattened_hough_space.at<float>(current_y, current_x) ) {
                        best_y   = current_y;
                        best_x   = current_x;
                        best_sum = thresholded_flattened_hough_space.at<float>(current_y, current_x);
                    }

                    // Now we need to delete it, to stop it from coming up again.

                    thresholded_flattened_hough_space.at<float>(current_y, current_x) = 0;
                }
            }

            current_index++;
        }

        // Now we have our x and y position of our circle_object:
        // Let's reset everything and store the circles position.

        if(circle_object_found == 1) {
            circles[number_of_circles].x_coord = best_x;
            circles[number_of_circles].y_coord = best_y;
            number_of_circles++;
        }
    }


    // Now let's go through the circles and find the correct radius:
    for( int num = 0; num < number_of_circles; num++ ) {
        
        int likelihood = 0;
        int distance   = 0;

        for(int radius = 0; radius < (max_radius-min_radius); radius++ ) {
            if(hough_space.at<int>(circles[num].y_coord, circles[num].x_coord, radius ) > likelihood) {
                likelihood = hough_space.at<int>(circles[num].y_coord, circles[num].x_coord, radius );
                distance   = radius + min_radius;
            }
        }

        circles[num].radius = distance;
    }

    number_of_true_circles[0] = number_of_circles;
    // printf("\nNumber of circles found by hough transform: %i\n", number_of_circles);

    Mat input_image_with_circles( input_image.rows, input_image.cols, CV_8UC1 );
    input_image_with_circles = input_image.clone();
    
    scaleFloatImage( flattened_hough_space, scaled_thresholded_flattened_hough_space );

    Point circle_center;

    for( int num = 0; num < number_of_circles; num++ ) {
        
        // Write onto the hough space.
        
        circle_center.x = (double)circles[num].x_coord;
        circle_center.y = (double)circles[num].y_coord;
        circle(scaled_thresholded_flattened_hough_space, circle_center, circles[num].radius, Scalar( 255, 255, 255 ), 2);

        // Write the same thing onto the input image.
        
        circle_center.x = circles[num].x_coord-max_radius;
        circle_center.y = circles[num].y_coord-max_radius;
        circle(input_image_with_circles,                 circle_center, circles[num].radius, Scalar( 255, 255, 255 ), 2);
    }

    // wr_image( "DartBoxes/HOUGH_CIRCLES           10:", scaled_thresholded_flattened_hough_space, iteration );
    // wr_image( "DartBoxes/INPUT_CIRCLES           11:", input_image_with_circles, iteration );

    // Clean up:
    
    delete[] y_coordinates;
    delete[] x_coordinates;

    return circles;
}


void thresholdFloatImage( Mat &input_image, float threshold, Mat &output_image ) {
    for( int y = 0; y < input_image.rows; y++ ) {
        for( int x = 0; x < input_image.cols; x++ ) {
            if( input_image.at<float>(y,x) <= threshold ) {
                output_image.at<float>(y,x) = 0;
            } else {
                output_image.at<float>(y,x) = input_image.at<float>(y,x);
            }
        }
    }
}


std::vector<Rect> violaJonesDarts( Mat &frame ) {
    std::vector<Rect> dart_squares;
    
    Mat frame_gray = frame.clone();

    equalizeHist( frame_gray, frame_gray );

    // 2. Perform Viola-Jones Object Detection 
    cascade.detectMultiScale( frame_gray, dart_squares, VIOLA_SCALING_AMOUNT, 1, 0|CV_HAAR_SCALE_IMAGE, Size(50, 50), Size(500,500) );
    return dart_squares;
}




void calculateDirectionOfGradient ( Mat &x_derivative, Mat &y_derivative, Mat &gradient_image ) {
    for( int y = 0; y < gradient_image.rows; y++ ) {
        for ( int x = 0; x < gradient_image.cols; x++ ) {

            // Convert to double precision to conserve accuracy.

            float y_d                      = y_derivative.at<float>(y,x);
            float x_d                      = x_derivative.at<float>(y,x);
            float inverse_tan_degrees      = atan2((double)y_d,(double)x_d);
            gradient_image.at<float>(y, x) = inverse_tan_degrees;
        }
    }
}


void calculateMagnitudeImage( Mat &x_derivative, Mat &y_derivative, Mat &magnitude ) {
    // Set the magnitude value.
    for( int y = 0; y < y_derivative.rows; y++ ) {
        for( int x = 0; x < y_derivative.cols; x++ ) {
            magnitude.at<float>(y,x) = sqrt(x_derivative.at<float>(y,x)*x_derivative.at<float>(y,x) + y_derivative.at<float>(y,x)*y_derivative.at<float>(y,x));
        }
    }
}



float findLargestMatrixValue(Mat &image) {
    
    float maximum = -MAX_COMPARISON_NUMBER; // Some arbitrary number.

    for( int y = 0; y < image.rows; y++ ) {
        for( int x = 0; x < image.cols; x++ ) {
            if (maximum < image.at<float>(y,x)) {
                maximum = image.at<float>(y,x);
            }
        }
    }

    return maximum;
}



float findSmallestMatrixValue(Mat &image) {

    float minimum = MAX_COMPARISON_NUMBER; // Some arbitrary number.

    for( int y = 0; y < image.rows; y++ ) {
        for( int x = 0; x < image.cols; x++ ) {
            if (minimum > image.at<float>(y,x)) {
                minimum = image.at<float>(y,x);
            }
        }
    }

    return minimum;
}



void convolveEdgeXDerivative( Mat &input_image, Mat &output_image ) {
    
    Mat delta_x_kernel( 3, 3, CV_32S );
    
    // Create the kernel.
    short int kernel_width  = 3;
    short int kernel_height = 3; 
    int value               = 0;

    // Set the kernel values.
    for( int y = 0; y < kernel_height; y++ ) {
        for( int x = 0; x < kernel_width; x++ ) {

            if( x == 0 ) {
                value = -1;
            } else if ( x == 1 ) {
                value = 0;
            } else {
                value = 1;
            }

            delta_x_kernel.at<int>( y, x ) = value;
        }
    }

    // Now we convolve the output image using the input image as a scratch space.
    convolve(input_image, delta_x_kernel, output_image);
}



// This function visualises the image for float types.
void scaleFloatImage( Mat &image, Mat &scaled_image ) {

    float minimum = findSmallestMatrixValue(image);
    float maximum = findLargestMatrixValue(image);

    maximum += abs(minimum);

    // Scale to a 0-255 range.
    for( int y = 0; y < scaled_image.rows; y++ ) {
        for( int x = 0; x < scaled_image.cols; x++ ) {

            float image_value = image.at<float>(y,x);
            image_value      += abs(minimum);
            image_value       = ( 255.0f / maximum ) * image_value;

            scaled_image.at<uchar>(y, x) = (uchar)(image_value);
       }
    }
}



void convolveEdgeYDerivative( Mat &input_image, Mat &output_image ) {
    
    Mat delta_y_kernel( 3, 3, CV_32S );
    
    // Create the kernel.
    int kernel_width  = 3;
    int kernel_height = 3; 
    int value = 0;

    // Set the kernel values.
    for( int y = 0; y < kernel_height; y++ ) {
        for( int x = 0; x < kernel_width; x++ ) {

            if( y == 0 ) {
                value = -1;
            } else if ( y == 1 ) {
                value = 0;
            } else {
                value = 1;
            }

            delta_y_kernel.at<int>( y, x ) = value;
        }
    }

    // Now we convolve the output image using the input image as a scratch space.
    convolve(input_image, delta_y_kernel, output_image);
}


void convolve( Mat &original_image, Mat &kernel, Mat &output_image ) {

    // we need to create a padded version of the input
    // or there will be border effects
    int kernelRadiusX = ( kernel.size[0] - 1 ) / 2;
    int kernelRadiusY = ( kernel.size[1] - 1 ) / 2;

    // now we can do the convoltion
    for ( int i = 1; i < original_image.rows - 1; i++ )
    {
        for( int j = 1; j < original_image.cols - 1; j++ )
        {
            float sum = 0.0f;
            for( int m = -kernelRadiusX; m <= kernelRadiusX; m++ )
            {
                for( int n = -kernelRadiusY; n <= kernelRadiusY; n++ )
                {
                    // find the correct indices we are using
                    int imagex = i + m + kernelRadiusX;
                    int imagey = j + n + kernelRadiusY;
                    int kernelx = m + kernelRadiusX;
                    int kernely = n + kernelRadiusY;

                    // get the values from the padded image and the kernel
                    int imageval = original_image.at<uchar>( imagex, imagey );
                    int kernalval = kernel.at<int>( kernelx, kernely );

                    // do the multiplication
                    sum += imageval * kernalval;                            
                }
            }

            output_image.at<float>(i, j) = sum;


        }
    }
}

void free_darts(ground_truth *darts) {

    free(darts[0].x_start);
    free(darts[0].y_start);
    free(darts[0].x_end  );
    free(darts[0].y_end  );

    free(darts[1].x_start);
    free(darts[1].y_start);
    free(darts[1].x_end  );
    free(darts[1].y_end  );

    free(darts[2].x_start);
    free(darts[2].y_start);
    free(darts[2].x_end  );
    free(darts[2].y_end  );

    free(darts[3].x_start);
    free(darts[3].y_start);
    free(darts[3].x_end  );
    free(darts[3].y_end  );

    free(darts[4].x_start);
    free(darts[4].y_start);
    free(darts[4].x_end  );
    free(darts[4].y_end  );

    free(darts[5].x_start);
    free(darts[5].y_start);
    free(darts[5].x_end  );
    free(darts[5].y_end  );

    free(darts[6].x_start);
    free(darts[6].y_start);
    free(darts[6].x_end  );
    free(darts[6].y_end  );

    free(darts[7].x_start);
    free(darts[7].y_start);
    free(darts[7].x_end  );
    free(darts[7].y_end  );

    free(darts[8].x_start);
    free(darts[8].y_start);
    free(darts[8].x_end  );
    free(darts[8].y_end  );

    free(darts[9].x_start);
    free(darts[9].y_start);
    free(darts[9].x_end  );
    free(darts[9].y_end  );

    free(darts[10].x_start);
    free(darts[10].y_start);
    free(darts[10].x_end  );
    free(darts[10].y_end  );

    free(darts[11].x_start);
    free(darts[11].y_start);
    free(darts[11].x_end  );
    free(darts[11].y_end  );

    free(darts[12].x_start);
    free(darts[12].y_start);
    free(darts[12].x_end  );
    free(darts[12].y_end  );

    free(darts[13].x_start);
    free(darts[13].y_start);
    free(darts[13].x_end  );
    free(darts[13].y_end  );

    free(darts[14].x_start);
    free(darts[14].y_start);
    free(darts[14].x_end  );
    free(darts[14].y_end  );

    free(darts[15].x_start);
    free(darts[15].y_start);
    free(darts[15].x_end  );
    free(darts[15].y_end  );
}

void get_truth(ground_truth *darts) {
    darts[0].img_id         = 14;
    darts[0].number_of_db   = 2;
    darts[0].x_start        = (int*)malloc(sizeof(int)*darts[0].number_of_db);
    darts[0].y_start        = (int*)malloc(sizeof(int)*darts[0].number_of_db);
    darts[0].x_end          = (int*)malloc(sizeof(int)*darts[0].number_of_db);
    darts[0].y_end          = (int*)malloc(sizeof(int)*darts[0].number_of_db);
    darts[0].x_start[0]     = 103;
    darts[0].y_start[0]     = 90;
    darts[0].x_end[0]       = 252;
    darts[0].y_end[0]       = 236;
    darts[0].x_start[1]     = 970;
    darts[0].y_start[1]     = 80;
    darts[0].x_end[1]       = 1131;
    darts[0].y_end[1]       = 225;
    darts[1].img_id         = 0;
    darts[1].number_of_db   = 1;
    darts[1].x_start        = (int*)malloc(sizeof(int)*darts[1].number_of_db);
    darts[1].y_start        = (int*)malloc(sizeof(int)*darts[1].number_of_db);
    darts[1].x_end          = (int*)malloc(sizeof(int)*darts[1].number_of_db);
    darts[1].y_end          = (int*)malloc(sizeof(int)*darts[1].number_of_db);
    darts[1].x_start[0]     = 450;
    darts[1].y_start[0]     = 27;
    darts[1].x_end[0]       = 615;
    darts[1].y_end[0]       = 200;
    darts[2].img_id         = 1;
    darts[2].number_of_db   = 1;
    darts[2].x_start        = (int*)malloc(sizeof(int)*darts[2].number_of_db);
    darts[2].y_start        = (int*)malloc(sizeof(int)*darts[2].number_of_db);
    darts[2].x_end          = (int*)malloc(sizeof(int)*darts[2].number_of_db);
    darts[2].y_end          = (int*)malloc(sizeof(int)*darts[2].number_of_db);
    darts[2].x_start[0]     = 171;
    darts[2].y_start[0]     = 113;
    darts[2].x_end[0]       = 403;
    darts[2].y_end[0]       = 333;
    darts[3].img_id         = 2;
    darts[3].number_of_db   = 1;
    darts[3].x_start        = (int*)malloc(sizeof(int)*darts[3].number_of_db);
    darts[3].y_start        = (int*)malloc(sizeof(int)*darts[3].number_of_db);
    darts[3].x_end          = (int*)malloc(sizeof(int)*darts[3].number_of_db);
    darts[3].y_end          = (int*)malloc(sizeof(int)*darts[3].number_of_db);
    darts[3].x_start[0]     = 90;
    darts[3].y_start[0]     = 90;
    darts[3].x_end[0]       = 200;
    darts[3].y_end[0]       = 200;
    darts[4].img_id         = 3;
    darts[4].number_of_db   = 1;
    darts[4].x_start        = (int*)malloc(sizeof(int)*darts[4].number_of_db);
    darts[4].y_start        = (int*)malloc(sizeof(int)*darts[4].number_of_db);
    darts[4].x_end          = (int*)malloc(sizeof(int)*darts[4].number_of_db);
    darts[4].y_end          = (int*)malloc(sizeof(int)*darts[4].number_of_db);
    darts[4].x_start[0]     = 321;
    darts[4].y_start[0]     = 142;
    darts[4].x_end[0]       = 394;
    darts[4].y_end[0]       = 220;
    darts[5].img_id         = 4;
    darts[5].number_of_db   = 1;
    darts[5].x_start        = (int*)malloc(sizeof(int)*darts[5].number_of_db);
    darts[5].y_start        = (int*)malloc(sizeof(int)*darts[5].number_of_db);
    darts[5].x_end          = (int*)malloc(sizeof(int)*darts[5].number_of_db);
    darts[5].y_end          = (int*)malloc(sizeof(int)*darts[5].number_of_db);
    darts[5].x_start[0]     = 166;
    darts[5].y_start[0]     = 80;
    darts[5].x_end[0]       = 330;
    darts[5].y_end[0]       = 257;
    darts[6].img_id         = 5;
    darts[6].number_of_db   = 1;
    darts[6].x_start        = (int*)malloc(sizeof(int)*darts[6].number_of_db);
    darts[6].y_start        = (int*)malloc(sizeof(int)*darts[6].number_of_db);
    darts[6].x_end          = (int*)malloc(sizeof(int)*darts[6].number_of_db);
    darts[6].y_end          = (int*)malloc(sizeof(int)*darts[6].number_of_db);
    darts[6].x_start[0]     = 440;
    darts[6].y_start[0]     = 132;
    darts[6].x_end[0]       = 522;
    darts[6].y_end[0]       = 235;
    darts[7].img_id         = 6;
    darts[7].number_of_db   = 1;
    darts[7].x_start        = (int*)malloc(sizeof(int)*darts[7].number_of_db);
    darts[7].y_start        = (int*)malloc(sizeof(int)*darts[7].number_of_db);
    darts[7].x_end          = (int*)malloc(sizeof(int)*darts[7].number_of_db);
    darts[7].y_end          = (int*)malloc(sizeof(int)*darts[7].number_of_db);
    darts[7].x_start[0]      = 208;
    darts[7].y_start[0]      = 111;
    darts[7].x_end[0]        = 268;
    darts[7].y_end[0]        = 173;
    darts[8].img_id          = 7;
    darts[8].number_of_db    = 1;
    darts[8].x_start         = (int*)malloc(sizeof(int)*darts[8].number_of_db);
    darts[8].y_start         = (int*)malloc(sizeof(int)*darts[8].number_of_db);
    darts[8].x_end           = (int*)malloc(sizeof(int)*darts[8].number_of_db);
    darts[8].y_end           = (int*)malloc(sizeof(int)*darts[8].number_of_db);
    darts[8].x_start[0]      = 240;
    darts[8].y_start[0]      = 160;
    darts[8].x_end[0]        = 360;
    darts[8].y_end[0]        = 302;
    darts[9].img_id          = 8;
    darts[9].number_of_db    = 2;
    darts[9].x_start         = (int*)malloc(sizeof(int)*darts[9].number_of_db);
    darts[9].y_start         = (int*)malloc(sizeof(int)*darts[9].number_of_db);
    darts[9].x_end           = (int*)malloc(sizeof(int)*darts[9].number_of_db);
    darts[9].y_end           = (int*)malloc(sizeof(int)*darts[9].number_of_db);
    darts[9].x_start[0]      = 68;
    darts[9].y_start[0]      = 248;
    darts[9].x_end[0]        = 126;
    darts[9].y_end[0]        = 340;
    darts[9].x_start[1]      = 827;
    darts[9].y_start[1]      = 218;
    darts[9].x_end[1]        = 990;
    darts[9].y_end[1]        = 355;
    darts[10].img_id         = 9;
    darts[10].number_of_db   = 1;
    darts[10].x_start        = (int*)malloc(sizeof(int)*darts[10].number_of_db);
    darts[10].y_start        = (int*)malloc(sizeof(int)*darts[10].number_of_db);
    darts[10].x_end          = (int*)malloc(sizeof(int)*darts[10].number_of_db);
    darts[10].y_end          = (int*)malloc(sizeof(int)*darts[10].number_of_db);
    darts[10].x_start[0]     = 176;
    darts[10].y_start[0]     = 26;
    darts[10].x_end[0]       = 458;
    darts[10].y_end[0]       = 302;
    darts[11].img_id         = 10;
    darts[11].number_of_db   = 3;
    darts[11].x_start        = (int*)malloc(sizeof(int)*darts[11].number_of_db);
    darts[11].y_start        = (int*)malloc(sizeof(int)*darts[11].number_of_db);
    darts[11].x_end          = (int*)malloc(sizeof(int)*darts[11].number_of_db);
    darts[11].y_end          = (int*)malloc(sizeof(int)*darts[11].number_of_db);
    darts[11].x_start[0]     = 76;
    darts[11].y_start[0]     = 91;
    darts[11].x_end[0]       = 200;
    darts[11].y_end[0]       = 229;
    darts[11].x_start[1]     = 574;
    darts[11].y_start[1]     = 111;
    darts[11].x_end[1]       = 646;
    darts[11].y_end[1]       = 219;
    darts[11].x_start[2]     = 914;
    darts[11].y_start[2]     = 133;
    darts[11].x_end[2]       = 950;
    darts[11].y_end[2]       = 219;
    darts[12].img_id         = 11;
    darts[12].number_of_db   = 2;
    darts[12].x_start        = (int*)malloc(sizeof(int)*darts[12].number_of_db);
    darts[12].y_start        = (int*)malloc(sizeof(int)*darts[12].number_of_db);
    darts[12].x_end          = (int*)malloc(sizeof(int)*darts[12].number_of_db);
    darts[12].y_end          = (int*)malloc(sizeof(int)*darts[12].number_of_db);
    darts[12].x_start[0]     = 165;
    darts[12].y_start[0]     = 92;
    darts[12].x_end[0]       = 239;
    darts[12].y_end[0]       = 148;
    darts[12].x_start[1]     = 438;
    darts[12].y_start[1]     = 124;
    darts[12].x_end[1]       = 461;
    darts[12].y_end[1]       = 183;
    darts[13].img_id         = 12;
    darts[13].number_of_db   = 1;
    darts[13].x_start        = (int*)malloc(sizeof(int)*darts[13].number_of_db);
    darts[13].y_start        = (int*)malloc(sizeof(int)*darts[13].number_of_db);
    darts[13].x_end          = (int*)malloc(sizeof(int)*darts[13].number_of_db);
    darts[13].y_end          = (int*)malloc(sizeof(int)*darts[13].number_of_db);
    darts[13].x_start[0]     = 155;
    darts[13].y_start[0]     = 65;
    darts[13].x_end[0]       = 220;
    darts[13].y_end[0]       = 228;
    darts[14].img_id         = 13;
    darts[14].number_of_db   = 1;
    darts[14].x_start        = (int*)malloc(sizeof(int)*darts[14].number_of_db);
    darts[14].y_start        = (int*)malloc(sizeof(int)*darts[14].number_of_db);
    darts[14].x_end          = (int*)malloc(sizeof(int)*darts[14].number_of_db);
    darts[14].y_end          = (int*)malloc(sizeof(int)*darts[14].number_of_db);
    darts[14].x_start[0]     = 266;
    darts[14].y_start[0]     = 109;
    darts[14].x_end[0]       = 400;
    darts[14].y_end[0]       = 255;
    darts[15].img_id         = 15;
    darts[15].number_of_db   = 1;
    darts[15].x_start        = (int*)malloc(sizeof(int)*darts[15].number_of_db);
    darts[15].y_start        = (int*)malloc(sizeof(int)*darts[15].number_of_db);
    darts[15].x_end          = (int*)malloc(sizeof(int)*darts[15].number_of_db);
    darts[15].y_end          = (int*)malloc(sizeof(int)*darts[15].number_of_db);
    darts[15].x_start[0]     = 138;
    darts[15].y_start[0]     = 41;
    darts[15].x_end[0]       = 290;
    darts[15].y_end[0]       = 209;
}



















