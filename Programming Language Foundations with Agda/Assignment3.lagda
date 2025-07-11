---
title     : "Assignment3: TSPL Assignment 3"
layout    : page
permalink : /Assignment3/
---

\begin{code}
module plfa.Assignment3 where
\end{code}

## YOUR NAME AND EMAIL GOES HERE
Oliver Goldstein s1424164@ed.ac.uk
## Introduction

This assignment is due **4pm Thursday 1 November** (Week 7).

You must do _all_ the exercises labelled "(recommended)".

Exercises labelled "(stretch)" are there to provide an extra challenge.
You don't need to do all of these, but should attempt at least a few.

Exercises without a label are optional, and may be done if you want
some extra practice.

Submit your homework using the "submit" command.
Please ensure your files execute correctly under Agda!

## Imports

\begin{code}
import Relation.Binary.PropositionalEquality as Eq
open Eq using (_≡_; refl; cong; sym)
open Eq.≡-Reasoning using (begin_; _≡⟨⟩_; _≡⟨_⟩_; _∎)
open import Data.Bool.Base using (Bool; true; false; T; _∧_; _∨_; not)
open import Data.Nat using (ℕ; zero; suc; _+_; _*_; _∸_; _≤_; s≤s; z≤n)
open import Data.Nat.Properties using
  (+-assoc; +-identityˡ; +-identityʳ; *-assoc; *-identityˡ; *-identityʳ; *-comm)
open import Relation.Nullary using (¬_; Dec; yes; no)
open import Data.Product using (_×_) renaming (_,_ to ⟨_,_⟩)
open import Data.Empty using (⊥; ⊥-elim)
open import Function using (_∘_)
open import Level using (Level)
open import plfa.Relations using (_<_; z<s; s<s)
open import plfa.Isomorphism using (_≃_; ≃-sym; ≃-trans; _≲_; extensionality)
open plfa.Isomorphism.≃-Reasoning
open import plfa.Connectives using (_⊎_)
open import plfa.Lists using (List; []; _∷_; [_]; [_,_]; [_,_,_]; [_,_,_,_];
  _++_; reverse; map; foldr; sum; Any; All; _∈_; ++-assoc; here; there)
open import plfa.Lambda hiding (ƛ′_⇒_; case′_[zero⇒_|suc_⇒_]; μ′_⇒_; plus′)
open import plfa.Properties hiding (value?)
\end{code}

#### Exercise `reverse-++-commute` (recommended)

Show that the reverse of one list appended to another is the
reverse of the second appended to the reverse of the first.
\begin{code}

++-identityʳ : ∀ {A : Set} (xs : List A) → xs ++ [] ≡ xs
++-identityʳ [] =
  Eq.≡-Reasoning.begin
    [] ++ []
  ≡⟨⟩
    []
  Eq.≡-Reasoning.∎
++-identityʳ (x ∷ xs) =
  Eq.≡-Reasoning.begin
    (x ∷ xs) ++ []
  ≡⟨⟩
    x ∷ (xs ++ [])
  ≡⟨ cong (x ∷_) (++-identityʳ xs) ⟩
    x ∷ xs
  Eq.≡-Reasoning.∎



reverse-++-commute : ∀ {A : Set} (xs ys : List A)
    → reverse (xs ++ ys) ≡ reverse ys ++ reverse xs
    
reverse-++-commute [] (ys) =
  Eq.≡-Reasoning.begin
    reverse([] ++ ys)
  ≡⟨⟩
    reverse (ys)
  ≡⟨ sym (++-identityʳ (reverse ys)) ⟩
    (reverse ys) ++ []
  Eq.≡-Reasoning.∎
  
reverse-++-commute {A} (x ∷ xs) ys = 
  Eq.≡-Reasoning.begin
    reverse (x ∷ xs ++ ys)
  ≡⟨⟩
    reverse (x ∷ (xs ++ ys))
  ≡⟨⟩
    reverse (xs ++ ys) ++ [ x ]
  ≡⟨ cong (_++ [ x ]) (reverse-++-commute xs ys) ⟩
    (reverse ys ++ reverse xs) ++ [ x ]
  ≡⟨ ++-assoc (reverse ys) (reverse xs) [ x ] ⟩
    reverse ys ++ (reverse xs ++ [ x ])
  ≡⟨⟩
    reverse ys ++ (reverse (x ∷ xs))
  Eq.≡-Reasoning.∎

leftid++ : ∀ Gen (A : List Gen) → A ≡ A ++ []
leftid++ Gen [] = refl
leftid++ Gen (x ∷ xs) = cong (λ y → x ∷ y) (leftid++ Gen xs)



\end{code}

#### Exercise `reverse-involutive` (recommended)

A function is an _involution_ if when applied twice it acts
as the identity function.  Show that reverse is an involution.
\begin{code}

helper-lemma : ∀ {A : Set} (x : A) (ys : List A)
  → reverse (ys ++ [ x ]) ≡ [ x ] ++ reverse ys 

helper-lemma x [] = refl
helper-lemma x (y ∷ ys) = 
  Eq.≡-Reasoning.begin
    reverse ((y ∷ ys) ++ [ x ])
  ≡⟨⟩
    reverse (y ∷ (ys ++ [ x ]))
  ≡⟨⟩
    reverse (ys ++ [ x ]) ++ [ y ]
  ≡⟨ cong (_++ [ y ]) (helper-lemma x ys)  ⟩
    reverse ([ x ]) ++ reverse ys ++ [ y ]
  ≡⟨⟩
    [ x ] ++ reverse (y ∷ ys)
  Eq.≡-Reasoning.∎  

reverse-involutive : ∀ {A : Set} {xs : List A}
  → reverse (reverse xs) ≡ xs

reverse-involutive {a} {[]} =
  Eq.≡-Reasoning.begin
    reverse (reverse [])
  ≡⟨⟩
    []
  Eq.≡-Reasoning.∎

reverse-involutive {a} {(x ∷ xs)} = 
  Eq.≡-Reasoning.begin
    reverse (reverse (x ∷ xs))
  ≡⟨⟩
    reverse (reverse xs ++ [ x ])
  ≡⟨ helper-lemma x (reverse xs) ⟩
    [ x ] ++ reverse (reverse xs)
  --the reverse of a list move one over and t
  ≡⟨ cong ([ x ] ++_) (reverse-involutive) ⟩
    [ x ] ++ xs
  Eq.≡-Reasoning.∎

