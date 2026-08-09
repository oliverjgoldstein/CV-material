{-# LANGUAGE Rank2Types #-}
type Constant = Int
type Operand  = Int
type Gene     = (Constant, Operand, Constant, Operand, Constant, Operand, Constant)
type Op       = (forall a. Num a => a -> a -> a)
type Func     = (Double -> Op -> Double -> Op -> Double -> Op -> Double)

find_Error    :: [Func] -> [Double] -> [Double]
eval_Fitness  :: [Func] -> [(Gene, Double)]
conv_To_Gene  :: [Func] -> [Gene]
conv_To_Func  :: [Gene] -> [Gene]
tournament    :: [(Gene, Double)] -> [Gene]
epoch         :: [Double] -> [Gene]

main::IO()
main = do xs <- getFromFile
