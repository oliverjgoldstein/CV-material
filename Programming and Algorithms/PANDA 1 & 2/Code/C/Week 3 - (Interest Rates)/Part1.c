#include <stdio.h>

float raise_number_to_power(float base, unsigned int exponent)
{
	int loop_counter = 1;
	float original_base = base;	
	while(loop_counter<exponent)
	{
		base = original_base*base;	
		loop_counter++;	 
	}
	return base;
}

int main()
{
	float result = (raise_number_to_power(1.05,25)*1000);
	printf("%f\n",result);
	return 0;
}

