-- Beggining of program

rep::Int->String->String 		-- This is a replicate function returning a String repeated n times where n is an Int.
rep _ [] = [] 					-- When hitting the empty list no matter how many times you want to repeat it, it will be empty as 0*(Any Int) is equal to 0.
rep 0 s = [] 					-- If the string is to be repeated 0 times then it should return an empty string.
rep 1 s = s 					-- If the string is to be repeated once then return itself.
rep n s =  (s) ++ rep (n-1) (s) -- For n times, return the string concatenated to the function calling itself recursively (n-1) times. It will eventually catch on one of the edge cases.

-- New function

allIn::Eq a => [a]->[a]->Bool 			-- allIn uses a type called a that can be compared using the equality operator. It takes two lists of this type and returns a Bool.
allIn [] [] = True						-- The purpose of the function is to determine whether one list is a subset of the other. An empty list is a subset of the empty list.
allIn a [] = False						-- Is a list with elements inside an empty list. NO.
allIn [] a = True						-- Is the empty list inside a list with elements. YES. 1:2:3:[] is syntactic sugar for [1,2,3]
allIn (a:as) b 							-- Takes the head of the list and checks to see if each element is within b, then calls it again and ands the Trues together.
	|a `elem` b = True && allIn as b 	-- Will only give true if all of the and statements return a true.
	|otherwise = False 					

-- New function

csh::Int->String->String 					-- csh stands for circular shift.
csh 0 s = s 								-- Shifted zero times gives the original.
csh _ [] = [] 								-- Given an empty list, no matter the shift returns an empty list.
csh n xs = csh (n-1) ((last xs):(init xs))	-- csh n xs takes the csh with n-1 and binds the last element onto everything but the last element i.e. the init.

-- New function

prm::String->String->Bool 					-- prm tries to detect whether 
prm x y 									-- This function tries to ascertain whether one list is a reordering of the other.
	|qSort(x) == qSort(y) = True 			-- if the sorted first string is the sorted second string then return true.
	|otherwise = False

qSort :: String -> String 						-- Quicksort 
qSort [] = [] 									-- The empty list and edge case is the empty list.
qSort (x:ys) = (qSort ls) ++ [x] ++ (qSort gs)  -- Takes Quicksort first divides a large array into two smaller sub-arrays: the low elements and the high elements.  												
	where 										-- Quicksort can then recursively sort the sub-arrays. The steps are: Pick an element, called a pivot, from the array.
		ls = [y | y<-ys, y<=x] 					-- qSort the lower elements.
		gs = [y | y<-ys, y>x] 					-- qSort the higher elements.

comp::[String]->String 													-- This is a string compare function
comp [] = [] 															-- Compares a list of strings and returns when it finds a repeat element in the list. Edge case is empty list if nothing is found.
comp s = if (head s) `elem` (tail s) then (head s) else comp (tail s) 	-- If the head of the list is an element of the tail then return the head else compare the tail.

lrs::String->String 				-- LRS finds the longest repeated substring by examining the relationship between the length of a string and the numner of substrings.
lrs [] = []							-- Empty list is the edge case.
lrs s = comp [g s|g<-[((take x).(drop y))|x<-reverse [1..((length s)-1)], y<-[0..((length s)-x)]]]

split2::Int->[Char]->String -- Splits a list of characters dependent on the place of the int. Every time that Int multiple is reached in the list of characters.
split2 n [] = [] -- Given a number and an empty list, a split empty list is always the empty list.
split2 n s = (fst (splitAt n s)) ++ "\n" ++ (split n (snd (splitAt n s)))



-- NUMBER GENERATION 
-- Works by adding together 01 and 10 to give 121 and then 0121 and 1210 to give 1331. 
x = [0]
start = [1]
newrow input = zipWith (+) (x ++ input) (input ++ x)
triangle::Int->[Integer]
triangle n = (iterate newrow start) !! n -- Iterate takes the output and feeds it into the input.

