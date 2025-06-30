---
title     : "Assignment1: TSPL Assignment 1"
layout    : page
permalink : /Assignment1/
---

\begin{code}
module plfa.Assignment1 where
\end{code}

## YOUR NAME AND EMAIL GOES HERE
Oliver Goldstein 
s1424164@ed.ac.uk

## Introduction

This assignment is due **4pm Thursday 4 October** (Week 3).

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
open import Data.Nat using (ℕ; zero; suc; _+_; _*_; _∸_; _≤_; z≤n; s≤s)
open import Data.Nat.Properties using (+-assoc; +-identityʳ; +-suc; +-comm;
  ≤-refl; ≤-trans; ≤-antisym; ≤-total; +-monoʳ-≤; +-monoˡ-≤; +-mono-≤)
\end{code}

## Naturals

#### Exercise `seven` {#seven}

Write out `7` in longhand.

\begin{code}

_ : ℕ
_ = suc (suc (suc (suc (suc (suc (suc zero))))))

\end{code}

#### Exercise `+-example` {#plus-example}

Compute `3 + 4`, writing out your reasoning as a chain of equations.

\begin{code}

_ : 3 + 4 ≡ 7
_ =
  begin
    3 + 4
   ≡⟨⟩ -- is shorthand for
     (suc (suc (suc zero))) + (suc (suc (suc (suc zero))))
   ≡⟨⟩ -- inductive case
     suc (suc (suc zero) + suc (suc (suc (suc zero))))
   ≡⟨⟩ -- inductive case
     suc (suc ((suc zero) + suc (suc (suc (suc zero)))))
   ≡⟨⟩ -- inductive case
     suc (suc (suc (zero + suc (suc (suc (suc zero))))))
   ≡⟨⟩ -- base case
     suc (suc (suc (suc (suc (suc (suc zero))))))
   ∎
   
\end{code}

#### Exercise `*-example` {#times-example}

Compute `3 * 4`, writing out your reasoning as a chain of equations.
\begin{code}

_ : 3 * 4 ≡ 12
_ =
  begin
    3 * 4
  ≡⟨⟩ -- is shorthand for
    (suc (suc (suc zero))) * (suc (suc (suc (suc zero))))
  ≡⟨⟩ -- is longhand for 
    (suc 2) * (suc 3)
  ≡⟨⟩ -- inductive case of multiplication
    (suc 3) + (2 * (suc 3))
  ≡⟨⟩ -- inductive case of multiplication
    (suc 3) + (suc 3) + (1 * (suc 3))
  ≡⟨⟩ -- inductive case of addition
    (suc 7) + (1 * (suc 3))
  ≡⟨⟩ -- inductive case of multiplication
    (suc 7) + (suc 3) + (zero * (suc 3))
  ≡⟨⟩ -- inductive case of addition
    (suc 11) + (zero * (suc 3))
  ≡⟨⟩ -- base case of multiplication
    (suc 11)
  ≡⟨⟩
    12
  ∎
  
\end{code}
#### Exercise `_^_` (recommended) {#power}

Define exponentiation, which is given by the following equations.

\begin{code}

_^_ : ℕ → ℕ → ℕ 
n ^ zero     =  1
n ^ (suc m)  =  n * (n ^ m)

k : 3 ^ 4 ≡ 81
k =
  begin
    3 ^ 4
  ≡⟨⟩
    81
  ∎
  
\end{code}

Check that `3 ^ 4` is `81`.


#### Exercise `∸-examples` (recommended) {#monus-examples}

Compute `5 ∸ 3` and `3 ∸ 5`, writing out your reasoning as a chain of equations.

First, definition of monus:

\begin{code}

