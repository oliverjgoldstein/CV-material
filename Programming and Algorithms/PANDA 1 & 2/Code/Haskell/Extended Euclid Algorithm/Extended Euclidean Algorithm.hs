module Euclid where

euclidGCD :: Integer -> Integer -> ( (Integer, Integer), Integer )
euclidGCD x y
	| y > x = euclid y x
	| otherwise = euclid x y
	where
	euclid :: Integer -> Integer -> ( (Integer, Integer), Integer )
	euclid a b
		| b == 0 = ((1,0), a)
		| otherwise = ((y, x - y*q), g)
		where
		(q,r) = longDivision a b
		((x,y), g) = euclid b r

longDivision :: Integer -> Integer -> (Integer, Integer)
longDivision x y = binaryDivision x y (maximumShift x y 1)
	where
	binaryDivision :: Integer -> Integer -> Integer -> (Integer, Integer)
	binaryDivision x y q
		| x < y = (0, x)
		| otherwise = bitChoice x y q
		where
		bitChoice :: Integer -> Integer -> Integer -> (Integer, Integer)
		bitChoice x y q
			| x < h = (qst, rst)
			| otherwise = (qgt+q, rgt)
			where
			(qst, rst) = binaryDivision x y (div q 2)
			(qgt, rgt) = binaryDivision (x-h) y (div q 2)
			h = q*y
	
	maximumShift :: Integer -> Integer -> Integer -> Integer
	maximumShift x y q
		| nq*y > x = q
		| otherwise = maximumShift x y nq
		where
		nq = 2*q

{-
longDivision :: Integer -> Integer -> (Integer, Integer)
longDivision x y = binaryDivision x y 0
	where
	binaryDivision :: Integer -> Integer -> Integer -> (Integer, Integer)
	binaryDivision x y q
		| x < y = (q,x)
		| otherwise = binaryDivision rt y (q + qt)
		where
		(qt,rt) = binaryIncrement x y  
	
	binaryIncrement :: Integer -> Integer -> (Integer, Integer)
	binaryIncrement x y
		| x < z = (1, x-y)
		| otherwise = (2*q, r)
		where
		z = 2*y
		(q,r) = binaryIncrement x z
-}

