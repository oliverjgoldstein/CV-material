#include <stdio.h>
#include <opencv2/opencv.hpp> 
#include <opencv/cv.h>        
#include <opencv/highgui.h>   
#include <opencv/cxcore.h>   
#include <cmath> 

#define PI 3.14159265358

using namespace cv;
void convolve                      ( Mat &original_image, Mat &kernel, Mat &output_image );
void convolveEdgeXDerivative       ( Mat &input_image, Mat &output_image );
void convolveEdgeYDerivative       ( Mat &input_image, Mat &output_image );
void convertImageToBlackAndWhite   ( Mat &image_to_convert );
void sobel                         ( Mat &input_image, Mat &x_derivative, Mat &y_derivative, Mat &magnitude, Mat &gradient_direction_image, Mat &magnitude_thresholded, float magnitude_threshold);
void calculateMagnitudeImage       ( Mat &x_derivative, Mat &y_derivative, Mat &magnitude );
void calculateDirectionOfGradient  ( Mat &x_derivative, Mat &y_derivative, Mat &direction );
void thresholdFloatImage           ( Mat &input_image, float threshold, Mat &output_image );
void hough                         ( Mat &thresholded_magnitude_image, Mat &gradient_direction_image, Mat &input_image, int hough_threshold, int min_radius, int max_radius );
void scaleFloatImage               ( Mat &image, Mat &scaled_image );
float findLargestMatrixValue       ( Mat &image );
float findSmallestMatrixValue      ( Mat &image );


typedef struct {
    int x_coord;
    int y_coord;
    int radius;
} coin;






int main( int argc, char** argv ) {

    // Floats will provide greater accuracy.
    // I am using 8 bit uchars for the arrays for the time being.

    Mat input_image = imread( "Images/coins1.png", 1 );
    convertImageToBlackAndWhite(input_image);

    Mat x_derivative            ( input_image.rows, input_image.cols, CV_32F );
    Mat y_derivative            ( input_image.rows, input_image.cols, CV_32F );
    Mat magnitude_image         ( input_image.rows, input_image.cols, CV_32F );
    Mat gradient_direction_image( input_image.rows, input_image.cols, CV_32F );
    Mat magnitude_thresholded   ( input_image.rows, input_image.cols, CV_32F );

    float magnitude_threshold = 115.0f;

    sobel( input_image, x_derivative, y_derivative, magnitude_image, gradient_direction_image, magnitude_thresholded, magnitude_threshold );

    int hough_threshold = 75;
    int min_radius      = 10;  //(in pixels)
    int max_radius      = 100;  //(in pixels)

    hough( magnitude_thresholded, gradient_direction_image, input_image, hough_threshold, min_radius, max_radius );
}


void sobel ( Mat &input_image, Mat &x_derivative, Mat &y_derivative, Mat &magnitude_image, Mat &gradient_direction_image, Mat &magnitude_thresholded, float magnitude_threshold ) {

    convolveEdgeXDerivative( input_image, x_derivative );
    convolveEdgeYDerivative( input_image, y_derivative );
    calculateMagnitudeImage( x_derivative, y_derivative, magnitude_image );
    calculateDirectionOfGradient( x_derivative, y_derivative, gradient_direction_image );
    thresholdFloatImage( magnitude_image, magnitude_threshold, magnitude_thresholded );

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

    imwrite("OutputImages/x_derivative.jpg", scaled_x_derivative);
    imwrite("OutputImages/y_derivative.jpg", scaled_y_derivative);
    imwrite("OutputImages/magnitude_image.jpg", scaled_magnitude);
    imwrite("OutputImages/gradient_direction_image.jpg", scaled_gradient_direction);
    imwrite("OutputImages/magnitude_thresholded.jpg", scaled_magnitude_thresholded);
}

