{-**********-}

greeting :: String 				-- greeting simply returns a string
greeting = "Hello "++"world!"	-- It concatenates Hello and World

{-**********-}

value :: Int 					-- Value is an Int with value 5.
value = 5

{-**********-}

f :: Integer -> Integer 		-- f takes and integer and recursively finds the fibonacci number.
f n
	| n==0 = 0 					-- guards here are the edge cases for the fibonacci recursion.
	| n==1 = 1 					
	| n >= 2 = f (n-1) + f (n-2)

{-**********-}

fac :: Int -> Int
fac n
	| n==0  = 1
	| n>0   = n * fac (n-1)

{-**********-}

g :: Integer -> Integer -> Integer -> Integer
g n a b
	| n == 0 = a
	| n >= 1 = g (n-1) (b) (a+b)

{-**********-}

sort a b c 
	| a<=b, c>=b = b  
	| b<=a, a>=c = a
	| c<=b, a>=b = b
	| a<=c, b>=c = c 
	| c<=a, b>=a = a
	| b<=c, a>=c = c

{-**********-}

intRoot :: Int -> Int
intRoot x = try x where
  try c   | c*c > x   = try (c - 1) 
          | c*c <= x  = c

{-**********-}

numRoots a b c
	| ((b*b)-(4*a*c)) > 0 = 2
	| ((b*b)-(4*a*c)) == 0 = 1
	| ((b*b)-(4*a*c)) < 0 = 0

{-**********-}

power :: Int -> Int -> Int
power x n       
	| n==0	  = 1
	| even n = power (x*x) (div n 2)
	| odd n  = x * power x (n-1)

{-**********-}

pow :: Int -> Int -> Int
pow x 0         = 1
pow x n
    | m == 0    = let y=x*x in pow y d
    | otherwise = let y=pow x (n-1) in x*y
    where (d,m) = divMod n 2
    
{-**********-}