\end{code}

#### Exercise `map-compose`

Prove that the map of a composition is equal to the composition of two maps.
\begin{code}
postulate
  map-compose : ∀ {A B C : Set} {f : A → B} {g : B → C}
    → map (g ∘ f) ≡ map g ∘ map f
{-
helperi : ∀ {A B C : Set} {f : A → B} {g : B → C} (xs : List A) → map (g ∘ f) xs ≡ (map g ∘ map f) xs
helperi [] = refl
helperi {A} {B} {C} {f} {g} (x ∷ xs) = cong (λ y 
-}
\end{code}
The last step of the proof requires extensionality.

















helperi : ∀ {A B C : Set} {f : A → B} {g : B → C} xs → map (g ∘ f) xs ≡ (map g ∘ map f) xs
helperi [] = refl
helperi {A} {B} {C} {f} {g} (x ∷ xs) = cong (λ y → g(f x) ∷ y) (helperi xs)

map-compose {A} {B} {C} {f} {g} = extensionality helperi




#### Exercise `map-++-commute`

Prove the following relationship between map and append.
\begin{code}
postulate
  map-++-commute : ∀ {A B : Set} {f : A → B} {xs ys : List A}
   →  map f (xs ++ ys) ≡ map f xs ++ map f ys
\end{code}

#### Exercise `map-Tree`

Define a type of trees with leaves of type `A` and internal
nodes of type `B`.
\begin{code}
data Tree (A B : Set) : Set where
  leaf : A → Tree A B
  node : Tree A B → B → Tree A B → Tree A B
\end{code}
Define a suitabve map operator over trees.
\begin{code}
postulate
  map-Tree : ∀ {A B C D : Set}
    → (A → C) → (B → D) → Tree A B → Tree C D
\end{code}

#### Exercise `product` (recommended)

Use fold to define a function to find the product of a list of numbers.
For example,

    product [ 1 , 2 , 3 , 4 ] ≡ 24

\begin{code}

product : List ℕ → ℕ
product [] = 0
product (x ∷ xs) = foldr _*_ 1 (x ∷ xs)

\end{code}

#### Exercise `foldr-++` (recommended)

Show that fold and append are related as follows.
\begin{code}

foldr-++ : ∀ {A B : Set} (_⊗_ : A → B → B) (e : B) (xs ys : List A) →
    foldr _⊗_ e (xs ++ ys) ≡ foldr _⊗_ (foldr _⊗_ e ys) xs

foldr-++ operation e [] ys =
  Eq.≡-Reasoning.begin
    foldr operation e ([] ++ ys)
  ≡⟨⟩
    foldr operation e (ys)
  ≡⟨⟩
    foldr operation (foldr operation e ys) []
  Eq.≡-Reasoning.∎

foldr-++ operation e (x ∷ xs) ys =
  Eq.≡-Reasoning.begin
    foldr operation e ((x ∷ xs) ++ ys)
  ≡⟨⟩
    foldr operation e (x ∷ (xs ++ ys))
  ≡⟨⟩
    operation x (foldr operation e (xs ++ ys)) 
  ≡⟨ cong (operation x) (foldr-++ operation e xs ys) ⟩
    operation x (foldr operation (foldr operation e ys) xs)
  ≡⟨⟩
    (foldr operation (foldr operation e ys) (x ∷ xs))
  Eq.≡-Reasoning.∎

\end{code}


#### Exercise `map-is-foldr`

Show that map can be defined using fold.
\begin{code}
postulate
  map-is-foldr : ∀ {A B : Set} {f : A → B} →
    map f ≡ foldr (λ x xs → f x ∷ xs) []
\end{code}
This requires extensionality.

#### Exercise `fold-Tree`

Define a suitable fold function for the type of trees given earlier.
\begin{code}
postulate
  fold-Tree : ∀ {A B C : Set}
    → (A → C) → (C → B → C → C) → Tree A B → C
\end{code}

#### Exercise `map-is-fold-Tree`

Demonstrate an anologue of `map-is-foldr` for the type of trees.

#### Exercise `sum-downFrom` (stretch)

Define a function that counts down as follows.
\begin{code}
downFrom : ℕ → List ℕ
downFrom zero     =  []
downFrom (suc n)  =  n ∷ downFrom n
\end{code}
For example,
\begin{code}
_ : downFrom 3 ≡ [ 2 , 1 , 0 ]
_ = refl
\end{code}
Prove that the sum of the numbers `(n - 1) + ⋯ + 0` is
equal to `n * (n ∸ 1) / 2`.
\begin{code}
{-
*-distrib-+ : ∀ (m n p : ℕ) → (m + n) * p ≡ m * p + n * p
*-distrib-+ zero n p = refl
*-distrib-+ (suc m) n p rewrite (*-distrib-+ m n p) | (sym (+-assoc p (m * p) (n * p))) = refl

helper-lemma-two : ∀ (n : ℕ) → suc (suc (n ∸ 1)) * n ≡ (suc n) * (suc n ∸ 1)

sum-downFrom : ∀ (n : ℕ) → sum (downFrom n) * 2 ≡ n * (n ∸ 1)
sum-downFrom zero =
  Eq.≡-Reasoning.begin
    sum (downFrom zero) * 2
  ≡⟨⟩
    (sum []) * 2
  ≡⟨⟩
    sum []
  ≡⟨⟩
    0 * (0 ∸ 1)
  Eq.≡-Reasoning.∎

sum-downFrom (suc n) = -- sum is foldr (+) 0
  Eq.≡-Reasoning.begin
    sum (downFrom (suc n)) * 2
  ≡⟨⟩
    sum (n ∷ (downFrom n)) * 2
  ≡⟨⟩
    (n + (sum (downFrom n))) * 2
  ≡⟨ *-distrib-+ n (sum (downFrom n)) 2 ⟩
    (n * 2) + ((sum (downFrom n)) * 2)
  ≡⟨ cong (_+_ (n * 2)) (sum-downFrom n) ⟩
    (n * 2) + n * (n ∸ 1)
  ≡⟨⟩
    (n * (suc 1)) + n * (n ∸ 1)
  ≡⟨ cong (_+ (n * (n ∸ 1))) (*-comm n (suc 1)) ⟩
    ((suc 1) * n) + n * (n ∸ 1)
  ≡⟨⟩
    n + ((suc 0) * n) + n * (n ∸ 1)
  ≡⟨⟩
    n + (n + (0 * n)) + n * (n ∸ 1) 
  ≡⟨⟩
    n + (n + 0) + n * (n ∸ 1)
  ≡⟨ +-assoc n (n + 0) (n * (n ∸ 1)) ⟩
    n + ((n + 0) + n * (n ∸ 1))
  ≡⟨ cong (n +_) (Data.Nat.Properties.+-comm (n + 0) (n * (n ∸ 1))) ⟩
    n + ((n * (n ∸ 1)) + (n + 0))  
  ≡⟨ sym (+-assoc n (n * (n ∸ 1)) (n + 0)) ⟩
    (n + (n * (n ∸ 1))) + (n + 0)
  ≡⟨ cong (n + (n * (n ∸ 1)) +_) (Data.Nat.Properties.+-comm n 0) ⟩
    (n + (n * (n ∸ 1))) + (0 + n)
  ≡⟨⟩
    (n + n * (n ∸ 1)) + n
  ≡⟨ cong (_+ n) (cong (n +_) (*-comm n (n ∸ 1))) ⟩
    (n + (n ∸ 1) * n) + n
  ≡⟨⟩
    (suc (n ∸ 1) * n) + n
  ≡⟨ Data.Nat.Properties.+-comm (suc (n ∸ 1) * n) n ⟩
    n + (suc (n ∸ 1) * n)
  ≡⟨⟩
    suc (suc (n ∸ 1)) * n
  ≡⟨ helper-lemma-two n ⟩
    (suc n) * (suc n ∸ 1)
  ≡⟨⟩
   n -- Proof did not finish 
  Eq.≡-Reasoning.∎
-}
\end{code}


#### Exercise `foldl`

Define a function `foldl` which is analogous to `foldr`, but where
operations associate to the left rather than the right.  For example,

    foldr _⊗_ e [ x , y , z ]  =  x ⊗ (y ⊗ (z ⊗ e))
    foldl _⊗_ e [ x , y , z ]  =  ((e ⊗ x) ⊗ y) ⊗ z


#### Exercise `foldr-monoid-foldl`

Show that if `_⊕_` and `e` form a monoid, then `foldr _⊗_ e` and
`foldl _⊗_ e` always compute the same result.


#### Exercise `Any-++-⇔` (recommended)

Prove a result similar to `All-++-↔`, but with `Any` in place of `All`, and a suitable
replacement for `_×_`.  As a consequence, demonstrate an equivalence relating
`_∈_` and `_++_`.

\begin{code}

record _⇔_ (A B : Set) : Set where
  field
    to   : A → B
    from : B → A

Any-++-⇔ : ∀ {A : Set} {P : A → Set} (xs ys : List A) →
  Any P (xs ++ ys) ⇔ (Any P xs ⊎ Any P ys)
Any-++-⇔ xs ys =
  record
    { to   = to xs ys
    ; from = from xs ys
    }
  where 

  to : ∀ {A : Set} {P : A → Set} (xs ys : List A) →
    Any P (xs ++ ys) → (Any P xs ⊎ Any P ys)
  to [] ys Pys = (_⊎_.inj₂ Pys)
  to (x ∷ xs) ys (Any.here Px) = (_⊎_.inj₁ (Any.here Px))
  to (x ∷ xs) ys (Any.there APxsys) with to xs ys APxsys
  to (x ∷ xs) ys (there APxsys) | _⊎_.inj₁ x₁ = _⊎_.inj₁ (there x₁)
  to (x ∷ xs) ys (there APxsys) | _⊎_.inj₂ x₁ = _⊎_.inj₂ x₁ 
  
  from : ∀ {A : Set} {P : A → Set} (xs ys : List A) →
    (Any P xs ⊎ Any P ys) → Any P (xs ++ ys)
  from [] ys (_⊎_.inj₂ AnyPys) = AnyPys
  from [] ys (_⊎_.inj₁ ())
  from (x ∷ xs) ys (_⊎_.inj₁ (here Px)) = here Px
  from (x ∷ xs) ys (_⊎_.inj₁ (there Pxs)) with from xs ys (_⊎_.inj₁ Pxs)
  ... | leftXs = there leftXs
  from (x ∷ xs) ys (_⊎_.inj₂ Pys) with from xs ys (_⊎_.inj₂ Pys)
  ... | rightYs = there rightYs

∈++-++-⇔ : ∀ (A : Set) (n : A) (xs ys : List A) →
  (n ∈ (xs ++ ys)) ⇔ ((n ∈ xs) ⊎ (n ∈ ys))
∈++-++-⇔ A n xs ys = Any-++-⇔ {A} {n ≡_} xs ys  

\end{code}


#### Exercise `All-++-≃` (stetch)

Show that the equivalence `All-++-⇔` can be extended to an isomorphism.


#### Exercise `¬Any≃All¬` (stretch)

First generalise composition to arbitrary levels, using
[universe polymorphism][plfa.Equality#unipoly].
\begin{code}
_∘′_ : ∀ {ℓ₁ ℓ₂ ℓ₃ : Level} {A : Set ℓ₁} {B : Set ℓ₂} {C : Set ℓ₃}
  → (B → C) → (A → B) → A → C
(g ∘′ f) x  =  g (f x)
\end{code}

Show that `Any` and `All` satisfy a version of De Morgan's Law.
\begin{code}
postulate
  ¬Any≃All¬ : ∀ {A : Set} (P : A → Set) (xs : List A)
    → (¬_ ∘′ Any P) xs ≃ All (¬_ ∘′ P) xs
\end{code}

Do we also have the following?
\begin{code}
postulate
  ¬All≃Any¬ : ∀ {A : Set} (P : A → Set) (xs : List A)
    → (¬_ ∘′ All P) xs ≃ Any (¬_ ∘′ P) xs
\end{code}
If so, prove; if not, explain why.


#### Exercise `any?`

Just as `All` has analogues `all` and `all?` which determine whether a
predicate holds for every element of a list, so does `Any` have
analogues `any` and `any?` which determine whether a predicates holds
for some element of a list.  Give their definitions.

## Lambda

#### Exercise `mul` (recommended)

Write out the definition of a lambda term that multiplies
two natural numbers.

\begin{code}
mul : Term
mul = μ "*" ⇒ ƛ "m" ⇒ ƛ "n" ⇒
        case ` "m"
          [zero⇒ `zero
          |suc "m" ⇒ (plus · ` "n" · (` "*" · ` "m" · ` "n")) ]