void hough( Mat &thresholded_magnitude_image, Mat &gradient_direction_image, Mat &input_image, int threshold, int min_radius, int max_radius ) {

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

    // Loop through image and calculate votes.
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
                }
            }
        }
    }


    // Let's flatten the image and find the probable centres of the image.
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

    scaleFloatImage( flattened_hough_space, scaled_flattened_hough_space );

    Mat thresholded_flattened_hough_space( row, col, CV_32F );
    Mat scaled_thresholded_flattened_hough_space( row, col, CV_8UC1 );

    thresholdFloatImage( flattened_hough_space, threshold, thresholded_flattened_hough_space );
    scaleFloatImage( thresholded_flattened_hough_space, scaled_thresholded_flattened_hough_space );

    imwrite("OutputImages/flattened_hough_space.jpg", scaled_flattened_hough_space);
    imwrite("OutputImages/thresholded_flattened_hough_space.jpg", scaled_thresholded_flattened_hough_space);

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

    int *y_coordinates   = new int[number_of_candidates];
    int *x_coordinates   = new int[number_of_candidates];

    int possible_coin_count = 0;

    for( int y = 0; y < thresholded_flattened_hough_space.rows; y++ ) {
        for ( int x = 0; x < thresholded_flattened_hough_space.cols; x++ ) {
            if( thresholded_flattened_hough_space.at<float>(y,x) > 0 ) {
                // Consider as a potential circle centre:
                y_coordinates[possible_coin_count] = y;
                x_coordinates[possible_coin_count] = x;
                possible_coin_count++;
            }
        }
    }

    coin *coins  = new coin[possible_coin_count];

    printf( "Coin candidates: %i\n", possible_coin_count );

    // Initialise the coin structure:

    for( int num = 0; num < thresholded_flattened_hough_space.rows; num++ ) {
        coins[num].x_coord = -1;
        coins[num].y_coord = -1;
        coins[num].radius  = -1;
    }






    // Let's go to each local minima in the thresholded region, find the most voted for pixel and find its most voted for radius.

    int x_proximity_threshold = 60; // Pixel count of the minima of the coins.
    int y_proximity_threshold = 60;

    // 1. Find the best pixel in this region.

    int best_sum        = 0;
    int number_of_coins = 0;

    int index = 0;
    for( int outer_index = 0; outer_index < number_of_candidates; outer_index++ ) {
        int current_index   = 0;
        int coin_found      = 0;
        int best_x          = -1;
        int best_y          = -1;
        int best_sum        = 0;

        for( int inner_index = 0; inner_index < number_of_candidates; inner_index++ ) {
            int current_x = x_coordinates[current_index];
            int current_y = y_coordinates[current_index];

            // Loop through those near to it and check if there is one in its region greater than it.

            if ( thresholded_flattened_hough_space.at<float>(current_y, current_x) > 0 ) {

                coin_found = 1;

                // Best_x is the problem here.

                if( (best_x == -1) && (best_y == -1) ) {
                    best_x = current_x;
                    best_y = current_y;
                }

                if( ( abs(best_x - current_x) <= x_proximity_threshold ) && ( abs(best_y - current_y) <= y_proximity_threshold ) ) {
                    if( best_sum < thresholded_flattened_hough_space.at<float>(current_y, current_x) ) {
                        best_y   = current_y;
                        best_x   = current_x;
                        best_sum = thresholded_flattened_hough_space.at<float>(current_y, current_x);
                    }

                    thresholded_flattened_hough_space.at<float>(current_y, current_x) = 0;
                }
            }

            current_index++;
        }

        // Now we have our x and y position of our coin:
        // Let's reset everything and store the coins position.

        if(coin_found == 1) {
            coins[number_of_coins].x_coord = best_x;
            coins[number_of_coins].y_coord = best_y;
            number_of_coins++;
        }
    }


    // Now let's go through the coins and find the correct radius:
    for( int num = 0; num < number_of_coins; num++ ) {
        
        int likelihood = 0;
        int distance   = 0;

        for(int radius = 0; radius < (max_radius-min_radius); radius++ ) {
            if(hough_space.at<int>(coins[num].y_coord, coins[num].x_coord, radius ) > likelihood) {
                likelihood = hough_space.at<int>(coins[num].y_coord, coins[num].x_coord, radius );
                distance   = radius + min_radius;
            }
        }

        coins[num].radius = distance;
    }

    for( int num = 0; num < number_of_coins; num++ ) {
        printf("Circle centre X:%i\n", coins[num].x_coord);
        printf("Circle centre Y:%i\n", coins[num].y_coord);
        printf("Circle radius:%i\n",   coins[num].radius);
    }

    // Now we can write the circle onto the original image.

    int true_coin_count   = number_of_coins;

    printf("True number of coins: %i", true_coin_count );

    CvPoint* coin_centres = new CvPoint[true_coin_count];

    // Draw a circle in white.

    Mat input_image_copy = input_image.clone();

    for( int num = 0; num < number_of_coins; num++ ) {
        coin_centres[num].x = coins[num].x_coord;
        coin_centres[num].y = coins[num].y_coord;
        circle(flattened_hough_space, coin_centres[num], coins[num].radius, Scalar( 255, 255, 255 ), 2);
        coin_centres[num].x = coins[num].x_coord-max_radius;
        coin_centres[num].y = coins[num].y_coord-max_radius;
        circle(input_image_copy,      coin_centres[num], coins[num].radius, Scalar( 255, 255, 255 ), 2);
    }

    scaleFloatImage( flattened_hough_space, scaled_flattened_hough_space );

    imwrite("OutputImages/flattened_hough_space_with_circles.jpg", scaled_flattened_hough_space);
    imwrite("OutputImages/input_image_with_circles.jpg", input_image_copy);



    // Clean up:
    delete[] coin_centres;
    delete[] y_coordinates;
    delete[] x_coordinates;
    delete[] coins;
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
    
    float maximum = -10000.0f; // Some arbitrary number.

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

    float minimum = 10000.0f; // Some arbitrary number.

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





















void convertImageToBlackAndWhite( Mat &image_to_convert ) {
    cvtColor(image_to_convert, image_to_convert, CV_BGR2GRAY);
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






















