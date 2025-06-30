import Prelude hiding (Num)
import qualified Prelude (Num)
type Num = Integer
type Var = String
data Aexp = N Num | V Var | Add Aexp Aexp | Mult Aexp Aexp | Sub Aexp Aexp | Uneg Aexp  deriving (Show, Eq, Read)

type Z = Integer

a = Mult (Add (V "x") (V "y")) (Sub (V "z") (N 1))

n_val :: Num -> Z
n_val x = x

type State = Var -> Z

s::State
s "x" = 1
s "y" = 2
s "z" = 3
s _ = 0

a_val::Aexp -> State -> Z
a_val (N n) s = n
a_val (V x) s = s x
a_val (Add (aexp1) (aexp2)) s = a_val (aexp1) s + a_val (aexp2) s
a_val (Mult (aexp1) (aexp2)) s = a_val (aexp1) s * a_val (aexp2) s
a_val (Sub (aexp1) (aexp2)) s = a_val (aexp1) s - a_val (aexp2) s
a_val (Uneg (aexp1)) s = 0 - a_val (aexp1) s


data Bexp = TRUE | FALSE | Eq Aexp Aexp | Le Aexp Aexp | Neg Bexp | And Bexp Bexp

b::Bexp
b = Neg (Eq (Add (V x) (V y)) (N 4))

type T = Bool

f::State
f True = True

b_val::Bexp->State->T
b_val TRUE _ = True
b_val FALSE _ = False
b_val (Add (aexp1) (aexp2)) s
b_val (Eq (aexp1) (aexp2)) s = (a_val (aexp1) s) == (a_val (aexp2) s)
b_val (Le (aexp1) (aexp2)) s = (((a_val (aexp1) s) - (a_val (aexp2) s)) <= 0)
b_val (Neg (bexp1) s = (if b_val bexp1 s == True then False else True)
b_val (And (bexp1) (bexp2)) s = (b_val bexp1 s) && (b_val bexp2 s)