2*2 : Term
2*2 = mul · two · two


\end{code}


#### Exercise `primed` (stretch)

We can make examples with lambda terms slighly easier to write
by adding the following definitions.
\begin{code}
ƛ′_⇒_ : Term → Term → Term
ƛ′ (` x) ⇒ N  =  ƛ x ⇒ N
ƛ′ _ ⇒ _      =  ⊥-elim impossible
  where postulate impossible : ⊥

case′_[zero⇒_|suc_⇒_] : Term → Term → Term → Term → Term
case′ L [zero⇒ M |suc (` x) ⇒ N ]  =  case L [zero⇒ M |suc x ⇒ N ]
case′ _ [zero⇒ _ |suc _ ⇒ _ ]      =  ⊥-elim impossible
  where postulate impossible : ⊥

μ′_⇒_ : Term → Term → Term
μ′ (` x) ⇒ N  =  μ x ⇒ N
μ′ _ ⇒ _      =  ⊥-elim impossible
  where postulate impossible : ⊥
\end{code}
The definition of `plus` can now be written as follows.
\begin{code}
plus′ : Term
plus′ = μ′ + ⇒ ƛ′ m ⇒ ƛ′ n ⇒
          case′ m
            [zero⇒ n
            |suc m ⇒ `suc (+ · m · n) ]
  where
  +  =  ` "+"
  m  =  ` "m"
  n  =  ` "n"
\end{code}
Write out the definition of multiplication in the same style.

#### Exercise `_[_:=_]′` (stretch)

The definition of substitution above has three clauses (`ƛ`, `case`,
and `μ`) that invoke a with clause to deal with bound variables.
Rewrite the definition to factor the common part of these three
clauses into a single function, defined by mutual recursion with
substitution.


#### Exercise `—↠≃—↠′`

Show that the two notions of reflexive and transitive closure
above are isomorphic.


#### Exercise `plus-example`

Write out the reduction sequence demonstrating that one plus one is two.


#### Exercise `mul-type`

Using the term `mul` you defined earlier, write out the derivation
showing that it is well-typed.

\begin{code}

⊢mul : ∅ ⊢ mul ⦂ `ℕ ⇒ `ℕ ⇒ `ℕ
⊢mul = ⊢μ (⊢ƛ (⊢ƛ (⊢case ((⊢` ∋m)) (⊢zero) ((⊢plus · ⊢` ∋n' · (⊢` ∋* · ⊢` ∋m' · ⊢` ∋n'))))))
  where
  ∋*  = (S ("*" ≠ "m") (S ("*" ≠ "n") (S ("*" ≠ "m") Z)))
  ∋m  = (S ("m" ≠ "n") Z)
  ∋m' = Z
  ∋n' = (S ("n" ≠ "m") Z)

\end{code}

## Properties


#### Exercise `Progress-iso`

Show that `Progress M` is isomorphic to `Value M ⊎ ∃[ N ](M —→ N)`.


#### Exercise `progress′`

Write out the proof of `progress′` in full, and compare it to the
proof of `progress` above.


#### Exercise `value?`

Combine `progress` and `—→¬V` to write a program that decides
whether a well-typed term is a value.
\begin{code}
postulate
  value? : ∀ {A M} → ∅ ⊢ M ⦂ A → Dec (Value M)
\end{code}


#### Exercise `subst′` (stretch)

Rewrite `subst` to work with the modified definition `_[_:=_]′`
from the exercise in the previous chapter.  As before, this
should factor dealing with bound variables into a single function,
defined by mutual recursion with the proof that substitution
preserves types.


#### Exercise `mul-example` (recommended)

Using the evaluator, confirm that two times two is four.

\begin{code}
{-
⊢2*2 : ∅ ⊢ mul · two · two ⦂ `ℕ
⊢2*2 = ⊢mul · ⊢two · ⊢two

_ : eval (gas 100) ⊢2*2 ≡
  steps
    ((μ "*" ⇒
    (ƛ "m" ⇒
     (ƛ "n" ⇒
      case ` "m" [zero⇒ `zero |suc "m" ⇒
      (μ "+" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
         ])))
      · ` "n"
      · (` "*" · ` "m" · ` "n")
      ])))
   · `suc (`suc `zero)
   · `suc (`suc `zero)
   —→⟨ ξ-·₁ (ξ-·₁ β-μ) ⟩
   (ƛ "m" ⇒
    (ƛ "n" ⇒
     case ` "m" [zero⇒ `zero |suc "m" ⇒
     (μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "n"
     ·
     ((μ "*" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ `zero |suc "m" ⇒
         (μ "+" ⇒
          (ƛ "m" ⇒
           (ƛ "n" ⇒
            case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
            ])))
         · ` "n"
         · (` "*" · ` "m" · ` "n")
         ])))
      · ` "m"
      · ` "n")
     ]))
   · `suc (`suc `zero)
   · `suc (`suc `zero)
   —→⟨ ξ-·₁ (β-ƛ (V-suc (V-suc V-zero))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ `zero |suc "m" ⇒
    (μ "+" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
       ])))
    · ` "n"
    ·
    ((μ "*" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ `zero |suc "m" ⇒
        (μ "+" ⇒
         (ƛ "m" ⇒
          (ƛ "n" ⇒
           case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
           ])))
        · ` "n"
        · (` "*" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   · `suc (`suc `zero)
   —→⟨ β-ƛ (V-suc (V-suc V-zero)) ⟩
   case `suc (`suc `zero) [zero⇒ `zero |suc "m" ⇒
   (μ "+" ⇒
    (ƛ "m" ⇒
     (ƛ "n" ⇒
      case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
      ])))
   · `suc (`suc `zero)
   ·
   ((μ "*" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ `zero |suc "m" ⇒
       (μ "+" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
          ])))
       · ` "n"
       · (` "*" · ` "m" · ` "n")
       ])))
    · ` "m"
    · `suc (`suc `zero))
   ]
   —→⟨ β-suc (V-suc V-zero) ⟩
   (μ "+" ⇒
    (ƛ "m" ⇒
     (ƛ "n" ⇒
      case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
      ])))
   · `suc (`suc `zero)
   ·
   ((μ "*" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ `zero |suc "m" ⇒
       (μ "+" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
          ])))
       · ` "n"
       · (` "*" · ` "m" · ` "n")
       ])))
    · `suc `zero
    · `suc (`suc `zero))
   —→⟨ ξ-·₁ (ξ-·₁ β-μ) ⟩
   (ƛ "m" ⇒
    (ƛ "n" ⇒
     case ` "m" [zero⇒ ` "n" |suc "m" ⇒
     `suc
     ((μ "+" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
         ])))
      · ` "m"
      · ` "n")
     ]))
   · `suc (`suc `zero)
   ·
   ((μ "*" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ `zero |suc "m" ⇒
       (μ "+" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
          ])))
       · ` "n"
       · (` "*" · ` "m" · ` "n")
       ])))
    · `suc `zero
    · `suc (`suc `zero))
   —→⟨ ξ-·₁ (β-ƛ (V-suc (V-suc V-zero))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   ((μ "*" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ `zero |suc "m" ⇒
       (μ "+" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
          ])))
       · ` "n"
       · (` "*" · ` "m" · ` "n")
       ])))
    · `suc `zero
    · `suc (`suc `zero))
   —→⟨ ξ-·₂ V-ƛ (ξ-·₁ (ξ-·₁ β-μ)) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   ((ƛ "m" ⇒
     (ƛ "n" ⇒
      case ` "m" [zero⇒ `zero |suc "m" ⇒
      (μ "+" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
         ])))
      · ` "n"
      ·
      ((μ "*" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ `zero |suc "m" ⇒
          (μ "+" ⇒
           (ƛ "m" ⇒
            (ƛ "n" ⇒
             case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
             ])))
          · ` "n"
          · (` "*" · ` "m" · ` "n")
          ])))
       · ` "m"
       · ` "n")
      ]))
    · `suc `zero
    · `suc (`suc `zero))
   —→⟨ ξ-·₂ V-ƛ (ξ-·₁ (β-ƛ (V-suc V-zero))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   ((ƛ "n" ⇒
     case `suc `zero [zero⇒ `zero |suc "m" ⇒
     (μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "n"
     ·
     ((μ "*" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ `zero |suc "m" ⇒
         (μ "+" ⇒
          (ƛ "m" ⇒
           (ƛ "n" ⇒
            case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
            ])))
         · ` "n"
         · (` "*" · ` "m" · ` "n")
         ])))
      · ` "m"
      · ` "n")
     ])
    · `suc (`suc `zero))
   —→⟨ ξ-·₂ V-ƛ (β-ƛ (V-suc (V-suc V-zero))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   case `suc `zero [zero⇒ `zero |suc "m" ⇒
   (μ "+" ⇒
    (ƛ "m" ⇒
     (ƛ "n" ⇒
      case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
      ])))
   · `suc (`suc `zero)
   ·
   ((μ "*" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ `zero |suc "m" ⇒
       (μ "+" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
          ])))
       · ` "n"
       · (` "*" · ` "m" · ` "n")
       ])))
    · ` "m"
    · `suc (`suc `zero))
   ]
   —→⟨ ξ-·₂ V-ƛ (β-suc V-zero) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   ((μ "+" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
       ])))
    · `suc (`suc `zero)
    ·
    ((μ "*" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ `zero |suc "m" ⇒
        (μ "+" ⇒
         (ƛ "m" ⇒
          (ƛ "n" ⇒
           case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
           ])))
        · ` "n"
        · (` "*" · ` "m" · ` "n")
        ])))
     · `zero
     · `suc (`suc `zero)))
   —→⟨ ξ-·₂ V-ƛ (ξ-·₁ (ξ-·₁ β-μ)) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   ((ƛ "m" ⇒
     (ƛ "n" ⇒
      case ` "m" [zero⇒ ` "n" |suc "m" ⇒
      `suc
      ((μ "+" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
          ])))
       · ` "m"
       · ` "n")
      ]))
    · `suc (`suc `zero)
    ·
    ((μ "*" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ `zero |suc "m" ⇒
        (μ "+" ⇒
         (ƛ "m" ⇒
          (ƛ "n" ⇒
           case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
           ])))
        · ` "n"
        · (` "*" · ` "m" · ` "n")
        ])))
     · `zero
     · `suc (`suc `zero)))
   —→⟨ ξ-·₂ V-ƛ (ξ-·₁ (β-ƛ (V-suc (V-suc V-zero)))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   ((ƛ "n" ⇒
     case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
     `suc
     ((μ "+" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
         ])))
      · ` "m"
      · ` "n")
     ])
    ·
    ((μ "*" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ `zero |suc "m" ⇒
        (μ "+" ⇒
         (ƛ "m" ⇒
          (ƛ "n" ⇒
           case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
           ])))
        · ` "n"
        · (` "*" · ` "m" · ` "n")
        ])))
     · `zero
     · `suc (`suc `zero)))
   —→⟨ ξ-·₂ V-ƛ (ξ-·₂ V-ƛ (ξ-·₁ (ξ-·₁ β-μ))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   ((ƛ "n" ⇒
     case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
     `suc
     ((μ "+" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
         ])))
      · ` "m"
      · ` "n")
     ])
    ·
    ((ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ `zero |suc "m" ⇒
       (μ "+" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
          ])))
       · ` "n"
       ·
       ((μ "*" ⇒
         (ƛ "m" ⇒
          (ƛ "n" ⇒
           case ` "m" [zero⇒ `zero |suc "m" ⇒
           (μ "+" ⇒
            (ƛ "m" ⇒
             (ƛ "n" ⇒
              case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
              ])))
           · ` "n"
           · (` "*" · ` "m" · ` "n")
           ])))
        · ` "m"
        · ` "n")
       ]))
     · `zero
     · `suc (`suc `zero)))
   —→⟨ ξ-·₂ V-ƛ (ξ-·₂ V-ƛ (ξ-·₁ (β-ƛ V-zero))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   ((ƛ "n" ⇒
     case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
     `suc
     ((μ "+" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
         ])))
      · ` "m"
      · ` "n")
     ])
    ·
    ((ƛ "n" ⇒
      case `zero [zero⇒ `zero |suc "m" ⇒
      (μ "+" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
         ])))
      · ` "n"
      ·
      ((μ "*" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ `zero |suc "m" ⇒
          (μ "+" ⇒
           (ƛ "m" ⇒
            (ƛ "n" ⇒
             case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
             ])))
          · ` "n"
          · (` "*" · ` "m" · ` "n")
          ])))
       · ` "m"
       · ` "n")
      ])
     · `suc (`suc `zero)))
   —→⟨ ξ-·₂ V-ƛ (ξ-·₂ V-ƛ (β-ƛ (V-suc (V-suc V-zero)))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   ((ƛ "n" ⇒
     case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
     `suc
     ((μ "+" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
         ])))
      · ` "m"
      · ` "n")
     ])
    ·
    case `zero [zero⇒ `zero |suc "m" ⇒
    (μ "+" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
       ])))
    · `suc (`suc `zero)
    ·
    ((μ "*" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ `zero |suc "m" ⇒
        (μ "+" ⇒
         (ƛ "m" ⇒
          (ƛ "n" ⇒
           case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
           ])))
        · ` "n"
        · (` "*" · ` "m" · ` "n")
        ])))
     · ` "m"
     · `suc (`suc `zero))
    ])
   —→⟨ ξ-·₂ V-ƛ (ξ-·₂ V-ƛ β-zero) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   ((ƛ "n" ⇒
     case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
     `suc
     ((μ "+" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
         ])))
      · ` "m"
      · ` "n")
     ])
    · `zero)
   —→⟨ ξ-·₂ V-ƛ (β-ƛ V-zero) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   case `suc (`suc `zero) [zero⇒ `zero |suc "m" ⇒
   `suc
   ((μ "+" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
       ])))
    · ` "m"
    · `zero)
   ]
   —→⟨ ξ-·₂ V-ƛ (β-suc (V-suc V-zero)) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   `suc
   ((μ "+" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
       ])))
    · `suc `zero
    · `zero)
   —→⟨ ξ-·₂ V-ƛ (ξ-suc (ξ-·₁ (ξ-·₁ β-μ))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   `suc
   ((ƛ "m" ⇒
     (ƛ "n" ⇒
      case ` "m" [zero⇒ ` "n" |suc "m" ⇒
      `suc
      ((μ "+" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
          ])))
       · ` "m"
       · ` "n")
      ]))
    · `suc `zero
    · `zero)
   —→⟨ ξ-·₂ V-ƛ (ξ-suc (ξ-·₁ (β-ƛ (V-suc V-zero)))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   `suc
   ((ƛ "n" ⇒
     case `suc `zero [zero⇒ ` "n" |suc "m" ⇒
     `suc
     ((μ "+" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
         ])))
      · ` "m"
      · ` "n")
     ])
    · `zero)
   —→⟨ ξ-·₂ V-ƛ (ξ-suc (β-ƛ V-zero)) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   `suc
   case `suc `zero [zero⇒ `zero |suc "m" ⇒
   `suc
   ((μ "+" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
       ])))
    · ` "m"
    · `zero)
   ]
   —→⟨ ξ-·₂ V-ƛ (ξ-suc (β-suc V-zero)) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   `suc
   (`suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · `zero
     · `zero))
   —→⟨ ξ-·₂ V-ƛ (ξ-suc (ξ-suc (ξ-·₁ (ξ-·₁ β-μ)))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   `suc
   (`suc
    ((ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ ` "n" |suc "m" ⇒
       `suc
       ((μ "+" ⇒
         (ƛ "m" ⇒
          (ƛ "n" ⇒
           case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
           ])))
        · ` "m"
        · ` "n")
       ]))
     · `zero
     · `zero))
   —→⟨ ξ-·₂ V-ƛ (ξ-suc (ξ-suc (ξ-·₁ (β-ƛ V-zero)))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   `suc
   (`suc
    ((ƛ "n" ⇒
      case `zero [zero⇒ ` "n" |suc "m" ⇒
      `suc
      ((μ "+" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
          ])))
       · ` "m"
       · ` "n")
      ])
     · `zero))
   —→⟨ ξ-·₂ V-ƛ (ξ-suc (ξ-suc (β-ƛ V-zero))) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   ·
   `suc
   (`suc
    case `zero [zero⇒ `zero |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · `zero)
    ])
   —→⟨ ξ-·₂ V-ƛ (ξ-suc (ξ-suc β-zero)) ⟩
   (ƛ "n" ⇒
    case `suc (`suc `zero) [zero⇒ ` "n" |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · ` "n")
    ])
   · `suc (`suc `zero)
   —→⟨ β-ƛ (V-suc (V-suc V-zero)) ⟩
   case `suc (`suc `zero) [zero⇒ `suc (`suc `zero) |suc "m" ⇒
   `suc
   ((μ "+" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
       ])))
    · ` "m"
    · `suc (`suc `zero))
   ]
   —→⟨ β-suc (V-suc V-zero) ⟩
   `suc
   ((μ "+" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
       ])))
    · `suc `zero
    · `suc (`suc `zero))
   —→⟨ ξ-suc (ξ-·₁ (ξ-·₁ β-μ)) ⟩
   `suc
   ((ƛ "m" ⇒
     (ƛ "n" ⇒
      case ` "m" [zero⇒ ` "n" |suc "m" ⇒
      `suc
      ((μ "+" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
          ])))
       · ` "m"
       · ` "n")
      ]))
    · `suc `zero
    · `suc (`suc `zero))
   —→⟨ ξ-suc (ξ-·₁ (β-ƛ (V-suc V-zero))) ⟩
   `suc
   ((ƛ "n" ⇒
     case `suc `zero [zero⇒ ` "n" |suc "m" ⇒
     `suc
     ((μ "+" ⇒
       (ƛ "m" ⇒
        (ƛ "n" ⇒
         case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
         ])))
      · ` "m"
      · ` "n")
     ])
    · `suc (`suc `zero))
   —→⟨ ξ-suc (β-ƛ (V-suc (V-suc V-zero))) ⟩
   `suc
   case `suc `zero [zero⇒ `suc (`suc `zero) |suc "m" ⇒
   `suc
   ((μ "+" ⇒
     (ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
       ])))
    · ` "m"
    · `suc (`suc `zero))
   ]
   —→⟨ ξ-suc (β-suc V-zero) ⟩
   `suc
   (`suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · `zero
     · `suc (`suc `zero)))
   —→⟨ ξ-suc (ξ-suc (ξ-·₁ (ξ-·₁ β-μ))) ⟩
   `suc
   (`suc
    ((ƛ "m" ⇒
      (ƛ "n" ⇒
       case ` "m" [zero⇒ ` "n" |suc "m" ⇒
       `suc
       ((μ "+" ⇒
         (ƛ "m" ⇒
          (ƛ "n" ⇒
           case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
           ])))
        · ` "m"
        · ` "n")
       ]))
     · `zero
     · `suc (`suc `zero)))
   —→⟨ ξ-suc (ξ-suc (ξ-·₁ (β-ƛ V-zero))) ⟩
   `suc
   (`suc
    ((ƛ "n" ⇒
      case `zero [zero⇒ ` "n" |suc "m" ⇒
      `suc
      ((μ "+" ⇒
        (ƛ "m" ⇒
         (ƛ "n" ⇒
          case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
          ])))
       · ` "m"
       · ` "n")
      ])
     · `suc (`suc `zero)))
   —→⟨ ξ-suc (ξ-suc (β-ƛ (V-suc (V-suc V-zero)))) ⟩
   `suc
   (`suc
    case `zero [zero⇒ `suc (`suc `zero) |suc "m" ⇒
    `suc
    ((μ "+" ⇒
      (ƛ "m" ⇒
       (ƛ "n" ⇒
        case ` "m" [zero⇒ ` "n" |suc "m" ⇒ `suc (` "+" · ` "m" · ` "n")
        ])))
     · ` "m"
     · `suc (`suc `zero))
    ])
   —→⟨ ξ-suc (ξ-suc β-zero) ⟩ `suc (`suc (`suc (`suc `zero))) _—↠_.∎)
  (done (V-suc (V-suc (V-suc (V-suc V-zero)))))


