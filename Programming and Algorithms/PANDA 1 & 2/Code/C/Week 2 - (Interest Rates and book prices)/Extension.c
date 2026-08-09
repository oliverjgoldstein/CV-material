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

int Round(int sheet)
{
	if((sheet%16)==0)
	{
	return sheet;
	}
	else
{	
	int modulo = sheet % 16;
	int number = 16 - modulo;
	sheet += number;
	return sheet;
}
}

int Roundedc(int sheets, int book)
{
	int pages = 2 * sheets;
	return colour(pages,book);
}

int Roundedb(int sheets2, int book2)
{
	int pages2 = 2 * sheets2;
	return black_and_white(pages2,book2);
}

int main()
{
	int sheetno = Round(16);
	int rounded_bp = Roundedc(sheetno, 50);
	int sheetno2 = Round(15);
	int rounded_bp2 = Roundedc(sheetno2, 35);
	printf("%d\n",rounded_bp);
printf("%d\n",rounded_bp2);
	return 0;
}


