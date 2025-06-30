#include <stdio.h>
#include <opencv2/opencv.hpp> 
#include <opencv/cv.h>        
#include <opencv/highgui.h>   
#include <opencv/cxcore.h>

using namespace cv;

int main() {
    
    //create a red 256x256, 8bit, 3channel BGR image in a matrix container
    
    Mat mandrill_image = imread("mandrillRGB.jpg", 1);
    
    //construct a window for image display
    namedWindow("Display window", CV_WINDOW_AUTOSIZE);
   
    for ( int row = 0; row < mandrill_image.rows; row++ ) {
        for ( int col = 0; col < mandrill_image.cols; col++ ) {

            uchar blue_pixel  = mandrill_image.at<Vec3b>( row, col )[0];
            uchar green_pixel = mandrill_image.at<Vec3b>( row, col )[1];
            uchar red_pixel   = mandrill_image.at<Vec3b>( row, col )[2];

            if (blue_pixel>100) {
                mandrill_image.at<Vec3b>(row, col)[0] = 255;
                mandrill_image.at<Vec3b>(row, col)[1] = 2;
                mandrill_image.at<Vec3b>(row, col)[2] = 225;
            } else {
                mandrill_image.at<Vec3b>(row, col)[0] = 0;
                mandrill_image.at<Vec3b>(row, col)[1] = 0;
                mandrill_image.at<Vec3b>(row, col)[2] = 0;
            }
        }
    }


    // threshold( mandrill_image, mandrill_image, 128, 255, 4 );

    //visualise the loaded image in the window
    imshow("Display window", mandrill_image);
    
    //save image to file
    imwrite("myimages.jpg", mandrill_image);
    
    //free memory occupied by image
    mandrill_image.release();
    
    return 0;
}