_ : 5 ∸ 3 ≡ 2
_ = begin
    5 ∸ 3
  ≡⟨⟩ -- rewritten in longhand as
    (suc 4) ∸ (suc 2)
  ≡⟨⟩ -- inductive case of monus
    4 ∸ 2
  ≡⟨⟩ -- rewritten in longhand as
    (suc 3) ∸ (suc 1)
  ≡⟨⟩ -- inductive case of monus
    3 ∸ 1
  ≡⟨⟩ -- rewritten in longhand as
    (suc 2) ∸ (suc zero)
  ≡⟨⟩ -- inductive case of monus
    2 ∸ zero
  ≡⟨⟩ -- first case of monus
    2
  ∎

_ : 3 ∸ 5 ≡ zero
_ = begin
    3 ∸ 5
  ≡⟨⟩ -- rewritten in longhand as
    (suc 2) ∸ (suc 4)
  ≡⟨⟩ -- inductive case of monus
    2 ∸ 4
  ≡⟨⟩ -- rewritten in longhand as
    (suc 1) ∸ (suc 3)
  ≡⟨⟩ -- inductive case of monus
    1 ∸ 3
  ≡⟨⟩ -- rewritten in longhand as
    (suc zero) ∸ (suc 2)
  ≡⟨⟩ -- inductive case of monus
    zero ∸ 2
  ≡⟨⟩ -- second case of monus
    zero
  ∎
  
\end{code}

#### Exercise `Bin` (stretch) {#Bin}

A more efficient representation of natural numbers uses a binary
rather than a unary system.  We represent a number as a bitstring.

\begin{code}

data Bin : Set where
  nil : Bin
  x0_ : Bin → Bin
  x1_ : Bin → Bin
  
\end{code}

For instance, the bitstring

    1011

standing for the number eleven is encoded, right to left, as


x1 x1 x0 x1 nil

Representations are not unique due to leading zeros.
Hence, eleven is also represented by `001011`, encoded as

    x1 x1 x0 x1 x0 x0 nil

Define a function

    inc : Bin → Bin

that converts a bitstring to the bitstring for the next higher
number.  For example, since `1100` encodes twelve, we should have

    inc (x1 x1 x0 x1 nil) ≡ x0 x0 x1 x1 nil

Confirm that this gives the correct answer for the bitstrings
encoding zero through four.

Using the above, define a pair of functions to convert
between the two representations.

    to   : ℕ → Bin
    from : Bin → ℕ

For the former, choose the bitstring to have no leading zeros if it
represents a positive natural, and represent zero by `x0 nil`.
Confirm that these both give the correct answer for zero through four.

\begin{code}

-- For numbers mod 2^n
inc : Bin → Bin 
inc nil = nil
inc (x0 (x)) = x1 x
inc (x1 (x)) = x0 (inc  x)

\end{code}

## Induction

#### Exercise `operators` {#operators}

Give another example of a pair of operators that have an identity
and are associative, commutative, and distribute over one another.

Give an example of an operator that has an identity and is
associative but is not commutative.

A pair of operators that have the property are addition XOR and AND.

Matrix multiplication

#### Exercise `finite-+-assoc` (stretch) {#finite-plus-assoc}

