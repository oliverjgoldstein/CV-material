{------------------------------------------------------------
 -- Language Engineering: COMS22201
 -- Assessed Coursework 2: CWK2
 -- Question 3: Axiomatic Semantics of While with read/write
 ------------------------------------------------------------
 -- This stub file gives two code fragments (from the test7.w 
 -- source file used in CWK1) that you will need to annotate 
 -- with tableau correctness proofs using the partial axiomatic 
 -- semantics extended with axioms for read/write statements.
 -- 
 -- To answer this question, upload one file "cwk2.w" to the 
 -- "CWK2" unit component in SAFE by midnight on 01/05/2016.
 --
 -- For further information see the brief at the following URL:
 -- https://www.cs.bris.ac.uk/Teaching/Resources/COMS22201/cwk2.pdf
 ------------------------------------------------------------}


{------------------------------------------------------------
 -- Part A)
 --
 -- provide a tableau-based partial correctness proof
 -- of the following program (for computing factorials) 
 -- with respect to the given pre- and post-conditions
 -- by completing the annotation of the program with 
 -- logical formulae enclosed within curly braces:
 ------------------------------------------------------------}

{

I -> use as an implication sign

}



{ head(IN) = n }

write('Factorial calculator'); 

writeln;

write('Enter number: ');

read(x);

{ x = n }

{ IN = tail(IN) }

write('Factorial of '); 

write(x); 

write(' is ');

y := 1; 

{ y = 1 & x = n }

{
1. y = 1 given
2. x = n given
3. n > 0 assume
4. x > 0 sub 2 in lhs of 3
5. n! = n! by reflexivity of =
6. x! = n! sub 2 in las of 5
7. 1*x! = x! by def of multiplication
8. x! = yx! sub 1 in the of 6.
9. n! = yx! sub 7 in rhs of 5
10. x>0 sub 2 in lhs of 3 
}

{ x > 0 -> y*x! = n! }

while !(x=1) do (

{ x > 0 -> y*x! = n! & !(x=1) }

{
1. n!=yx! given
2. x>0    given
3. ¬(x=1) given
4. x>1 from 2 and 3
5. x!=x(x-1)! by defn of fact using 2
6. n!=y(x(x-1)!) sub 5 in rhs of 1
7. n!=(y*x)(x-1)! by associativity of mult
8. x-1>0 dec both sides of 4 
}

{ x - 1 > 0 -> n! = y*x*(x-1)! & !(x=1) }

  y := y * x;

{ x - 1 > 0 -> n! = y(x-1)! }


  x := x - 1


{ x > 0 -> n! = yx! }

);

{ x > 0 -> n! = yx! & !!(x=1) }
{
1. n!=yx! given
2. x>0 given
3. ¬¬(x=1) given
4. x=1 ¬¬ elimination on 3
5. x!=1!=1 by defn of fac using 4
6. n!=y1 sub 5 in rhs of 1
7. n!=y by defn of mult
8. y=n! by symmetry of =
}
{ y = n! }

{ append(OUT,[y]) = append(_,[n!]) }

write(y);

{ OUT = append(_ , [n!]) }
{ append(OUT,[‘\n’]) = append(_,[n!,_]) }


writeln;

{ OUT=append(_ , [n!,_]) }
{ append(OUT, [‘\n’]) = append(_,[n!,_,_]) }

writeln;

{ OUT = append(_, [n!,_,_]) }

{------------------------------------------------------------
 -- Part B)
 --
 -- provide a tableau-based partial correctness proof
 -- of the following program (for computing exponents) 
 -- with respect to suitable pre- and post-conditions:
 ------------------------------------------------------------}
{

I -> use as an implication sign

}

{ n = head(IN) & m = head(tail(IN)) }

write('Exponential calculator'); writeln;
write('Enter base: ');
read(base);

{ base = n & IN = tail(IN) }