_ = refl
-}
\end{code}

#### Exercise: `progress-preservation` (recommended)

Without peeking at their statements above, write down the progress
and preservation theorems for the simply typed lambda-calculus.

Preservation states
∅ ⊢ N ⦂ A
→ N —→ M
-----
∅ ⊢ M ⦂ A

Progress says
either a term reduces further or it is a value (done).
More formally, if ∅ ⊢ M ⦂ A then either M is a value or there exists N such that M —→ N.

#### Exercise `subject_expansion`

We say that `M` _reduces_ to `N` if `M —→ N`,
and conversely that `M` _expands_ to `N` if `N —→ M`.
The preservation property is sometimes called _subject reduction_.
Its opposite is _subject expansion_, which holds if
`M —→ N` and `∅ ⊢ N ⦂ A` imply `∅ ⊢ M ⦂ A`.
Find two counter-examples to subject expansion, one
with case expressions and one not involving case expressions.


#### Exercise `stuck` (recommended)

Give an example of an ill-typed term that does get stuck.



#### Exercise `unstuck` (recommended)

Provide proofs of the three postulates, `unstuck`, `preserves`, and `wttdgs` above.

\begin{code}

unstucks : ∀ {M A}
  → ∅ ⊢ M ⦂ A
  ------------
  → ¬ (Stuck M)
