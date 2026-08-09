#include <stdio.h>

float raise_number_to_power(float base, int exponent)
{
	int loop_counter = 1;
	float original_base = base;
	base = 1;
	int negative_flag;


	if(exponent<0)
	{
		exponent = - exponent;
		negative_flag = 1;
	}
	else
	{
		negative_flag = 0;
	}

	while(loop_counter<=exponent)
	{
		base = original_base*base;	
		loop_counter++;	 
	}

	if(negative_flag == 1)
	{	
		base = 1/base;
	}

	return base;
}

float raise_number_to_powert(float base, unsigned int exponent)
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
	float resulth = 10000*(raise_number_to_power(1.04,-12));
	printf("%f\n",resulth);
	float result = (raise_number_to_powert(1.05,25)*1000);
	printf("%f\n",result);
	return 0;
}
 