if 1 <= base then (

{ base = n & head(IN) = m & base >= 1}
{
1. base     = n given
2. head(IN) = m given
3. base    >= 1 given
4. n       >= 1 substitute 3 in left hand side of 1
}
{ base = n & n >= 1 }

write('Enter exponent: ');
{ base = n & head(IN) = m & n>= 1 }
read(exponent);
{ exponent = m & base = n & n >= 1 & m >= 1 }

num := 1;
{ exponent = m & base = n & n >= 1 & m >= 1 & num = 1 }
count := exponent;

{ exponent = m & base = n & n >= 1, m >= 1 & num = 1 }
{
1. exponent = m given
2. count    = exponent given
3. count    = m as sub 2 into 1
}
{ exponent = m & base = n & n >= 1, m >= 1 & num = 1 & count = m }

{ count >= 0 -> base^exponent = num*base^count & base = n & exponent = m }
{
1. base     = n given
2. exponent = m given
3. base^exponent = n^m by equality and substituting 1,2 into lhs.
4. num      = 1 given
5. base^exponent = num*base^count
6. n^m      = 1*n^m from substituting 4, 1, 2, into 5
7. n^m      = n^m by definition of 1*x = x for all x
8. Finally converting back we can see base ^ exponent = num*base^count therefore we can see invariant holds before loop. 
}

while 1 <= count do (

{ count >= 0 -> base ^ exponent = num*base^count & base = n & exponent = m & count >= 1 }
{
1. count >= 0 -> base^exponent = num*base^count given
2. by definition of (<=) !(count <= 1) is equivalent to count >= 1
3. count - 1 >= 0 subtract both side of 2 by 1 i.e. count - 1 >= 1 - 1 -> count - 1 >= 0
4. base ^ exponent = (num*base)*base^(count-1)
}
 
{ base ^ exponent = num * base ^ (1 + count - 1) & !(count <= 1) }

{(count - 1) >= 0 -> base^exponent = (num*base)*base^(count-1) & base = n & exponent = m}

    num := num * base;

{(count - 1) >= 0 -> base ^ exponent = num * base ^ (count - 1) & base = n & exponent = m }

    count := count - 1

{ count >= 0 -> base ^ exponent = num * base ^ (count) & base = n & exponent = m }

  );



{ count >= 0 -> base^exponent = num*base^count & base = n & exponent = m & !(count >= 1) }
{
  1. !(count - 1 >= 0) -> count <= 1
  2. base = n given
  3. exponent = m given
  4. !(count >= 1) given
  5. count < 1 from 4 and definition of inequality
  6. count = 0 from substitution from equation 5 into equation 1 using upper and lower bounds defining count as 0. i.e. if 0 <= x < 1 then x = 0 for all x
  7. num*base^count = base^exponent
  8. num*n^0 = n^m sub 2, 3, 6 into equation 7
  9. num = n^m as a consequence of equation 8.
}
{ num=n^m }

{ num = base ^ exponent : ^ is the power operator }

  write(base); write(' raised to the power of '); write(exponent); write(' is ');
{ append(OUT,[num]) = append(_,[n^m]) }
  write(num)
{ OUT = append(_,[n^m]) }

{ base = n }

) else (
{
1. !(1<=base) given
2. from negation 1 > base
3. from symmetry of < we can use 2 to get 1 < base
}

{ 
1. base > 1 given
2. base < 1 given
3. From 2 and 1 this implies false
WE FIND A CONTRADICTION HERE.
}


{ base < 1 & base = n & head(IN) = m }

  write('Invalid base '); 

{ append(OUT,[base]) = append(_,[n]) }

write(base)

{ OUT = append(_,[n]) }
{ base = n }

);

{ I now show that regardless of what if case you are in you achieve this output for the else case one can see the existence of a contradiction so working upwards we can see base is both less than and greater than one. and false implies false. }


{ OUT = append(_,[n^m]) }
writeln
