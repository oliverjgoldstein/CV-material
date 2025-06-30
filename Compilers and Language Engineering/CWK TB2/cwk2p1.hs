--data Aexp = N Num | V Var | Add Aexp Aexp | Mult Aexp Aexp | Sub Aexp Aexp deriving (Show, Eq, Read)
--data Bexp = TRUE | FALSE | Eq Aexp Aexp | Le Aexp Aexp | Neg Bexp | And Bexp Bexp deriving (Show, Eq, Read)
--data Stm  = Ass Var Aexp | Skip | Comp Stm Stm | If Bexp Stm Stm | While Bexp Stm deriving (Show, Eq, Read)

--subst_aexp :: Aexp -> Var -> Aexp -> Aexp
--subst_aexp (N n) _ _ = (N n)
--subst_aexp (V x) variable aexp1
--                | x == variable = aexp1
--                | otherwise = (V x)
--subst_aexp (Add (aexp1) (aexp2)) variable replace = Add (subst_aexp aexp1 variable replace) (subst_aexp aexp2 variable replace)
--subst_aexp (Mult (aexp1) (aexp2)) variable replace = Mult (subst_aexp aexp1 variable replace) (subst_aexp aexp2 variable replace)
--subst_aexp (Sub (aexp1) (aexp2)) variable replace = Sub (subst_aexp aexp1 variable replace) (subst_aexp aexp2 variable replace)

--subst_bexp :: Bexp -> Var -> Aexp -> Bexp 
--subst_bexp TRUE _ _ = TRUE
--subst_bexp FALSE _ _ = FALSE
--subst_bexp (Eq (aexp1)(aexp2)) variable replace = Eq (subst_aexp aexp1 variable replace) (subst_aexp aexp2 variable replace)
--subst_bexp (Le (aexp1)(aexp2)) variable replace = Le (subst_aexp aexp1 variable replace) (subst_aexp aexp2 variable replace)
--subst_bexp (Neg (bexp1)) variable replace = subst_bexp bexp1 variable replace
--subst_bexp (And (bexp1) (bexp2)) variable replace = And (subst_bexp bexp1 variable replace) (subst_bexp bexp2 variable replace)

--s_ds :: Stm -> State -> State
--s_ds (Ass var aexp1) s      = update s (a_val (aexp1) (s)) var
--s_ds (Skip) s               = s
--s_ds (Comp stm1 stm2) s     = ((s_ds stm1) . (s_ds stm2)) s
--s_ds (If bexp1 stm1 stm2) s = cond (b_val bexp1, s_ds stm1, s_ds stm2) s
--s_ds (While bexp1 stm1) s   = fix f s where f g = cond (b_val bexp1, g . s_ds stm1, id)

import Prelude hiding (Num)
import qualified Prelude (Num)

type Num = Integer
type Var = String
type Z = Integer
type T = Bool
type State = Var -> Z
type Input  = [Integer]  -- to denote the values read by a program
type Output = [String]   -- to denote the strings written by a program
type IOState = (Input,Output,State)  -- to denote the combined inputs, outputs and state of a program

data Aexp = N Num | V Var | Add Aexp Aexp | Mult Aexp Aexp | Sub Aexp Aexp deriving (Show, Eq, Read)
data Bexp = TRUE | FALSE | Eq Aexp Aexp | Le Aexp Aexp | Neg Bexp | And Bexp Bexp deriving (Show, Eq, Read)
data Stm  = Ass Var Aexp | Skip | Comp Stm Stm | If Bexp Stm Stm | While Bexp Stm 
          | Read Var       -- for reading in the value of a variable
          | WriteA Aexp    -- for writing out the value of an arithmetic expression
          | WriteB Bexp    -- for writing out the value of a Boolean expression
          | WriteS String  -- for writing out a given string
          | WriteLn        -- for writing out a string consisting of a newline character
          deriving (Show, Eq, Read)


fv_stm :: Stm -> [Var]
fv_stm (Skip) = []
fv_stm (Ass (var) (aexp1))  = union [var] (fv_aexp (aexp1))
fv_stm (Comp stm1 stm2)     = union (fv_stm (stm1)) (fv_stm (stm2))
fv_stm (If bexp1 stm1 stm2) = union ( union ( fv_bexp (bexp1) ) ( fv_stm (stm1) ) ) ( fv_stm (stm2) )
fv_stm (While bexp1 stm1)   = union ( fv_bexp (bexp1) ) ( fv_stm (stm1) )
fv_stm (Read var)           = [var]
fv_stm (WriteA aexp1)       = fv_aexp (aexp1)
fv_stm (WriteB bexp1)       = fv_bexp (bexp1)
fv_stm (WriteS string)      = []
fv_stm (WriteLn)            = []