funcinput::Int->[[Integer]] -- funcinput is given an integer of the pascals triangle and returns a list of list of integers of pascals triangle.
funcinput (-1) = [] 		-- base case of -1
funcinput x = [(triangle y) | y<-[0 .. x]]  -- returns a list of the triangle numbers.

-- IMAGE GENERATION

genfinal::[Integer]->[Integer]->String 
genfinal [] [] = []
genfinal [x] _ = show x 
genfinal (x:xs) (y:ys) = ((show x) ++ genericRep y ".") ++ genfinal xs ys

printlast2::Int->String
printlast2 x = genfinal (triangle (x)) (lengths2 (triangle (x-1)))

genrow::[Integer]->String->Bool->String -- genrow takes a list of integers, a string and whether the next one is a fullstop or not.
genrow [] [] _ = [] 
genrow [] (x:xs) True = '.':genrow [] xs True
genrow olives@(x:xs) (y:ys) test
	| test == True && y == '.' = '.':genrow olives ys ((head ys)=='.')
	| test == False && y/='.' = '.':genrow olives ys False
	| test == True &&  y/='.' = '.':genrow olives ys ((head ys)/='.')
	| test == False && y == '.' = (show x)++genrow xs (drop (length(show x)-1) ys) True


teller::String->Int->String
teller s 0 = genrow (triangle 0) s True
teller s y = (teller mayerrow (y-1)) ++ "\n" ++ mayerrow 
	where mayerrow = genrow (triangle y) s True 


pas::Int->String
pas 0 = ""
pas 1 = "1\n"
pas x =  ((teller (printlast2 (y)) (y-1) ) ++ "\n" ++ (printlast2 y))++"\n"
	where y = (x-1)


lengths2::[Integer]->[Integer]
lengths2 [] = []
lengths2 (x:xs) = (numDigits x 1):(lengths2(xs))

numDigits :: Integer -> Integer -> Integer
numDigits x accumulator
	| (quot x 10) >= 1 = numDigits (quot x 10) (accumulator+1)
	| otherwise = accumulator
--END

genericRep::Integer->String->String
genericRep 0 _ = []
genericRep x [] = []
genericRep x a = a ++ (genericRep (x-1) a)

-- Ant

type Track = (Int,(Int,Int))
type Point = (Int,Int,Int)
type Image = [Point]

changeOrientBlack::Int->Int
changeOrientBlack x = ((x+3) `mod` 4)

changeOrientWhite::Int->Int
changeOrientWhite x = ((x+1) `mod` 4)

newxy::Int->(Int,Int)->(Int,Int) -- Orientation then tuple of coordinates
newxy o (x,y) 
	| o == 0 = (x,y+1)
	| o == 1 = (x+1,y)
	| o == 2 = (x,y-1)
	| o == 3 = (x-1,y)


updateElem::Track->Image->Image
updateElem b a 
	| (((u,x,1) `notElem` a) && ((u,x,0) `notElem` a)) == True = addElem (u,x,1) a 
	| otherwise = deleteReAdd ( findElem b a ) a  
	where 
		u = fst(snd(b))
		x = snd(snd(b))

findElem::Track->Image->Point
findElem b a 
	| ((fst(snd(b)),snd(snd(b)),1) `elem` a) = ((fst(snd(b)),snd(snd(b)),1))
	| ((fst(snd(b)),snd(snd(b)),0) `elem` a) = ((fst(snd(b)),snd(snd(b)),0))

deleteReAdd::Point->Image->Image -- point to delete, image to replace the point in.
deleteReAdd b a = addElem (reverseTrack b) (remove b a)

updateElem2::Track->Image->Image
updateElem2 b a 
	| (((u,x,1) `notElem` a) && ((u,x,0) `notElem` a)) == True = addElem (u,x,3) a 
	| otherwise = deleteReAdd2 ( findElem b a ) a  
	where 
		u = fst(snd(b))
		x = snd(snd(b))

