/////////////////////////////////////////////////////////////////////////////
//
// COMS30121 - thr.cpp
// TOPIC: RGB explicit thresholding
//
// Getting-Started-File for OpenCV
// University of Bristol
//
/////////////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include <opencv2/opencv.hpp> 
#include <opencv/cv.h>        //you may need to
#include <opencv/highgui.h>   //adjust import locations
#include <opencv/cxcore.h>    //depending on your machine setup

using namespace cv;

int main() { 

  // Read image from file
  Mat image         = imread("mandrill (2).jpg", 1);
  Mat grey_image;
  cvtColor(image, grey_image, CV_BGR2GRAY);


  Mat kernel        = Mat( 3, 3, CV_8UC1 );
  Mat kernel_image  = Mat( grey_image.rows - 1 , grey_image.cols - 1 , CV_8UC1 ); 

  // Set the kernel for convolution.
  for ( int y = 0; y < 3; y++ ) {
    for ( int x = 0; x < 3; x++ ) {
        kernel.at<uchar>(y,x) = 1;
    }
  }

kernel.at<uchar>(y,x) = 8;

  // Threshold by looping through all pixels
  for( int y = 0; y < kernel_image.rows; y++ ) {
    for( int x = 0; x < kernel_image.cols; x++ ) {
        int summation = 0;

        // Implement convolution with kernel.
            for( int square_row = -1; square_row <= 1; square_row++ ) {
                for ( int square_column = -1; square_column <= 1; square_column++ ) {
                    summation += grey_image.at<uchar>(y - square_row, x - square_column) * kernel.at<uchar>(square_row + 1, square_column + 1);
                }
            }

            kernel_image.at<uchar>(y, x) = summation/9;
        } 
    } 

  //Save thresholded image
  imwrite("task_2.jpg", kernel_image);

  return 0;
}