fv_aexp :: Aexp -> [Var]
fv_aexp (N n)                   = []
fv_aexp (V x)                   = [x]
fv_aexp (Add (aexp1) (aexp2))   = union (fv_aexp(aexp1)) (fv_aexp(aexp2))
fv_aexp (Mult (aexp1) (aexp2))  = union (fv_aexp(aexp1)) (fv_aexp(aexp2))
fv_aexp (Sub (aexp1) (aexp2))   = union (fv_aexp(aexp1)) (fv_aexp(aexp2))

fv_bexp :: Bexp -> [Var]
fv_bexp TRUE                    = []
fv_bexp FALSE                   = []
fv_bexp (Eq (aexp1) (aexp2))    = union (fv_aexp(aexp1)) (fv_aexp(aexp2))
fv_bexp (Le (aexp1) (aexp2))    = union (fv_aexp(aexp1)) (fv_aexp(aexp2))
fv_bexp (Neg bexp1)             = fv_bexp (bexp1)
fv_bexp (And (bexp1) (bexp2))   = union (fv_bexp(bexp1)) (fv_bexp(bexp2))

a_val::Aexp -> State -> Z
a_val (N n) s                   = n
a_val (V x) s                   = s x
a_val (Add (aexp1) (aexp2)) s   = a_val (aexp1) s + a_val (aexp2) s
a_val (Mult (aexp1) (aexp2)) s  = a_val (aexp1) s * a_val (aexp2) s
a_val (Sub (aexp1) (aexp2)) s   = a_val (aexp1) s - a_val (aexp2) s

b_val::Bexp->State->T
b_val TRUE _                    = True
b_val FALSE _                   = False
b_val (Eq (aexp1) (aexp2)) s    = (a_val (aexp1) s) == (a_val (aexp2) s)
b_val (Le (aexp1) (aexp2)) s    = (((a_val (aexp1) s) - (a_val (aexp2) s)) <= 0)
b_val (Neg (bexp1)) s           = (if ((b_val bexp1 s) == True) then False else True)
b_val (And (bexp1) (bexp2)) s   = (b_val bexp1 s) && (b_val bexp2 s)

union :: Eq a => [a] -> [a] -> [a]
union [] []      = []
union [] [x]     = [x]
union [x] []     = [x]
union (x:xs) []  = (union xs [x])
union [] (x:xs)  = (union [x] xs)

union [x] [y]
    | x /= y = y:x:[]
    | otherwise = [y]

union [x] (y:ys)
    | (x `elem` (y:ys))   = union [y] ys
    | otherwise           = [x] ++ (union [y] ys)

union (y:ys) [x]
    | (x `elem` (y:ys)) = union ys [y]
    | otherwise         = [x] ++ (union ys [y])

union (x:xs) (y:ys)
    | (x `elem` (y:ys)) = union (y:ys) xs
    | (y `elem` (x:xs)) = union (x:xs) ys
    | (y `elem` ys)     = union (x:xs) ys
    | (x `elem` xs)     = union (y:ys) xs
    | otherwise         = [x] ++ [y] ++ union xs ys

cond :: (a->T, a->a, a->a) -> (a->a)
cond (p, g1, g2) s
    | p s == True   = g1 s
    | otherwise     = g2 s

fix :: (a->a) -> a
fix f = f (fix f)

update :: State -> Z -> Var -> State
update state intReplacement varToBeUpdated programVariable
    | varToBeUpdated == programVariable = intReplacement
    | otherwise = state programVariable