Write out what is known about associativity of addition on each of the first four
days using a finite story of creation, as
[earlier][plfa.Naturals#finite-creation]

(0 + n) + p = 0 + (n + p)

if m + n + p = m + n + p
then suc m + n + p = suc m + n + p

Day 1:
0 : ℕ

Day 2:
0 : ℕ
1 : ℕ

(0 + 0) + 0 = 0 + (0 + 0)

Day 3:
0 : ℕ
1 : ℕ
2 : ℕ

(0 + 0) + 0 = 0 + (0 + 0)

(1 + 0) + 0 = 1 + (0 + 0)
(0 + 1) + 0 = 0 + (1 + 0)
(0 + 0) + 1 = 0 + (0 + 1)

Day 4:
0 : ℕ
1 : ℕ
2 : ℕ
3 : ℕ

(0 + 0) + 0 = 0 + (0 + 0)

(1 + 0) + 0 = 1 + (0 + 0)
(0 + 1) + 0 = 0 + (1 + 0)
(0 + 0) + 1 = 0 + (0 + 1)

(1 + 1) + 0 = 0 + (1 + 1)
(0 + 1) + 1 = 0 + (1 + 1)
(1 + 0) + 1 = 1 + (0 + 1)

(2 + 0) + 0 = 2 + (0 + 0)
(0 + 2) + 0 = 0 + (2 + 0)
(0 + 0) + 2 = 0 + (0 + 2)
 

#### Exercise `+-swap` (recommended) {#plus-swap} 

Show

    m + (n + p) ≡ n + (m + p)

for all naturals `m`, `n`, and `p`. No induction is needed,
just apply the previous results which show addition
is associative and commutative.  You may need to use
the following function from the standard library:

    sym : ∀ {m n : ℕ} → m ≡ n → n ≡ m


\begin{code}

+-swap : ∀ (m n p : ℕ) → m + (n + p) ≡ n + (m + p)
+-swap m n p =
  begin
    m + (n + p)
  ≡⟨ sym (+-assoc m n p) ⟩ 
    (m + n) + p
  ≡⟨ cong (_+ p) (+-comm m n) ⟩
   (n + m) + p
  ≡⟨ (+-assoc n m p) ⟩ 
    n + (m + p)
  ∎

\end{code}

#### Exercise `*-distrib-+` (recommended) {#times-distrib-plus}

Show multiplication distributes over addition, that is,

    (m + n) * p ≡ m * p + n * p

for all naturals `m`, `n`, and `p`.

\begin{code}

*-distrib-+ : ∀ (m n p : ℕ) → (m + n) * p ≡ m * p + n * p
*-distrib-+ zero n p = refl
*-distrib-+ (suc m) n p rewrite (*-distrib-+ m n p) | (sym (+-assoc p (m * p) (n * p))) = refl

\end{code}


#### Exercise `*-assoc` (recommended) {#times-assoc}

Show multiplication is associative, that is,

    (m * n) * p ≡ m * (n * p)

\begin{code}

*-assoc : ∀ (m n p : ℕ) → (m * n) * p ≡ m * (n * p)
*-assoc zero n p = refl
*-assoc (suc m) n p rewrite (*-distrib-+ n (m * n) p) | (*-assoc m n p) = refl

\end{code}

#### Exercise `*-comm` {#times-comm}

Show multiplication is commutative, that is,
     m * n ≡ n * m

for all naturals `m` and `n`.  As with commutativity of addition,
you will need to formulate and prove suitable lemmas.

\begin{code}

+nzero : ∀ (n : ℕ) → n * zero ≡ zero
+nzero zero = refl
+nzero (suc n) = +nzero n 


*-left-suc : ∀ (n m : ℕ) → n * (suc m) ≡ n + n * m
*-left-suc 0 _ = refl
*-left-suc (suc n) m = 
  begin
    (suc n) * (suc m)
  ≡⟨⟩ -- inductive case of multiplication
    (suc m) + n * (suc m)
  ≡⟨ cong ((suc m) +_) (*-left-suc n m) ⟩
    (suc m) + (n + (n * m))
  ≡⟨ sym (+-assoc (suc m) n (n * m)) ⟩
    (suc m + n) + (n * m)
  ≡⟨⟩
    (suc (m + n) + (n * m))
  ≡⟨ cong (_+ (n * m)) (cong (suc) (+-comm m n)) ⟩
    (suc (n + m) + (n * m))
  ≡⟨⟩
    (suc n + m) + (n * m)
  ≡⟨⟩
    ((suc n) + m) + (n * m)
  ≡⟨ +-assoc (suc n) m (n * m) ⟩
    (suc n) + (suc n * m)
  ≡⟨⟩
    (suc n) + ((suc n) * m)
  ∎

*-comm : ∀ (m n : ℕ) → m * n ≡ n * m
*-comm zero n =
  begin
    zero * n
  ≡⟨⟩
    zero
  ≡⟨ sym (+nzero n) ⟩
    n * zero
  ∎
*-comm (suc m) n = 
  begin
    (suc m) * n
  ≡⟨⟩
    n + m * n
  ≡⟨ cong (n +_) (*-comm m n) ⟩
    n + n * m
  ≡⟨ sym (*-left-suc n m) ⟩
    n * suc m
  ∎
  
\end{code}

#### Exercise `0∸n≡0` {#zero-monus}

Show

    zero ∸ n ≡ zero

for all naturals `n`. Did your proof require induction?

\begin{code}

0∸n≡0 : ∀ (m n : ℕ) → zero ∸ n ≡ zero
0∸n≡0 zero (suc n) = refl
0∸n≡0 m zero = refl
0∸n≡0 (suc m) (suc n) = refl

\end{code}

The proof did not require induction as the inductive step is hidden
in refls ability to use zero-monus on m and n.

#### Exercise `∸-+-assoc` {#monus-plus-assoc}

Show that monus associates with addition, that is,

    m ∸ n ∸ p ≡ m ∸ (n + p)

for all naturals `m`, `n`, and `p`.

\begin{code}
  

∸-+-assoc : ∀ (m n p : ℕ) → m ∸ n ∸ p ≡ m ∸ (n + p)
∸-+-assoc m zero p = refl
∸-+-assoc zero (suc n) p =
  begin
    zero ∸ (suc n) ∸ p
  ≡⟨⟩
    zero ∸ p
  ≡⟨ 0∸n≡0 zero p ⟩
    zero
  ≡⟨⟩
    zero ∸ (suc (n + p))
  ≡⟨⟩
    zero ∸ (suc n + p)
  ∎
∸-+-assoc (suc m) (suc n) p =
  begin
    (suc m) ∸ (suc n) ∸ p
  ≡⟨⟩
    m ∸ n ∸ p 
  ≡⟨ ∸-+-assoc m n p ⟩
    m ∸ (n + p)
  ∎
    

\end{code}

#### Exercise `Bin-laws` (stretch) {#Bin-laws}

Recall that 
Exercise [Bin][plfa.Naturals#Bin]
defines a datatype `Bin` of bitstrings representing natural numbers
and asks you to define functions

    inc   : Bin → Bin
    to    : ℕ → Bin
    from  : Bin → ℕ

Consider the following laws, where `n` ranges over naturals and `x`
over bitstrings.

    from (inc x) ≡ suc (from x)
    to (from n) ≡ n
    from (to x) ≡ x

For each law: if it holds, prove; if not, give a counterexample.


## Relations


#### Exercise `orderings` {#orderings}

Give an example of a preorder that is not a partial order.

Give an example of a partial order that is not a preorder.


#### Exercise `≤-antisym-cases` {#leq-antisym-cases} 

The above proof omits cases where one argument is `z≤n` and one
argument is `s≤s`.  Why is it ok to omit them?


#### Exercise `*-mono-≤` (stretch)

Show that multiplication is monotonic with regard to inequality.

\begin{code}

{-
*-monoR≤ : ∀ (m p q : ℕ)
  → p ≤ q
  -------
  → m * p ≤ m * q
*-monoR≤ zero p q p≤q = z≤n -- trivial case but not base case
*-monoR≤ m zero q _ rewrite Data.Nat.Properties.*-comm m zero = {!z≤n!}
*-monoR≤ (suc zero) p q (s≤s p≤q) = ?
-}

\end{code}

#### Exercise `<-trans` (recommended) {#less-trans}

Show that strict inequality is transitive.

\begin{code}

infix 4 _<_

data _<_ : ℕ → ℕ → Set where

  z<s : ∀ {n : ℕ}
      -----------
    → zero < suc n

  s<s : ∀ {m n : ℕ}
    → m < n
      -------------
    → suc m < suc n

<-trans : ∀ {m n p : ℕ}
  → m < n
  → n < p
    -----
  → m < p
<-trans {zero} {suc n} {suc p} (z<s) (s<s n<p)      = z<s {p}
<-trans {suc m} {suc n} {suc p} (s<s a) (s<s b) = s<s {m} {p} (<-trans a b)

\end{code}

#### Exercise `trichotomy` {#trichotomy} 

Show that strict inequality satisfies a weak version of trichotomy, in
the sense that for any `m` and `n` that one of the following holds:
  * `m < n`,
  * `m ≡ n`, or
  * `m > n`
Define `m > n` to be the same as `n < m`.
You will need a suitable data declaration,
similar to that used for totality.
(We will show that the three cases are exclusive after we introduce
[negation][plfa.Negation].)

\begin{code}

data tri (m n : ℕ) : Set where

  LT :
    m < n
    ------
    → tri m n

  GT :
    n < m
    -----
    → tri m n

  EQ :
    m ≡ n
    -----
    → tri m n

trichotomy : ∀ (m n : ℕ) → tri m n
trichotomy zero (suc n) = LT z<s
trichotomy (suc n) zero = GT z<s
trichotomy zero zero    = EQ refl
trichotomy (suc m) (suc n) with trichotomy m n
...                    | LT m<n = LT (s<s m<n)
...                    | GT n<m = GT (s<s n<m)
...                    | EQ m≡n = EQ (cong suc (m≡n))

\end{code}


#### Exercise `+-mono-<` {#plus-mono-less}

Show that addition is monotonic with respect to strict inequality.
As with inequality, some additional definitions may be required.

\begin{code} 

+-monoR-< : ∀ (m p q : ℕ)
  → p < q
  --------
  → m + p < m + q
+-monoR-< zero p q eq     = eq
+-monoR-< (suc m) p q eq = s<s (+-monoR-< m p q eq)

+-monoL-< : ∀ (m n p : ℕ)
  → m < n
  ---------
  → m + p < n + p
+-monoL-< m n p eq rewrite +-comm m p | +-comm n p = +-monoR-< p m n eq

+-mono-< : ∀ (m n p q : ℕ)
  → m < n
  → p < q
  ----------
  → m + p < n + q
+-mono-< m n p q eq1 eq2 = <-trans (+-monoL-< m n p eq1) (+-monoR-< n p q eq2) 

\end{code}

#### Exercise `≤-iff-<` (recommended) {#leq-iff-less}

Show that `suc m ≤ n` implies `m < n`, and conversely.

\begin{code}

f : ∀ {m n : ℕ}
  → suc m ≤ n
  -----
  → m < n
  
f {zero} {.(suc _)} (s≤s m≤n) = z<s
f {suc m} {.(suc _)} (s≤s m≤n) = s<s (f m≤n)

g : ∀ {m n : ℕ}
  → m < n
  -----
  → suc m ≤ n
g {zero} {n} (z<s) = s≤s z≤n
g {suc m} {n} (s<s m<n) = s≤s (g m<n)

\end{code}

#### Exercise `<-trans-revisited` {#less-trans-revisited}

Give an alternative proof that strict inequality is transitive,
using the relating between strict inequality and inequality and
the fact that inequality is transitive.

\begin{code}

<-trans-lemma₁ : ∀ {m n : ℕ} → m < n → m ≤ n
<-trans-lemma₁ z<s = z≤n
<-trans-lemma₁ (s<s x<y) = s≤s (<-trans-lemma₁ x<y)

<-trans-revisited : ∀ (x y z : ℕ)
  → x < y
  → y < z
  --------
  → x < z
<-trans-revisited x y z a b = (f (≤-trans (g a) (<-trans-lemma₁ b)))


\end{code}

#### Exercise `o+o≡e` (stretch) {#odd-plus-odd}

Show that the sum of two odd numbers is even.

\begin{code}

data even : ℕ → Set
data odd  : ℕ → Set

data even where

  zero :
      ---------
      even zero

  suc  : ∀ {n : ℕ}
    → odd n
      ------------
    → even (suc n)

data odd where

  suc   : ∀ {n : ℕ}
    → even n
      -----------
    → odd (suc n)

e+o≡o : ∀ {m n : ℕ} → even m → odd n → odd (m + n)
o+o≡e : ∀ {m n : ℕ} → odd m → odd n → even (m + n)

e+o≡o zero on = on
e+o≡o (suc om) on =  suc (o+o≡e om on)

o+o≡e (suc zero) (suc zero) = suc (suc (zero))
o+o≡e (suc en) on = suc (e+o≡o en on)

\end{code}

#### Exercise `Bin-predicates` (stretch) {#Bin-predicates}

Recall that 
Exercise [Bin][plfa.Naturals#Bin]
defines a datatype `Bin` of bitstrings representing natural numbers.
Representations are not unique due to leading zeros.
Hence, eleven may be represented by both of the following

    x1 x1 x0 x1 nil
    x1 x1 x0 x1 x0 x0 nil

Define a predicate

    Can : Bin → Set

over all bitstrings that holds if the bitstring is canonical, meaning
it has no leading zeros; the first representation of eleven above is
canonical, and the second is not.  To define it, you will need an
auxiliary predicate

    One : Bin → Set

that holds only if the bistring has a leading one.  A bitstring is
canonical if it has a leading one (representing a positive number) or
if it consists of a single zero (representing zero).

Show that increment preserves canonical bitstrings.

    Can x
    ------------
    Can (inc x)

Show that converting a natural to a bitstring always yields a
canonical bitstring.

    ----------
    Can (to n)

Show that converting a canonical bitstring to a natural
and back is the identity.

    Can x
    ---------------
    to (from x) ≡ x

\end{code}


-- REVISION
\begin{code}
+-monoR-≤ : ∀ (m p q : ℕ)
  → p ≤ q
    -------------
  → m + p ≤ m + q
+-monoR-≤ zero p q p≤q = p≤q
+-monoR-≤ (suc m) p q (p≤q) = s≤s (+-monoR-≤ m p q (p≤q))

+-monoL-≤ : ∀ (m n p : ℕ)
  → m ≤ n
    -------------
  → m + p ≤ n + p
+-monoL-≤ m n p m≤n rewrite +-comm m p | +-comm n p = +-monoR-≤ p m n m≤n

≤-trans2 : ∀ {m n p : ℕ}
  → m ≤ n
  → n ≤ p
  → m ≤ p
≤-trans2 z≤n _ = z≤n
≤-trans2 (s≤s m≤n) (s≤s n≤p) = s≤s (≤-trans2 m≤n n≤p) 

+-mono-≤2 : ∀ (m n p q : ℕ)
  → m ≤ n
  → p ≤ q
    -------------
  → m + p ≤ n + q
+-mono-≤2 m n p q (m≤n) (p≤q) = {!!}

≤-antisym2 : ∀ {m n : ℕ}
  → m ≤ n
  → n ≤ m
    -----
  → m ≡ n
≤-antisym2 z≤n z≤n = refl
≤-antisym2 (s≤s m≤n) (s≤s n≤m) = cong suc ((≤-antisym2) (m≤n) (n≤m))

_ : 1 + 1 ≡ 2
_ =
  begin
    1 + 1
  ≡⟨⟩
    (suc zero) + (suc zero)
  ≡⟨⟩
    suc (zero + suc zero)
  ≡⟨⟩
    suc (suc zero)
  ≡⟨⟩
    2
  ∎

+-comm2 : ∀ (m n : ℕ) → m + n ≡ n + m
+-comm2 m zero =
  begin
    m + zero
  ≡⟨ +-identityʳ m ⟩
    m
  ≡⟨⟩
    zero + m
  ∎

+-comm2 m (suc n) =
  begin
    m + (suc n)
  ≡⟨ +-suc m n ⟩
    suc (m + n)
  ≡⟨ cong suc (+-comm m n) ⟩
    suc (n + m)
  ≡⟨ refl ⟩
    (suc n) + m
  ∎

+-comm′ : ∀ (m n : ℕ) → m + n ≡ n + m
+-comm′ zero n rewrite (+-identityʳ n) = refl
+-comm′ (suc m) n rewrite +-suc n m | +-comm m n = refl

\end{code}



























(Hint: For each of these, you may first need to prove related
properties of `One`.)