unstucks proofWTM ⟨ NM , ¬VM ⟩ with progress proofWTM
... | step x = NM x
... | done x = ¬VM x

preservess : ∀ {M N A}
  → ∅ ⊢ M ⦂ A
  → M —↠ N
  ---------
  → ∅ ⊢ N ⦂ A
preservess proofWTM (M _—↠_.∎) = proofWTM
preservess proofWTM (L —→⟨ x ⟩ MstepN) = preservess (preserve proofWTM x) MstepN

wttdgss : ∀ {M N A}
  → ∅ ⊢ M ⦂ A
  → M —↠ N
  --------
  → ¬ (Stuck N)
wttdgss proofWTM M—↠N = unstucks (preservess (proofWTM) M—↠N)  

\end{code}

-- REVISION

\begin{code}

_++2_ : ∀ {A : Set} → List A → List A → List A
[] ++2 ys = ys
(x ∷ xs) ++2 ys = x ∷ (xs ++2 ys)

++-assoc2 : ∀ {A : Set} (xs ys zs : List A)
  → (xs ++ ys) ++ zs ≡ xs ++ (ys ++ zs)
++-assoc2 [] ys zs = 
  Eq.≡-Reasoning.begin
    ([] ++ ys) ++ zs
  ≡⟨⟩
    ys ++ zs
  ≡⟨⟩
    [] ++ (ys ++ zs) 
  Eq.≡-Reasoning.∎
