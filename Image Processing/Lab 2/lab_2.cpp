#include <stdio.h>
#include <opencv/cv.h>        //you may need to
#include <opencv/highgui.h>   //adjust import locations
#include <opencv/cxcore.h>    //depending on your machine setup
#include <opencv2/opencv.hpp> 

using namespace cv;

void GaussianBlur(cv::Mat &input, int size, cv::Mat &blurredOutput);


// This first task is using an unsharp mask.

int main( int argc, char** argv ) {

    // Loading bureaucracies.
    Mat image           = imread( "Images/car1.png", 1 );
    cvtColor(image, image, CV_BGR2GRAY);
    Mat second_image    = image.clone();
    Mat blur_image;


    GaussianBlur( image, 8, blur_image );

    int minimum    = 256;
    int maximum    = -256;

    // imwrite("unsharp_mask.jpg", blur_image);

    // Mapping [0..x] -> [0..y] by means of (y/x)*t : t is the value to map.
    // Subtract the blurred image.

    for( int y = 0; y < second_image.rows; y++ ) {
        for( int x = 0; x < second_image.cols; x++ ) {

            image.at<uchar>(y,x)        -= blur_image.at<uchar>(y,x);
            second_image.at<uchar>(y,x) += image.at<uchar>(y,x);

            if( second_image.at<uchar>(y,x) < minimum ) {
                minimum = second_image.at<uchar>(y,x);
            }

            if( second_image.at<uchar>(y,x) > maximum ) {
                maximum = second_image.at<uchar>(y,x);
            }
        }
    }


    // Add on the absolute minimum value to make for mapping from 0-255.
    for ( int y = 0; y < second_image.rows; y++ ) {
        for( int x = 0; x < second_image.cols; x++ ) {
            second_image.at<uchar>(y,x) += abs(minimum);
        }
    }

    // Update to make mapping consistent.
    maximum += abs(minimum);

    // Perform the mapping.
    for ( int y = 0; y < image.rows; y++ ) {
        for( int x = 0; x < image.cols; x++ ) {
            second_image.at<uchar>(y,x) *= (255/maximum);
        }
    }

    // Save sharpened image
    imwrite("unsharp_mask.jpg", second_image);
}




void GaussianBlur(cv::Mat &input, int size, cv::Mat &blurredOutput)
{
    // intialise the output using the input
    blurredOutput.create(input.size(), input.type());

    // create the Gaussian kernel in 1D 
    cv::Mat kX = cv::getGaussianKernel(size, -1);
    cv::Mat kY = cv::getGaussianKernel(size, -1);
    
    // make it 2D multiply one by the transpose of the other
    cv::Mat kernel = kX * kY.t();

    //CREATING A DIFFERENT IMAGE kernel WILL BE NEEDED
    //TO PERFORM OPERATIONS OTHER THAN GUASSIAN BLUR!!!

    // we need to create a padded version of the input
    // or there will be border effects
    int kernelRadiusX = ( kernel.size[0] - 1 ) / 2;
    int kernelRadiusY = ( kernel.size[1] - 1 ) / 2;

    cv::Mat paddedInput;
    cv::copyMakeBorder( input, paddedInput, 
        kernelRadiusX, kernelRadiusX, kernelRadiusY, kernelRadiusY,
        cv::BORDER_REPLICATE );

    // now we can do the convoltion
    for ( int i = 0; i < input.rows; i++ )
    {   
        for( int j = 0; j < input.cols; j++ )
        {
            double sum = 0.0;
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
                    int imageval = ( int ) paddedInput.at<uchar>( imagex, imagey );
                    double kernalval = kernel.at<double>( kernelx, kernely );

                    // do the multiplication
                    sum += imageval * kernalval;                            
                }
            }
            // set the output value as the sum of the convolution
            blurredOutput.at<uchar>(i, j) = (uchar) sum;
        }
    }
}















 