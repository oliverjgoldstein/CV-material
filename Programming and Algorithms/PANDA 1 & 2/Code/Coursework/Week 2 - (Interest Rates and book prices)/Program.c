typedef int bool;
#define true 1
#define false 0

#include <stdio.h>

float price_per_book(float page_count, float book_copy_count, float sheet_price, float plate_price)
{
	float total_price = 0; // Will represent the total price of the book.
	float VAT;

	// Calculate Total Price.
	total_price += (((page_count/2)*book_copy_count) * sheet_price); // Here we divide by 2 as 2 pages per sheet.
	total_price += (plate_price * page_count);
	total_price += (2 * book_copy_count);
	VAT = (0.2*total_price);
	total_price += VAT;
	// End Total Price Calculation
	return total_price;
}

float black_and_white(float page, float book)
{
	return price_per_book(page, book, 0.01, 7.00);
}

float colour(float page, float book)
{
	return price_per_book(page, book, 0.04, 28.00);
}

int main()
{
	// 1 is for colour and 0 is for B&W
	float first = colour(32,1000);
	float second = black_and_white(40,2000);
	float third = black_and_white(160,400);
	float sum = first + second + third;
	printf("%f\n",sum);
	return 0;
}


