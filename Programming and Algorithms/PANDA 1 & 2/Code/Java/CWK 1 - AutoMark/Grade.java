class Grade
{
    public static void main(String[] args)
    {
        boolean binary = false;
        boolean gpa = false;
        float array[] = new float[args.length];
        try
        {
            int i = 0;
            boolean first_param_is_flag = false;
            boolean step = false;
            for(String arg : args)
            {
                step = false;
                if((first_param_is_flag==false) && (arg.equals("-binary")))
                {
                    binary = true;
                    step = true;
                    first_param_is_flag = true;
                }
                if((first_param_is_flag==false) && (arg.equals("-gpa")))
                {
                    gpa = true;
                    step = true;
                    first_param_is_flag = true;
                }
                if(step == false)
                {
                    if(arg.startsWith("0") && (Float.parseFloat(arg) != 0))
                    {
                        System.err.println("Error enter the correct types."); 
                        System.exit(1);
                    }
                    try
                    {
                        array[i] = Float.parseFloat(arg);
                    }
                    catch (Exception e)
                    {
                       System.err.println("Error enter the correct type."); 
                       System.exit(1);
                    }
                    if((array[i] > 100) || (array[i] < 0))
                    {
                       System.err.println("Error, enter a mark in the range."); 
                       System.exit(1);
                    }
                    i++; 
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("Error enter the correct types."); 
            System.exit(1);  
        }
        float sum = 0;
        for(float number : array)
        {
            sum += number;
        }
        int mark = Math.round(sum/(args.length-1));
        if((binary == false) && (gpa == false))
        {
            mark = Math.round(sum/args.length);
        if ((mark > 100) || (mark < 0))
        {
            System.err.println("Error, enter a mark in the range."); 
            System.exit(1);
        } 
        else if (mark < 40)
        {
            System.out.println("Fail"); 
        } 
        else if ((mark < 50) && (mark >= 40))
        {
            System.out.println("Third Class"); 
        } 
        else if ((mark < 60) && (mark >= 50))
        {
            System.out.println("Lower Second Class"); 
        } 
        else if ((mark < 70) && (mark >= 60))
        {
            System.out.println("Upper Second Class"); 
        } 
        else if ((mark < 80) && (mark >= 70))
        {
            System.out.println("First Class"); 
        } 
        else if ((mark < 90) && (mark >= 80))
        {
            System.out.println("Above and Beyond"); 
        } 
        else if ((mark < 100) && (mark >= 90))
        {
            System.out.println("Publishable"); 
        } 
        else if (mark == 100)
        {
            System.out.println("Perfect"); 
        } 
        }
        else if (binary == true)
        {
        int[] gradeBoundaries = { 0, 40, 50, 60, 70, 80, 90, 100, 101 };
        String[] grades =
        {
            "Fail",
            "Third Class",
            "Lower Second Class",
            "Upper Second Class",
            "First Class",
            "Above and Beyond",
            "Publishable",
            "Perfect"
        };
        int x = recurse (mark, gradeBoundaries, 0, 8);
        System.out.println(grades[x]); 
        }
        else if (gpa == true)
        {
            double value = ((sum/(args.length-1))/20)+0.05;
            value = Double.parseDouble(Double.toString(value).substring(0, Double.toString(value).indexOf('.') + 2));
            System.out.println("GPA "+(value)); 
        }
        System.exit(0);
    }
    private static int recurse(int mark, int[] gradeBoundries, int start, int end)
    {
        mark = Math.round(mark);
        int middle = Math.round((start + end)/2);
        System.out.println("Split: " + middle); 
        if((start == middle) || (end == middle))
        {
            return middle;
        }
        else if(mark<gradeBoundries[middle])
        {
            return recurse(mark, gradeBoundries, start, (middle-1));
        }
        else if(mark>=gradeBoundries[middle])
        {
            return recurse(mark, gradeBoundries, (middle+1), end);
        }
        else
        {
            return 0;
        }
    }
}
