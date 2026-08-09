partition::(a->Bool)->[a]->([a],[a])
partition _ [] = ([],[])

partition f (x:xs) 
	| f x = (x:ys,zs)
	| otherwise = (ys,x:zs)
	where
		(ys,zs) = partition f xs

qSort :: [Int] -> [Int]
qSort [] = []
qSort (x:ys) = (qSort ls) ++ [x] ++ (qSort gs)
	where
		ls = [y | y<-ys, y<=x]
		gs = [y | y<-ys, y>x]

bub ::[Int]->[Int]->[Int]->Bool->[Int]
bub [] [] zs _ = zs
bub xs [y] zs True = bub [] xs (y:zs) True
bub xs [y] zs False = xs++(y:zs) 
bub xs (y1:y2:ys) zs b
	| y1 <= y2 = bub(xs++[y1]) (y2:ys) zs b
	| otherwise = bub(xs++[y2]) (y1:ys) zs True

bSort::[Int]->[Int]
bSort xs = bub [] xs [] False

testFunc::[Int]->[Int]->[Int]->[Int]
testFunc xs [y] zs = xs++(y:zs)

