{-# LANGUAGE RankNTypes #-}
import Data.List
import Data.Maybe

data DTree split leafClass = Leaf leafClass | Branch split (DTree split leafClass) (DTree split leafClass) deriving Show
data Class = A | B | ErrorClass deriving (Show, Eq) -- This currently only supports A and B although is easily extensible.

type LeafData          = (Class, [DataPoint])
type Data1D            = Int
type Split             = (Ordering, Double)
type DataPoint         = (Data1D, Class)
type DataSplit         = ([DataPoint], [DataPoint])
--type  a        = (Bar a, Baz a, Quux a, Fleeble a)
type ImpurityMeasure   = forall b. (Integral b, Fractional b, Ord b, Eq b) => DataSplit -> b


demoData :: [DataPoint]
demoData = sortBy sndCompare [(1, A), (2, A), (55, A), (1, A), (50, B), (17, A), (11, B)]


growTree :: [DataPoint] -> ImpurityMeasure -> DTree Split LeafData
growTree [] impurity = Leaf (ErrorClass, [])
growTree input_data impurity
    | length input_data == 0 = Leaf (ErrorClass, [])
    | otherwise            = if isHomogenous then Leaf leafClass else Branch (LT, num_split) (growTree (fst data_split) impurity) (growTree (snd data_split) impurity)
        where isHomogenous = fst(check)
              leafClass    = (snd(check), input_data)
              check        = isDataHomogenous input_data
              data_split   = bestSplit input_data impurity
              num_split    = (fromIntegral (fst (last (fst data_split))) + fromIntegral (fst (head (snd data_split)))) / 2

-- Is leaf homogenous - if so return whether it is and the class that should be predicted.
isDataHomogenous :: [DataPoint] -> (Bool, Class)
isDataHomogenous input_data 
    | b_len <= 1 = (True, A)
    | a_len <= 1 = (True, B)
    | otherwise  = (False, ErrorClass)
    where 
        a_len = classCount input_data A
        b_len = classCount input_data B


-- This returns count of class in data points.
classCount :: (Integral a) => [DataPoint] -> Class -> a
classCount input_data feature = fromIntegral(length (filter (\x -> snd (x) == feature) (input_data)))



--mapFr :: (Fractional b, Integral b, Ord b, Eq b) => (a -> b) -> [a] -> [b]
--mapFr f [] = []
--mapFr f a = f x (foldr f z xs)
















-- The following is how to deal with the relevant splits:

-- This sorts the data then finds smallest impurity split. Returns this split and the number to split on in future.
bestSplit :: [DataPoint] -> ImpurityMeasure -> DataSplit
bestSplit input_data impurity = returnSplit input_data (splits !! (fromMaybe 0 (elemIndex (minimum impurities) impurities)))
    where
        splits     = (splittingPoints (sortBy sndCompare input_data))
        impurities = map impurity (map (returnSplit input_data) splits) 

-- This tells bestSplit how to sort numeric 1D data.
sndCompare (a1, b1) (a2, b2)
    | a1 > a2  = GT
    | a1 < a2  = LT 
    | a1 == a2 = EQ

-- This returns where to split
splittingPoints :: [DataPoint] -> [Int]
splittingPoints input_data = filterMaybe diffAdjacent classes
    where classes = (snd (unzip input_data))


-- This finds where there are changes in the data => where it makes sense to split.
diffAdjacent :: Eq a => [a] -> Maybe Bool
diffAdjacent input
    | length input == 2 = if (input!!0 == input!!1) then Just True else Just False
    | otherwise = Nothing

-- Filters out indices which do not represent non contiguous data elements.
filterMaybe :: ([a] -> Maybe Bool) -> [a] -> [Int]
filterMaybe p xs = [ index | index <- [ 1 .. (length(xs) - 1) ], fromMaybe True (p (slice xs index (index + 2))) == False ]

-- This returns two elements from a list specified by two ints.
slice :: [a] -> Int -> Int -> [a]
slice xs lower upper = take (upper - lower) (drop lower xs)

returnSplit :: [DataPoint] -> Int -> DataSplit
returnSplit x n 
    | n == 0                  = ([], [])
    | n >= length(x)          = ([], [])
returnSplit [] _              = ([], [])
returnSplit input_data s      = ((take (s + 1) input_data), (drop (s+1) input_data))

-- This is the end of the part that deals with splits.











-- 1. count the number of elements in the leaf that are accurately classified
-- 2. take those and divide them by the total number of elements in the branches.

-- Takes a tree and the number of total data points in the node.
--(total_correct, totalSubTree, totalA, totalB)
pruneTree :: DTree Split LeafData -> (DTree Split LeafData, (Int, Int))
pruneTree (Branch s (Leaf x) (Leaf y)) = if total_acc < majAccuracy then (Leaf (majClass, dps), (0,0))  else (Branch s (Leaf x) (Leaf y), (0,0))
    where total_acc     = accuracy (correct_in_x + correct_in_y) (length (snd x) + length (snd y)) 
          correct_in_x  = fromIntegral (classCount (snd x) (fst x))
          correct_in_y  = fromIntegral (classCount (snd y) (fst y))
          majAccuracy   = accuracy (numCorrect majClass dps) (fromIntegral (length dps))
          majClass      = if totalA > totalB then A else B
          totalA        = classCount dps A
          totalB        = classCount dps B
          dps           = snd x ++ snd y

--other :: Class -> [Class]
--other A = B
--other B = A
--other _ = []

numCorrect :: Class -> [DataPoint] -> Int
numCorrect c dps = fromIntegral (length ( filter (\x -> snd (x) == c) dps))

accuracy :: (Fractional a) => Int -> Int -> a
accuracy correct total = fromIntegral(correct)/fromIntegral(total)



--pruneTree (Branch _ b1       (Leaf y)) = if acc y + then return Leaf
--pruneTree (Branch _ (Leaf x) b2      ) = 
--pruneTree (Branch _ b1       b2      ) = 








-- This is the section that describes how to execute the tree:











-- The following deals with how to calculate the impurity of the splits.

giniIndex ::ImpurityMeasure
giniIndex ds = (calcGini (fst ds) total_length) + (calcGini (snd ds) total_length)
    where total_length = length ( fst ds ) + length ( snd ds )

calcGini :: (Integral a, Fractional a, Ord a, Eq a) => [DataPoint] -> Int -> a
calcGini [] _ = error "Empty List"
calcGini dps len = (total/total_length) * (bfrac*(1-bfrac) + afrac*(1-afrac))
    where
        total_length = (fromIntegral len)
        bfrac        = (bs/total)
        afrac        = (as/total)
        bs           = (classCount dps B)
        as           = (classCount dps A)
        total        = (classCount dps B) + (classCount dps A)












-- This simply pretty prints the tree
printTree :: DTree Split LeafData -> IO()
printTree (Leaf l)          = putStrLn( "Leaf: " ++ show (fst l) ++ "\n" )
printTree (Branch sp t1 t2) = putStrLn( "LTE: " ++ show sp ++ "\n" ) >> printTree(t1) >> printTree(t2) 