++-assoc2 (x ∷ xs) ys zs =
  Eq.≡-Reasoning.begin
    ((x ∷ xs) ++ ys) ++ zs
  ≡⟨⟩
    (x ∷ (xs ++ ys)) ++ zs
  ≡⟨⟩
    x ∷ ((xs ++ ys) ++ zs)
  ≡⟨  cong (x ∷_) (++-assoc2 xs ys zs) ⟩
    x ∷ (xs ++ (ys ++ zs))
  ≡⟨ refl ⟩
    x ∷ xs ++ (ys ++ zs)
  Eq.≡-Reasoning.∎

++-identityʳ2 : ∀ {A : Set} (xs : List A) → xs ++ [] ≡ xs
++-identityʳ2 [] =
  Eq.≡-Reasoning.begin
    [] ++ []
  ≡⟨⟩
    []
  Eq.≡-Reasoning.∎
++-identityʳ2 (x ∷ xs) =
  Eq.≡-Reasoning.begin
    (x ∷ xs) ++ []
  ≡⟨⟩
    x ∷ (xs ++ [])
  ≡⟨ cong (x ∷_) (++-identityʳ2 xs) ⟩
    x ∷ (xs)
  Eq.≡-Reasoning.∎

++-identityˡ : ∀ {A : Set} (xs : List A) → [] ++ xs ≡ xs
++-identityˡ xs =
  Eq.≡-Reasoning.begin
    [] ++ xs
  ≡⟨⟩
    xs
  Eq.≡-Reasoning.∎