deleteReAdd2::Point->Image->Image -- point to delete, image to replace the point in.
deleteReAdd2 b a = addElem (reverseTrack2 b) (remove b a)

reverseTrack2::Point->Point -- reverses Point.
reverseTrack2 (a,b,c)
	| c == 0 = (a,b,2)
	| c == 1 = (a,b,3) 

reverseTrack::Point->Point -- reverses Point.
reverseTrack (a,b,c)
	| c == 0 = (a,b,1)
	| c == 1 = (a,b,0) 

remove::Point->Image->Image
remove y [] = []
remove y (x:xs) = if y==x then remove y xs else x:(remove y xs)

addElem::Point->Image->Image
addElem b a = a++[b]

passToRender::Image->Track->Int->Image
passToRender a b c = updateElem2 b a 

boolValueofNext::Track->Image->Int
boolValueofNext b a 
	| (((u,x,1) `notElem` a) && ((u,x,0) `notElem` a)) == True = 0
	| otherwise =  trd3(findElem b a) 
	where 
		u = fst(snd(b))
		x = snd(snd(b))

updateGrid::Image->Track->Int->Int->Image -- Int here says what is the colour of the first coordinate. Int is no of times
updateGrid a b c 0 = passToRender a b c
updateGrid a b c d = updateGrid (updateElem b a) (moveAnt b c) (boolValueofNext (moveAnt b c) (updateElem b a)) (d-1)

moveAnt::Track->Int->Track -- The Int says which way to turn.
moveAnt b 1 = (changeOrientBlack (orientation), (newxy (changeOrientBlack (orientation)) (x,y)))
	where 
		orientation = fst(b)
		x = fst(snd(b))
		y = snd(snd(b))
moveAnt b 0 = (changeOrientWhite (orientation), (newxy (changeOrientWhite (orientation)) (x,y)))
	where 
		orientation = fst(b)
		x = fst(snd(b))
		y = snd(snd(b))


fst3 :: (a, b, c) -> a
fst3 (x, _, _) = x

snd3 :: (a, b, c) -> b
snd3 (_, x, _) = x

trd3 :: (a, b, c) -> c
trd3 (_, _, x) = x

bounds::Image->(Int,Int,Int,Int) 
bounds x = (lowX x, highX x, lowY x, highY x)
	where
		lowX i = minimum [fst3 p|p<-i]
		highX i = maximum [fst3 p|p<-i]
		lowY i = minimum [snd3 p|p<-i]
		highY i = maximum [snd3 p|p<-i]


split::Int->[Char]->[Char]
split n [] = []
split n s = (fst (splitAt n s)) ++ "\n" ++ (split n (snd (splitAt n s)))

renderGrid::Image->[Char]
renderGrid [] = []
renderGrid img = split (highX-lowX+1) ([if (x,y,1) `elem` img then 'x' else if (x,y,0) `elem` img then '.' else if (x,y,2) `elem` img then '+' else if (x,y,3) `elem` img then '*' else '.'|   y<-reverse[(lowY)..(highY)], x<-[(lowX)..(highX)]])
	where 
		(lowX,highX,lowY,highY) = bounds img 


ant::[(Int,Int)]->Int->String
ant a x = helper (renderGrid( updateGrid (initList a) (3, (0,0)) (checkOriginColour (initList a)) x))

helper::[Char]->String
helper a = a

checkOriginColour::Image->Int
checkOriginColour a = if (0,0,1) `elem` a then 1 else 0

initList::[(Int,Int)]->Image
initList [] = []
initList a = [(fst(head(a)), snd(head(a)), 1)] ++ initList (tail(a)) 

d::Int
d=77



a::(ff->yy)->ff->yy
a f x = f x
e::[Int]
e = [-1000..]
b::Int->Int


b ghy = ghy

c::Bool->Int


c True = 49


c False = 78

