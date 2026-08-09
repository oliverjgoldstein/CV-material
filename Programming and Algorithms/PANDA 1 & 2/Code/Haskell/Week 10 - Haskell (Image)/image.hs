{-**********-}

--  define a Point as a pair of integers
type Point = (Int,Int)

--  define the point we call the origin 
origin :: Point
origin = (0,0)

-- define a function for scaling a point
-- horizontally by h and vertically by v
scale :: Int -> Int -> Point -> Point
scale h v (x,y) = (h*x,v*y)

-- flip a point about the  horizontal axis (y=0)
flipH :: Point -> Point
flipH (x,y) = (x,-y)

-- flip a point about the  vertical axis (x=0)
flipV :: Point -> Point
flipV (x,y) = (-x,y)
 
-- flip a point about the diagonal line (x=y)
flipD :: Point -> Point
flipD (x,y) = (y,x)
-- image functions
-- quarter turn clockwise
turnC :: Point -> Point
turnC (x,y) = (y,-x)

-- half turn 
turnB :: Point -> Point
turnB (x,y) = (-x,-y)
 
-- quarter turn anticlockwise
turnA :: Point -> Point
turnA (x,y) = (-y,x)

{-**********-}

-- define an Image as a list of points
type Image = [Point]

-- define a T-shaped image
t :: Image
t = [(0,1),(1,0),(1,1),(2,1)]
x :: Image
x = [(1,1), (1,-1), (2,0), (3,1), (3,-1)]

-- transform an Image point by point
pointwise :: (Point->Point) -> (Image->Image)
pointwise f = \ps -> [f p | p <- ps]

shift::Int->Int->Point->Point
shift h v (b,c) = (b+h,c+v) 

bounds::Image->(Int,Int,Int,Int)
bounds x = (lowX x, highX x, lowY x, highY x)
	where
		lowX i = minimum [fst p|p<-i]
		highX i = maximum [fst p|p<-i]
		lowY i = minimum [snd p|p<-i]
		highY i = maximum [snd p|p<-i]

sideBySide::Image->Image->Image
sideBySide img1 img2 = overlay img11 img22
	where
		(lowX,highX,lowY,highY) = bounds img1
		(lowX2,highX2,lowY2,highY2) = bounds img2
		func1 = pointwise (shift 0 (-lowY))
		func3 = pointwise (shift (-highX) 0)
		func2 = pointwise (shift 0 (-lowY2))     
		func4 = pointwise (shift (-lowX2) 0)  
		img11 = func1 (func3 img1)
		img22 = func2 (func4 img2)

split::Int->[Char]->[Char]
split n [] = [];
split n s = (fst (splitAt n s)) ++ "\n" ++ (split n (snd (splitAt n s)))


render::Image->[Char]
render [] = []
render img = split (highX-lowX+3) ([if (x,y) `elem` img then 'x' else if (x,y)==origin then '+' else if (x,y)==(0,y) then '|' else if (x,y)==(x,0) then '-' else '.'|   y<-reverse[(lowY-1)..(highY+1)], x<-[(lowX-1)..(highX+1)]])
	where 
		(lowX,highX,lowY,highY) = bounds img 

-- [(a,b),(a,b)]
-- For any of the y axes we make it a | i.e. when x = 0 and when y = 0 i.e. x axis we do 
-- For any of the points on the image we print a x which takes priority.
-- For the point 0,0 we print a +
-- Places with none of the above properties takes a .


-- overlay two images
overlay :: Image -> Image -> Image
overlay i j = i ++ j

{-**********-}