xt::Stm
xt = (Comp (Comp (Comp (Comp (Comp (Comp (Comp (Comp (WriteS "Enter a number: ") (Read "i")) (WriteS "Enter a number: ")) (Read "x")) (WriteS "First is ")) (WriteA (V  "i"))) (WriteS "; second is ")) (WriteA (V  "x"))) WriteLn)
fac :: Stm
fac = 

    Comp (WriteS "Factorial Calculator")

        (Comp (WriteLn)

            (Comp (WriteS "Enter a number: ")

                (Comp (Read ("x"))
    
                    (Comp (WriteS "Factorial of ")
    
                        (Comp (WriteA (V "x"))
    
                            (Comp (WriteS ("is ")) 
    
                                (Comp (Ass "y" (N 1) )
                                
                                (Comp

                                (While ( Neg ( Eq (V "x") (N 1) ) )
                            
                                    (Comp 
    
                                        (Ass "y" 
                                            (Mult 
                                                (V "y") 
                                                (V "x")
                                            )
                                        ) 
                                        
                                        (Ass "x"
                                                (Sub 
                                                    (V "x") 
                                                    (N 1)
                                                )
                                            )
                                        )
                                    )
                                        (Comp
        
                                            (WriteA (V "y"))
        
                                            (Comp (WriteLn) (WriteLn)
        
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )



pow :: Stm
pow = 

    Comp (WriteS "Exponential Calculator")

        (Comp (WriteLn)

            (Comp (WriteS "Enter base: ")

                (Comp (Read ("base"))

                    (Comp

                        (If (Le (N 1) (V "base"))
    
                            (Comp (WriteS "Enter exponent: ")
    
                                (Comp (Read "exponent")
    
                                    (Comp (Ass ("num") (N 1))
    
                                        (Comp (Ass ("count") (V "exponent"))
    
                                            (Comp (While (Le (N 1) (V "count"))
    
                                                (Comp (Ass ("num") ((Mult (V "num") (V "base"))))
    
                                                    (Ass ("count") (Sub (V "count") (N 1)))
                                                            
                                                        
                                                    )
                                                )
    
                                            (Comp (WriteA (V "base"))
    
                                                (Comp (WriteS " raised to the power of ")
    
                                                    (Comp (WriteA (V "exponent"))
    
                                                        (Comp (WriteS " is ")
    
                                                            (WriteA (V "num"))

                                                            )
                                                        )
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )

                        (Comp
                                (WriteS "Invalid ") 
                                (WriteA (V "base"))
                        )
                    )
    
                    (WriteLn)
                )
            )
        )
    )


state_component :: IOState -> State
state_component (i, o, s) = s

input_component :: IOState -> Input
input_component (i, o, s) = i

output_component :: IOState -> Output
output_component (i, o, s) = o

boolean_evaluation :: Bexp -> IOState -> T
boolean_evaluation bexp1 (i, o, s) = b_val bexp1 s


s_ds :: Stm -> IOState -> IOState
s_ds (Ass var aexp1)        ( i, o, s ) = ( i , o, update s ( a_val ( aexp1) (s) ) var )
s_ds (Skip)                 ( i, o, s ) = ( i, o, s )
s_ds (Comp stm1 stm2)       ( i, o, s ) = ((s_ds stm2) . (s_ds stm1)) (i, o, s)
s_ds (If bexp1 stm1 stm2)   iostate     = cond (boolean_evaluation bexp1, s_ds stm1, s_ds stm2) iostate
s_ds (While bexp1 stm1)     ( i, o, s ) = fix f (i, o, s) where f g = cond (boolean_evaluation bexp1, g . ( s_ds stm1 ), s_ds (Skip))
s_ds (Read variable)      (input:tail, o, s) = ( tail, o ++ [ "<" ++ show(input) ++ ">" ], state_component (s_ds (Ass variable (N input)) (tail, o, s )))
s_ds (WriteA aexp1)         ( i, o, s ) = ( i, o ++ [ show ( a_val aexp1 s ) ], s )
s_ds (WriteB bexp1)         ( i, o, s ) = ( i, o ++ [ show ( b_val bexp1 s ) ], s )
s_ds (WriteS string_input)  ( i, o, s ) = ( i, o ++ [ string_input ], s ) 
s_ds WriteLn                ( i, o, s ) = ( i, o ++ [ "\n" ], s )

eval :: Stm -> IOState -> (Input, Output, [Var], [Num])
eval stm1 iostate = ( input_component(s_ds stm1 iostate), output_component(s_ds stm1 iostate), fv_stm( stm1 ), number_list_from_variable_list (fv_stm( stm1 )) (s_ds stm1 iostate))

number_list_from_variable_list :: [Var] -> IOState -> [Num]
number_list_from_variable_list []     (i, o, s)  = []
number_list_from_variable_list (x:xs) (i, o, s)  = [ ( from_Integer (a_val ( V x ) s) ) ] ++ ( number_list_from_variable_list ( xs ) (i, o, s) )

from_Integer :: Integer -> Num 
from_Integer integer = integer


--a = fv_aexp (Sub (V "o") (Mult (V "o") (V "o")))
--b = fv_aexp (Sub (V "y") (Mult (N 6) (V "y")))
--c = fv_aexp (Sub (V "y") (V "u"))

--s::State
--s "o" = 3
--s _ = 0

--d = s_ds (Skip) ([1],[],s)

--d = subst_aexp (Add (V "x") (Sub (V "t") (V "d"))) "t" (Sub (V "f") (N 7))

--Read - for reading in the value of a variable;
--WriteA - for writing out the value of an arithmetic expression;
--WriteB - for writing out the value of a Boolean expression;
--WriteS - for writing out a given string;
--WriteLn - for writing out a string consisting of a newline character.
--Second, the following type synonyms are added in order to represent the
--values that are read into or written out by the program:
--Input - to denote the values read by a program;
--Output - to denote the strings written by a program;
--IOState - to denote the combined inputs, outputs and state of a program.









                    


