length : ∀ {A : Set} → List A → ℕ
length []        =  zero
length (x ∷ xs)  =  suc (length xs)

_ : length [ 0 , 1 ] ≡ 2
_ =
  Eq.≡-Reasoning.begin
    suc ( length [ 1 ] )
  ≡⟨⟩
    suc ( suc ( zero ) )
  ≡⟨⟩
    2
  Eq.≡-Reasoning.∎

length-++ : ∀ {A : Set} (xs ys : List A)
  → length (xs ++ ys) ≡ length xs + length ys
length-++ [] ys = refl
length-++ (x ∷ xs) ys =
  Eq.≡-Reasoning.begin
    length (x ∷ xs ++ ys)
  ≡⟨⟩
    length (x ∷ (xs ++ ys))
  ≡⟨⟩
    suc (length (xs ++ ys))
  ≡⟨ cong suc (length-++ xs ys) ⟩
    suc (length xs + length ys)
  ≡⟨⟩
    suc (length xs) + length ys
  ≡⟨⟩
    length (x ∷ xs) + length ys
  Eq.≡-Reasoning.∎

shunt : ∀ {A : Set} → List A → List A → List A
shunt [] ys = ys
shunt (x ∷ xs) ys =  shunt xs (x ∷ ys)

shunt-reverse : ∀ {A : Set} (xs ys : List A)
  → shunt xs ys ≡ reverse xs ++ ys
