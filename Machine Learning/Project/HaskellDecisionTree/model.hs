{-# LANGUAGE RankNTypes #-}
import Data.List
import Data.Maybe
import Data.Char

data DTree split leafClass = Leaf leafClass | Branch split (DTree split leafClass) (DTree split leafClass) deriving Show
data Class = A | B | ErrorClass deriving (Show, Eq) -- This currently only supports A and B although it is extensible to multiclass problems.

type LeafData          = (Class, [DataPoint])
type Data1D            = Int
type Split             = (Ordering, Double)
type DataPoint         = (Data1D, Class)
type DataSplit         = ([DataPoint], [DataPoint])
type ImpurityMeasure   = DataSplit -> Double


-- Please note: inconsistent labelling e.g. (1,A) (1,B) will cause an exception!

training_data_one = sortBy sndCompare [(1,B), (2,A), (-3,A), (4,B), (4,B)]
identity          = sortBy sndCompare [(1,B), (2,A), (-3,A), (4,B), (4,B)]

a       = growTree training_data_one giniIndex
aPrint  = printTree a
b       = pruneTree a identity
bPrint  = printTree b


training_data_two = sortBy sndCompare [(-1,A), (2,B), (3,B), (4,B), (-4,B), (11,A), (12,A), (13,A), (14,A), (19,A)]
test_data         = sortBy sndCompare [(1,B), (2,B), (3,B)]

c       = growTree training_data_two giniIndex
cPrint  = printTree c
d       = pruneTree c test_data 
dPrint  = printTree d

dps = [1,99,201,156,5,-5,-9,-100,0]

predictThese = predict dps b

rFS = randomForest training_data_one giniIndex
betterAcc = average (map average (map (predict dps) rFS))
averages = (map average (map (predict dps) rFS))

randomForest :: [DataPoint] -> ImpurityMeasure -> [DTree Split LeafData]
randomForest dps imp = map f uniformSample
    where uniformSample = uniform dps 100000
          f             = (flip growTree) imp

uniform :: [DataPoint] -> Int -> [[DataPoint]]
uniform dps x = replicate x dps

average :: [Class] -> Class
average xs = if as < bs then B else A
    where as = length (filter (\x -> x == A) xs)
          bs = length (filter (\x -> x == B) xs)

growTree :: [DataPoint] -> ImpurityMeasure -> DTree Split LeafData
growTree [] impurity = Leaf (ErrorClass, [])
growTree input_data impurity
    | length (input_data) == 0 = Leaf (ErrorClass, [])
    | isHomogenous == True     = Leaf (snd(isLeaf), input_data)  
    | otherwise                = Branch (LT, num_split) (growTree (fst data_split) impurity) (growTree (snd data_split) impurity)
        where isHomogenous     = fst(isLeaf)
              isLeaf           = isDataHomogenous input_data
              data_split       = bestSplit input_data impurity
              num_split        = (fromIntegral (fst (last (fst data_split))) + fromIntegral (fst (head (snd data_split)))) / 2

-- Is leaf homogenous - if so return whether it is and the class that should be predicted.
isDataHomogenous :: [DataPoint] -> (Bool, Class)
isDataHomogenous input_data 
    | (b_len == 0 && a_len >= 1) = (True, A)
    | (a_len == 0 && b_len >= 1) = (True, B)
    | otherwise  = (False, ErrorClass)
    where 
        a_len = classCount input_data A
        b_len = classCount input_data B


-- This returns count of class in data points.
classCount :: [DataPoint] -> Class -> Int
classCount input_data feature = length (filter (\x -> snd (x) == feature) (input_data))






-- This function is used to take a data point and predict it on the tree.

predict :: [Data1D] -> DTree Split LeafData -> [Class]
predict data_points tree = map (predictor tree) data_points

predictor :: DTree Split LeafData -> Data1D -> Class
predictor (Leaf l)                 x = fst l
predictor (Branch (LT, val) b1 b2) x = if x <= (ceiling val) then predictor b1 x else predictor b2 x
predictor _ _ = error "Other orderings have not yet been implemented."







printComprehension :: DTree Split LeafData -> Data1D -> IO()
printComprehension tree x = putStrLn ("\n\n\n" ++ comprehendTree tree x ++ "\n\n\n")



-- This outputs the conjunction of features that it used to classify the data point.
comprehendTree :: DTree Split LeafData -> Data1D -> String
comprehendTree (Leaf l)  x = "\n\n----------------------\n=>Predicted Class:" ++ show (fst l)
comprehendTree (Branch (LT, val) b1 b2) x = if x <= v then ((show x) ++ " is smaller than " ++ (show v) ++ " (Left)\n" ++ comprehendTree b1 x) 
                                            else ((show x) ++ " is larger than " ++ (show v) ++ " (Right)\n" ++ comprehendTree b2 x)
                        where 
                            v = (ceiling val)
comprehendTree _ _ = error "Other orderings have not yet been implemented."












-- The following is how to deal with the relevant splits:

-- This sorts the data then finds smallest impurity split. Returns this split and the number to split on in future.
bestSplit :: [DataPoint] -> ImpurityMeasure -> DataSplit
bestSplit input_data impurity = returnSplit input_data (splits !! (fromMaybe 1 (elemIndex (minimum impurities) impurities)))
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
splittingPoints input_data = intersect (filterMaybe diffAdjacent classes) (filterMaybe diffAdjacent values)
    where classes = (snd (unzip input_data))
          values  = (fst (unzip input_data))

-- This finds where there are changes in the data => where it makes sense to split. (Points on ROC curve)
diffAdjacent :: Eq a => [a] -> Maybe Bool
diffAdjacent input
    | length input == 2 = if (input!!0 == input!!1) then Just False else Just True
    | otherwise = Nothing

-- Filters out indices which do not represent non contiguous data elements.
filterMaybe :: ([a] -> Maybe Bool) -> [a] -> [Int]
filterMaybe p xs = [ index | index <- [ 1 .. (length(xs) - 1) ], fromMaybe False (p (slice xs (index-1) (index + 1))) == True ]

-- This returns two elements from a list specified by two ints.
slice :: [a] -> Int -> Int -> [a]
slice xs lower upper = take (upper - lower) (drop lower xs)

returnSplit :: [DataPoint] -> Int -> DataSplit
returnSplit x n 
    | n == 0                  = ([], [])
    | n >= length(x)          = ([], [])
    | x == []                 = ([], [])
    | otherwise               = ((take (n) x), (drop (n) x))

-- This is the end of the part that deals with splits.



























pruneTree :: DTree Split LeafData -> [DataPoint] -> DTree Split LeafData
pruneTree tree test_data = modify (recurseAndPrune tree test_data)

-- The parameters that this tree returns are:
--(total_correct, totalA, totalB, Datapoints)
recurseAndPrune :: DTree Split LeafData -> [DataPoint] -> (DTree Split LeafData, (Int, Int, Int, [DataPoint]))
recurseAndPrune (Branch s (Leaf x) (Leaf y)) test_data = if node_acc < maj_accuracy then new_leaf else same_branch
    where
            -- Accuracy measure
            node_acc      = accuracy leaves_correct  total

            -- Test data
            totalTest       = totalATest + totalBTest
            totalATest      = (getTotalClass test_data A)
            totalBTest      = (getTotalClass test_data B)
            maj_accuracy    = accuracy maj_class_num totalTest

            -- Majority class measures on test data
            maj_class       = if totalATest > totalBTest then A else B
            maj_class_num   = if maj_class == A then totalATest else totalBTest

            -- Things to be returned:
            total           = totalA + totalB
            totalA          = (getTotalClass (snd x) A) + (getTotalClass (snd y) A)
            totalB          = (getTotalClass (snd x) B) + (getTotalClass (snd y) B)
            leaves_correct  = (leafAccTestData (Leaf x) test_data + leafAccTestData (Leaf y) test_data)
            total_correct   = if node_acc < maj_accuracy then maj_class_num else leaves_correct 
            dps             = (snd x) ++ (snd y)
            new_leaf        = (Leaf (maj_class, dps),      return_tuple)
            same_branch     = (Branch s (Leaf x) (Leaf y), return_tuple)
            return_tuple    = (total_correct, totalA, totalB, dps)

recurseAndPrune (Branch s d2 (Leaf y)) test_data = if node_acc < maj_accuracy then new_leaf else same_branch
    where 
            -- Accuracy measure
            node_acc      = accuracy leaves_correct  total

            -- Things from below
            pTree         = snd (recurseAndPrune d2 test_data)

            -- Test data
            totalTest       = totalATest + totalBTest
            totalATest      = (getTotalClass test_data A)
            totalBTest      = (getTotalClass test_data B)
            maj_accuracy    = accuracy maj_class_num totalTest

            -- Majority class measures on test data
            maj_class       = if totalATest > totalBTest then A else B
            maj_class_num   = if maj_class == A then totalATest else totalBTest

            -- Things to be returned:
            total           = totalA + totalB
            totalA          = second pTree + getTotalClass (snd y) A
            totalB          = thd    pTree + getTotalClass (snd y) B
            leaves_correct  = (leafAccTestData (Leaf y) test_data) + first pTree
            total_correct   = if node_acc < maj_accuracy then maj_class_num else leaves_correct 
            dps             = (fth pTree) ++ (snd y)
            new_leaf        = ((Leaf (maj_class, dps)),  return_tuple)
            same_branch     = ((Branch s d2 (Leaf y) ),  return_tuple)
            return_tuple    = (total_correct, totalA, totalB, dps)

recurseAndPrune (Branch s (Leaf y) d2) test_data = if node_acc < maj_accuracy then new_leaf else same_branch
    where 
            -- Accuracy measure
            node_acc      = accuracy leaves_correct  total

            -- Things from below
            pTree         = snd (recurseAndPrune d2 test_data)

            -- Test data
            totalTest       = totalATest + totalBTest
            totalATest      = (getTotalClass test_data A)
            totalBTest      = (getTotalClass test_data B)
            maj_accuracy    = accuracy maj_class_num totalTest

            -- Majority class measures on test data
            maj_class       = if totalATest > totalBTest then A else B
            maj_class_num   = if maj_class == A then totalATest else totalBTest

            -- Things to be returned:
            total           = totalA + totalB
            totalA          = second pTree + getTotalClass (snd y) A
            totalB          = thd    pTree + getTotalClass (snd y) B
            leaves_correct  = (leafAccTestData (Leaf y) test_data) + first pTree
            total_correct   = if node_acc < maj_accuracy then maj_class_num else leaves_correct 
            dps             = (fth pTree) ++ (snd y)
            new_leaf        = ((Leaf (maj_class, dps)),  return_tuple)
            same_branch     = ((Branch s (Leaf y) d2 ),  return_tuple)
            return_tuple    = (total_correct, totalA, totalB, dps)


recurseAndPrune (Branch s d1 d2) test_data = if node_acc < maj_accuracy then new_leaf else same_branch
    where 
            -- Accuracy measures
            node_acc        = accuracy leaves_correct total

            -- Things from below
            pTree           = componentWisePlus (snd (recurseAndPrune d1 test_data)) (snd (recurseAndPrune d2 test_data))

            -- Test data
            totalTest       = totalATest + totalBTest
            totalATest      = (getTotalClass test_data A)
            totalBTest      = (getTotalClass test_data B)
            maj_accuracy    = accuracy maj_class_num totalTest
            
            -- Majority class measures on test data
            maj_class       = if totalATest > totalBTest then A else B
            maj_class_num   = if maj_class == A then totalATest else totalBTest

            -- Things to be returned:
            total           = totalA + totalB
            totalA          = second pTree
            totalB          = thd    pTree
            leaves_correct  = first pTree
            total_correct   = if node_acc < maj_accuracy then maj_class_num else leaves_correct 
            dps             = fth pTree
            new_leaf        = ((Leaf (maj_class, dps)), return_tuple)
            same_branch     = ((Branch s d1 d2 )      , return_tuple)
            return_tuple    = (total_correct, totalA, totalB, dps)

recurseAndPrune _ _ = error "Tree cannot be pruned."

modify :: (DTree Split LeafData, (Int, Int, Int, [DataPoint])) -> DTree Split LeafData
modify ((Leaf x), tuple        ) = (Leaf x)
modify ((Branch s d1 d2), tuple) = (Branch s d1 d2)

componentWisePlus :: (Int, Int, Int, [DataPoint]) -> (Int, Int, Int, [DataPoint]) -> (Int, Int, Int, [DataPoint])
componentWisePlus (a, b, c, d) (e, f, g, h) = (a + b, b + f, c + g, d ++ h)

getTotal :: DTree Split LeafData -> Int
getTotal (Leaf x) = length (snd x)

leafAccTestData :: DTree Split LeafData -> [DataPoint] -> Int
leafAccTestData (Leaf x) dps = classCount dps (fst x)

getTotalClass :: [DataPoint] -> Class -> Int
getTotalClass xs c = fromIntegral (classCount xs c)

numCorrect :: (Integral a) => Class -> [DataPoint] -> a
numCorrect c dps = fromIntegral (length (filter (\x -> (snd x) == c) dps))

accuracy :: Int -> Int -> Double
accuracy correct total =  (fromIntegral (correct))  /  (fromIntegral (total))

first :: (a,b,c,d) -> a
first (v,b,x,z) = v

second :: (a,b,c,d) -> b
second (v,b,x,z) = b

thd :: (a,b,c,d) -> c
thd (v,b,x,z) = x

fth :: (a,b,c,d) -> d
fth (v,b,x,z) = z 












-- The following deals with how to calculate the impurity of the splits.

giniIndex ::ImpurityMeasure
giniIndex ds = (calcGini (fst ds) total_length) + (calcGini (snd ds) total_length)
    where total_length = length ( fst ds ) + length ( snd ds )

calcGini :: [DataPoint] -> Int -> Double
calcGini [] _ = error "Empty List"
calcGini dps len = (total/total_length) * (bfrac*(1-bfrac) + afrac*(1-afrac))
    where
        total_length = (fromIntegral len)
        bfrac        = (bs/total)
        afrac        = (as/total)
        bs           = fromIntegral(classCount dps B)
        as           = fromIntegral(classCount dps A)
        total        = fromIntegral((classCount dps B) + (classCount dps A))







replicateWithLines :: Int -> Int -> Char -> String
replicateWithLines 0 _ _ = []
replicateWithLines n divider char = replicateWithLines (n - 1) divider char ++ (replicate (divider - 1) ' ') ++ [char]

printSplit :: Split -> String
printSplit (ord, val) = ineq ++ show val
    where
        ineq = if ord == LT then "<= " else "> "



prettyPrintTree :: DTree Split LeafData -> Int -> String
prettyPrintTree (Leaf x) n = (replicateWithLines n 4 '|') ++ ( "Leaf : " ++ show (fst x) ++ (replicate 10 ' ') ++ "as:" ++ [intToDigit as]  ++ " bs:" ++ [intToDigit bs] ++ "\n" )
    where as = getTotalClass (snd x) A
          bs = getTotalClass (snd x) B
prettyPrintTree (Branch sp t1 t2) n = (replicateWithLines n 4 '|') ++ ( "Br " ++ printSplit sp ++ "\n" ) ++ prettyPrintTree t1 (n+1) ++ prettyPrintTree t2 (n+1)

printTree :: DTree Split LeafData -> IO()
printTree tree = putStrLn ("\n\nDecision Tree Visualisation\n\n\n\n" ++ prettyPrintTree tree 1 ++ "\n\n\n\nEnd of Visualisation \n\nTo see data points please see code. \n\n")



-- If you want to print the data points at the leaf:

printDPS :: LeafData -> [String]
printDPS (cl, dps) = map dpToString dps

dpToString :: DataPoint -> String
dpToString (val, cl) = " " ++ [intToDigit val] ++ "," ++ show cl ++ " "























