{-# LANGUAGE MultiParamTypeClasses #-}
{-# LANGUAGE TypeOperators #-}
{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE FlexibleContexts #-}
{-# LANGUAGE ScopedTypeVariables #-}
{-# LANGUAGE OverlappingInstances #-}
{-# LANGUAGE InstanceSigs #-}


data Expr f = In (f (Expr f))

infixl 6 ⊕
infixl 7 ⊗

data Val e = Val Int
type IntExpr = Expr Val

data Add e = Add e e
type AddExpr = Expr Add

data Mul e = Mul e e

instance Functor Mul where
    fmap :: (a -> b) -> Mul a -> Mul b
    fmap f (Mul x y) = Mul (f x) (f y)

data CoProduct f g e = Inl (f e) | Inr (g e)

instance Functor Val where
    fmap :: (a -> b) -> Val a -> Val b
    fmap f (Val x) = Val x
instance Functor Add where
    fmap :: (a -> b) -> Add a -> Add b
    fmap f (Add e1 e2) = Add (f e1) (f e2)

instance (Functor f , Functor g) => Functor (CoProduct f g) where
    fmap :: (Functor f, Functor g) => (a -> b) -> CoProduct f g a -> CoProduct f g b
    fmap f (Inl e1) = Inl (fmap f e1)
    fmap f (Inr e2) = Inr (fmap f e2)

foldExpr :: Functor f => (f a -> a) -> Expr f -> a
foldExpr f (In t) = f (fmap (foldExpr f ) t)

class Functor f => Eval f where
    evalAlgebra :: f Int -> Int

instance Eval Val where
    evalAlgebra :: Val Int -> Int
    evalAlgebra (Val x) = x
instance Eval Add where
    evalAlgebra :: Add Int -> Int
    evalAlgebra (Add x y) = x + y
instance Eval Mul where
    evalAlgebra :: Mul Int -> Int
    evalAlgebra (Mul x y) = x * y

instance (Eval f ,Eval g) => Eval (CoProduct f g) where
    evalAlgebra :: (Eval f, Eval g) => CoProduct f g Int -> Int
    evalAlgebra (Inl x) = evalAlgebra x
    evalAlgebra (Inr y) = evalAlgebra y

eval :: Eval f => Expr f -> Int
eval expr = foldExpr evalAlgebra expr

class (Functor sub, Functor sup) => sub `To` sup where
    inj :: sub a -> sup a

instance Functor f => f `To` f where
    inj :: Functor f => f a -> f a
    inj = id

instance (Functor f, Functor g) => f `To` (CoProduct f g) where
    inj :: (Functor f, Functor g) => f a -> CoProduct f g a
    inj = Inl

instance (Functor f, Functor g, Functor h, f `To` g) => f `To` (CoProduct h g) where
    inj :: (Functor f, Functor g, Functor h, To f g) => f a -> CoProduct h g a
    inj = Inr . inj


inject :: (g `To` f) => g (Expr f) -> Expr f
inject = In . inj

val :: (Val `To` f) => Int -> Expr f
val x = inject (Val x)

(⊕) :: (Add `To` f) => Expr f -> Expr f -> Expr f
x ⊕ y = inject (Add x y)

(⊗) :: (To Mul f) => Expr f -> Expr f -> Expr f
x ⊗ y = inject (Mul x y)








class Render f where
    render :: Render g => f (Expr g) -> String

pretty :: Render f => Expr f -> String
pretty (In t) = render t

instance Render Val where
    render :: Render g => Val (Expr g) -> String
    render (Val i) = show i
instance Render Add where
    render :: Render g => Add (Expr g) -> String
    render (Add x y) = "(" ++ pretty x ++ "+" ++ pretty y ++ ")"
instance Render Mul where
    render :: Render g => Mul (Expr g) -> String
    render (Mul x y) = "(" ++ pretty x ++ "*" ++ pretty y ++ ")"
instance (Render f , Render g) => Render (CoProduct f g) where
    render :: (Render f, Render g, Render g1) => CoProduct f g (Expr g1) -> String
    render (Inl x) = render x
    render (Inr y) = render y














data Term f a = Pure a | Impure (f (Term f a))

instance Functor f => Functor (Term f ) where
    fmap :: Functor f => (a -> b) -> Term f a -> Term f b
    fmap f (Pure x) = Pure (f x)
    fmap f (Impure t) = Impure (fmap (fmap f) t)


instance Functor f => Applicative (Term f) where
    pure :: Functor f => a -> Term f a
    pure = Pure
    (<*>) :: Functor f => Term f (a -> b) -> Term f a -> Term f b
    Pure f <*> Pure x = Pure (f x) -- If both are pure, just apply the function
    Pure f <*> Impure x = Impure (fmap (fmap f) x) -- Apply pure function inside Impure
    Impure f <*> x = Impure (fmap (<*> x) f) -- Apply inside Impure, distributing over the structure

instance Functor f => Monad (Term f) where
    return :: Functor f => a -> Term f a
    return x = Pure x
    (>>=) :: Functor f => Term f a -> (a -> Term f b) -> Term f b
    (Pure x) >>= f = f x
    (Impure t) >>= f = Impure (fmap (>>=f ) t)






data Incr t = Incr Int t
data Recall t = Recall (Int -> t)



injectT :: (g `To` f ) => g (Term f a) -> Term f a
injectT = Impure . inj
incr :: (Incr `To` f ) => Int -> Term f ()
incr i = injectT (Incr i (Pure ()))
recall :: (Recall `To` f ) => Term f Int
recall = injectT (Recall Pure)

tick :: Term (CoProduct Incr Recall) Int
tick = do 
    y <- recall
    incr 1
    return y

foldTerm :: Functor f => (a -> b) -> (f b -> b) -> Term f a -> b
foldTerm pure imp (Pure x) = pure x
foldTerm pure imp (Impure t) = imp (fmap (foldTerm pure imp) t)

newtype Mem = Mem Int deriving Show

instance Functor Incr where
    fmap :: (a -> b) -> Incr a -> Incr b
    fmap f (Incr i t) = Incr i (f t)

instance Functor Recall where
    fmap :: (a -> b) -> Recall a -> Recall b
    fmap f (Recall r) = Recall (f . r)

class Functor f => Run f where
    runAlgebra :: f (Mem -> (a, Mem)) -> (Mem -> (a, Mem))

instance Run Incr where
    runAlgebra :: Incr (Mem -> (a, Mem)) -> Mem -> (a, Mem)
    runAlgebra (Incr k r) (Mem i) = r (Mem (i + k))

instance Run Recall where
    runAlgebra :: Recall (Mem -> (a, Mem)) -> Mem -> (a, Mem)
    runAlgebra (Recall r) (Mem i) = r i (Mem i)

instance (Run f , Run g) => Run (CoProduct f g) where
    runAlgebra :: (Run f, Run g) => CoProduct f g (Mem -> (a, Mem)) -> Mem -> (a, Mem)
    runAlgebra (Inl r) = runAlgebra r
    runAlgebra (Inr r) = runAlgebra r

run :: Run f => Term f a -> Mem -> (a, Mem)
run = foldTerm ( ,) runAlgebra



data Teletype a = GetChar (Char -> a) | PutChar Char a

instance Functor Teletype where
    fmap :: (a -> b) -> Teletype a -> Teletype b
    fmap f (GetChar next) = GetChar (f . next)
    fmap f (PutChar c next) = PutChar c (f next)

data FileSystem a = ReadFile FilePath (String -> a) | WriteFile FilePath String a

exec :: Exec f => Term f a -> IO a
exec = foldTerm return execAlgebra

class Functor f => Exec f where
    execAlgebra :: f (IO a) -> IO a
    
instance Exec Teletype where
    execAlgebra :: Teletype (IO a) -> IO a
    execAlgebra (GetChar f ) = Prelude.getChar >>= f
    execAlgebra (PutChar c io) = Prelude.putChar c >> io


cat :: FilePath -> Term (CoProduct Teletype FileSystem) ()
cat fp = do
    contents <- readFile fp
    mapM putChar contents
    return ()