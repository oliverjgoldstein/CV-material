cmake_minimum_required(VERSION 2.8)
project( colourthr )
find_package( OpenCV )
include_directories( ${OpenCV_INCLUDE_DIRS} )
add_executable( colourthr colourthr.cpp )
target_link_libraries( colourthr ${OpenCV_LIBS} )