shunt-reverse [] ys = refl
shunt-reverse (x ∷ xs) ys =
  Eq.≡-Reasoning.begin
    shunt (x ∷ xs) ys
  ≡⟨⟩
    shunt xs (x ∷ ys)
  ≡⟨ shunt-reverse xs (x ∷ ys) ⟩
    reverse xs ++ (x ∷ ys)
  ≡⟨⟩
    reverse xs ++ ([ x ] ++ ys)
  ≡⟨ sym (++-assoc (reverse xs) [ x ] ys)  ⟩
    (reverse xs ++ [ x ]) ++ ys
  ≡⟨⟩
    (reverse (x ∷ xs) ++ ys)
  Eq.≡-Reasoning.∎

reverse-++-commute2 : ∀ {A : Set} (xs ys : List A)
    → reverse (xs ++ ys) ≡ reverse ys ++ reverse xs
reverse-++-commute2 [] ys =
  Eq.≡-Reasoning.begin
    reverse ([] ++ ys)
  ≡⟨⟩
    reverse ys
  ≡⟨ sym (++-identityʳ (reverse ys)) ⟩
    reverse ys ++ []
  Eq.≡-Reasoning.∎
reverse-++-commute2 (x ∷ xs) ys =
  Eq.≡-Reasoning.begin
    reverse ((x ∷ xs) ++ ys)
  ≡⟨⟩
    reverse (x ∷ (xs ++ ys))
  ≡⟨⟩
    reverse (xs ++ ys) ++ [ x ]
  ≡⟨ cong (_++ [ x ]) ((reverse-++-commute2 xs ys)) ⟩
    (reverse ys ++ reverse xs) ++ [ x ]
  ≡⟨ ++-assoc (reverse ys) (reverse xs) ([ x ]) ⟩
    reverse ys ++ reverse xs ++ [ x ]
  Eq.≡-Reasoning.∎

map2 : ∀ {A B : Set} → (A → B) → List A → List B
map2 f [] = []
map2 f (x ∷ xs) = f x ∷ map f xs

_ : map suc [ 0 , 1 , 2 ] ≡ [ 1 , 2 , 3 ]
_ =
  Eq.≡-Reasoning.begin
    suc 0 ∷ map suc (1 ∷ 2 ∷ [])
  ≡⟨ refl ⟩
    suc 0 ∷ suc 1 ∷ map suc ( 2 ∷ [] )
  Eq.≡-Reasoning.∎

foldr-++2 : ∀ {A B : Set} (_⊗_ : A → B → B) (e : B) (xs ys : List A) →
    foldr _⊗_ e (xs ++ ys) ≡ foldr _⊗_ (foldr _⊗_ e ys) xs
foldr-++2 ⊗ e [] ys = refl
foldr-++2 ⊗ e (x ∷ xs) ys =
  Eq.≡-Reasoning.begin
    foldr ⊗ e (x ∷ xs ++ ys)
  ≡⟨⟩
    ⊗ x (foldr ⊗ e (xs ++ ys))
  ≡⟨ cong (⊗ x) (foldr-++2 ⊗ e xs ys) ⟩
    ⊗ x (foldr ⊗ (foldr ⊗ e ys) xs)
  Eq.≡-Reasoning.∎

data Any2 {A : Set} (P : A → Set) : List A → Set where
  here : ∀ {x : A} {xs : List A} → P x → Any2 P (x ∷ xs)
  there : ∀ {x : A} {xs : List A} → Any2 P xs → Any2 P (x ∷ xs)

_∈2_ : ∀ {A : Set} (x : A) (xs : List A) → Set
x ∈2 xs = Any (x ≡_) xs

_∉2_ : ∀ {A : Set} (x : A) (xs : List A) → Set
x ∉2 xs = ¬ (x ∈ xs)

_ : 0 ∈ [ 0 , 1 ]
_ = here refl

_ : 0 ∈ [ 0 , 0 ]
_ = there (here refl)

All-++-⇔ : ∀ {A : Set} {P : A → Set} (xs ys : List A) →
  All P (xs ++ ys) ⇔ (All P xs × All P ys)
All-++-⇔ xs ys =
  record
    { to = to xs ys
    ; from = from xs ys
    }
  where
  
  to : ∀ {A : Set} {P : A → Set} (xs ys : List A) → All P (xs ++ ys) → (All P xs × All P ys)
  to [] ys Pys = ⟨ [] , Pys ⟩
  to (x ∷ xs) ys (Px ∷ Pxs++ys) with to xs ys Pxs++ys
  ... | ⟨ Pxs , Pys ⟩ = ⟨ Px ∷ Pxs , Pys ⟩

  from : ∀ {A : Set} {P : A → Set} (xs ys : List A) → (All P xs × All P ys) → All P (xs ++ ys)
  from [] ys ⟨ [] , Pys ⟩ = Pys
  from (x ∷ xs) ys ⟨ Px ∷ Pxs , Pys ⟩ = Px ∷ from xs ys ⟨ Pxs , Pys ⟩

_ : All (_≤ 2) [ 0 , 1 , 2 ]
_ = z≤n ∷ s≤s z≤n ∷ s≤s (s≤s z≤n) ∷ []

\end{code}

