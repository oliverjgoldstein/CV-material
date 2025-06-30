---
title     : "Assignment2: TSPL Assignment 2"
layout    : page
permalink : /Assignment2/
---

\begin{code}
module plfa.Assignment2 where
\end{code}

## YOUR NAME AND EMAIL GOES HERE
Oliver Goldstein
s1424164@ed.ac.uk
## Introduction

This assignment is due **4pm Thursday 18 October** (Week 5).

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
open import plfa.Relations using (_<_; z<s; s<s)
open import Data.Product using (_×_; proj₁; proj₂) renaming (_,_ to ⟨_,_⟩)
open import Data.Unit using (⊤; tt)
open import Data.Sum using (_⊎_; inj₁; inj₂) renaming ([_,_] to case-⊎)
open import Data.Empty using (⊥; ⊥-elim)
open import Data.Bool.Base using (Bool; true; false; T; _∧_; _∨_; not)
open import Relation.Nullary using (¬_; Dec; yes; no)
open import Relation.Nullary.Decidable using (⌊_⌋; toWitness; fromWitness)
open import Relation.Nullary.Negation using (¬?)
open import Relation.Nullary.Product using (_×-dec_)
open import Relation.Nullary.Sum using (_⊎-dec_)
open import Relation.Nullary.Negation using (contraposition)
open import Data.Product using (Σ; _,_; ∃; Σ-syntax; ∃-syntax)
open import plfa.Relations using (_<_; z<s; s<s)
open import plfa.Isomorphism using (_≃_; ≃-sym; ≃-trans; _≲_; extensionality; _∘_)
open plfa.Isomorphism.≃-Reasoning
\end{code}

## Equality

#### Exercise `≤-reasoning` (stretch)

The proof of monotonicity from
Chapter [Relations][plfa.Relations]
can be written in a more readable form by using an anologue of our
notation for `≡-reasoning`.  Define `≤-reasoning` analogously, and use
it to write out an alternative proof that addition is monotonic with
regard to inequality.  Rewrite both `+-monoˡ-≤` and `+-mono-≤`.

\begin{code}

module ≤-reasoning {A : Set} where

  infix  1 ≤begin_
  infixr 2 _≤⟨⟩_ _≤⟨_⟩_
  infix  3 _≤∎ 

  ≤begin_ : ∀ {x y : ℕ}
    → x ≤ y
    -------
    → x ≤ y
  ≤begin x≤y = x≤y

  _≤⟨⟩_ : ∀ (x : ℕ) {y : ℕ}
    → x ≤ y
    -------
    → x ≤ y
  x ≤⟨⟩ x≤y = x≤y

  _≤⟨_⟩_ : ∀ (x : ℕ) {y z : ℕ}
    → x ≤ y
    → y ≤ z
    -------
    → x ≤ z
  x ≤⟨ x≤y ⟩ y≤z = ≤-trans x≤y y≤z

  _≤∎ : ∀ (x : ℕ)
      -----
    → x ≤ x
  x ≤∎  = ≤-refl

open ≤-reasoning

{-
+-monoL-≤ : ∀ (m n p : ℕ)
  → m ≤ n
  -------
  → m + p ≤ n + p
+-monoL-≤ m n p m≤n =
  ≤begin
    m ≤ n
  ≤⟨ m ≤ n ⟩
    m ≤ n + p
  ∎
-}

\end{code}

## Isomorphism

#### Exercise `≃-implies-≲`

Show that every isomorphism implies an embedding.
\begin{code}

≃-implies-≲ : ∀ {A B : Set}
  → A ≃ B
      -----
  → A ≲ B
≃-implies-≲ isoproof = record
  { to = _≃_.to isoproof
  ; from = _≃_.from isoproof
  ; from∘to = _≃_.from∘to isoproof }  

\end{code}

#### Exercise `_⇔_` (recommended) {#iff}

Define equivalence of propositions (also known as "if and only if") as follows.
\begin{code}
record _⇔_ (A B : Set) : Set where
  field
    to   : A → B
    from : B → A
open _⇔_

proof⇔refl : ∀ (A : Set) → (A ⇔ A)
proof⇔refl A =
  record
    { to = λ x → x
    ; from = λ x → x
    }

proof⇔sym : ∀ (X Y : Set) → (X ⇔ Y) → (Y ⇔ X)
proof⇔sym X Y rel =
  record 
    { to = from rel
    ; from = to rel
    }

proof⇔trans : ∀ (X Y Z : Set) → (X ⇔ Y) → (Y ⇔ Z) → (X ⇔ Z)
proof⇔trans X Y Z rel-a rel-b =
  record
    { to = (to rel-b) ∘ (to rel-a)
    ; from = (from rel-a) ∘ (from rel-b)
    }
    

\end{code}
Show that equivalence is reflexive, symmetric, and transitive.

#### Exercise `Bin-embedding` (stretch) {#Bin-embedding}

Recall that Exercises
[Bin][plfa.Naturals#Bin] and
[Bin-laws][plfa.Induction#Bin-laws]
define a datatype of bitstrings representing natural numbers.
\begin{code}
data Bin : Set where
  nil : Bin
  x0_ : Bin → Bin
  x1_ : Bin → Bin
\end{code}
And ask you to define the following functions:

    to : ℕ → Bin
    from : Bin → ℕ

which satisfy the following property:

    from (to n) ≡ n

Using the above, establish that there is an embedding of `ℕ` into `Bin`.
Why is there not an isomorphism?


## Connectives

#### Exercise `⇔≃×` (recommended)

Show that `A ⇔ B` as defined [earlier][plfa.Isomorphism#iff]
is isomorphic to `(A → B) × (B → A)`.

\begin{code}

⇔≃× : (A : Set) (B : Set) → A ⇔ B ≃ (A → B) × (B → A)
⇔≃× A B =
  record
    { to = λ x → ⟨ (to x) , (from x) ⟩
    ; from = λ x → record { to = ( proj₁ x) ; from = (proj₂ x) }
    ; from∘to = λ x → refl ; to∘from = λ y → refl
    }

\end{code}

#### Exercise `⊎-comm` (recommended)

Show sum is commutative up to isomorphism.

\begin{code}

⊎-comm : (A B : Set) → A ⊎ B ≃ B ⊎ A
⊎-comm A B =
  record
    { to = λ {(inj₁ x) → (inj₂ x); (inj₂ y) → (inj₁ y)}
    ; from = λ {(inj₁ x) → (inj₂ x); (inj₂ x) →  (inj₁ x)}
    ; from∘to = λ {(inj₁ x) → refl; (inj₂ y) → refl}
    ; to∘from = λ {(inj₁ x) → refl; (inj₂ y) → refl} }

\end{code}

#### Exercise `⊎-assoc`

Show sum is associative up to ismorphism.

\begin{code}

⊎-assoc : ∀ {A B : Set} → A ⊎ B ≃ B ⊎ A
⊎-assoc =
  record
    { to = λ { (inj₁ a) → (inj₂ a); (inj₂ b) → (inj₁ b) }
    ; from = λ { (inj₁ b) → (inj₂ b) ; (inj₂ b) → (inj₁ b) }
    ; from∘to = λ{(inj₁ x) → refl ; (inj₂ x) → refl }
    ; to∘from = λ{(inj₁ x) → refl ; (inj₂ x) → refl } }

\end{code}

#### Exercise `⊥-identityˡ` (recommended)

Show zero is the left identity of addition.

\begin{code} 

⊥-identityL : ∀ {A : Set} → ⊥ ⊎ A ≃ A
⊥-identityL =
  record
    { to = λ{ (inj₁ x) → ⊥-elim x ; (inj₂ y) → y }
    ; from = λ x → (inj₂ x)
    ; from∘to = λ{ (inj₁ ()) ; (inj₂ y) → refl}
    ; to∘from = λ y → refl
    }
    
\end{code}

#### Exercise `⊥-identityʳ`

Show zero is the right identity of addition. 

#### Exercise `⊎-weak-×` (recommended)

Show that the following property holds.
\begin{code}

⊎-weak-× : ∀ {A B C : Set} → (A ⊎ B) × C → A ⊎ (B × C)
⊎-weak-× ⟨ (inj₁ ab) , c ⟩ = (inj₁ ab) 
⊎-weak-× ⟨ (inj₂ ab) , c ⟩ = (inj₂ ⟨ ab , c ⟩) 

\end{code}
This is called a _weak distributive law_. Give the corresponding
distributive law, and explain how it relates to the weak version.

#### Exercise `⊎×-implies-×⊎`

Show that a disjunct of conjuncts implies a conjunct of disjuncts.
\begin{code}

⊎×-implies-×⊎ : ∀ {A B C D : Set} → (A × B) ⊎ (C × D) → (A ⊎ C) × (B ⊎ D)
⊎×-implies-×⊎ (inj₁ a) = ⟨ (inj₁ (proj₁ a)) , (inj₁ (proj₂ a)) ⟩
⊎×-implies-×⊎ (inj₂ a) = ⟨ (inj₂ ((proj₁ a))) , (inj₂ ((proj₂ a))) ⟩

\end{code}
Does the converse hold? If so, prove; if not, give a counterexample.


## Negation

#### Exercise `<-irreflexive` (recommended)

Using negation, show that
[strict inequality][plfa.Relations#strict-inequality]
is irreflexive, that is, `n < n` holds for no `n`.

\begin{code}

<-irreflexive : ∀ (n : ℕ) → ¬ (n < n)
<-irreflexive zero ()
<-irreflexive (suc n) (s<s n<n) = <-irreflexive n n<n

\end{code}

#### Exercise `trichotomy`

Show that strict inequality satisfies
[trichotomy][plfa.Relations#trichotomy],
that is, for any naturals `m` and `n` exactly one of the following holds:

* `m < n`
* `m ≡ n`
* `m > n`

Here "exactly one" means that one of the three must hold, and each implies the
negation of the other two.


#### Exercise `⊎-dual-×` (recommended)

Show that conjunction, disjunction, and negation are related by a
version of De Morgan's Law.

    ¬ (A ⊎ B) ≃ (¬ A) × (¬ B)

This result is an easy consequence of something we've proved previously.

Do we also have the following?

    ¬ (A × B) ≃ (¬ A) ⊎ (¬ B)

If so, prove; if not, give a variant that does hold.

\begin{code}
⊎-dual-x : ∀ {A B : Set} → ¬ (A ⊎ B) ≃ (¬ A) × (¬ B)
⊎-dual-x = record
  { to = λ f → ⟨  (λ x → f (inj₁ x)) , (λ y → f (inj₂ y)) ⟩
  ; from =
    λ { ⟨ g , h ⟩ (inj₁ x) → g x
      ; ⟨ g , h ⟩ (inj₂ y) → h y}
 
  ; from∘to = λ f → extensionality λ{ (inj₁ x) → refl ; (inj₂ y) → refl }
  ; to∘from = λ { ⟨ g , h ⟩  → refl }
  }



\end{code}

#### Exercise `Classical` (stretch)

Consider the following principles.

  * Excluded Middle: `A ⊎ ¬ A`, for all `A`
  * Double Negation Elimination: `¬ ¬ A → A`, for all `A`
  * Peirce's Law: `((A → B) → A) → A`, for all `A` and `B`.
  * Implication as disjunction: `(A → B) → ¬ A ⊎ B`, for all `A` and `B`.
  * De Morgan: `¬ (¬ A × ¬ B) → A ⊎ B`, for all `A` and `B`.

Show that each of these implies all the others.


#### Exercise `Stable` (stretch)

Say that a formula is _stable_ if double negation elimination holds for it.
\begin{code}
Stable : Set → Set
Stable A = ¬ ¬ A → A
\end{code}
Show that any negated formula is stable, and that the conjunction
of two stable formulas is stable.


## Quantifiers

#### Exercise `∀-distrib-×` (recommended)

Show that universals distribute over conjunction.
\begin{code}

∀-distrib-× : ∀ {A : Set} {B C : A → Set} →
  (∀ (x : A) → B x × C x) ≃ (∀ (x : A) → B x) × (∀ (x : A) → C x)
∀-distrib-× = record
  { to = λ x → ⟨ (λ x₁ → proj₁ (x x₁)) , (λ x₁ → proj₂ (x x₁)) ⟩
  ; from = λ rhs → λ a → ⟨ (proj₁ rhs) a , (proj₂ rhs) a ⟩
  ; from∘to = λ x → refl
  ; to∘from = λ y → refl }

\end{code}
Compare this with the result (`→-distrib-×`) in
Chapter [Connectives][plfa.Connectives].

#### Exercise `⊎∀-implies-∀⊎`

Show that a disjunction of universals implies a universal of disjunctions.
\begin{code}
⊎∀-implies-∀⊎ : ∀ {A : Set} { B C : A → Set } →
  (∀ (x : A) → B x) ⊎ (∀ (x : A) → C x)  →  ∀ (x : A) → B x ⊎ C x
⊎∀-implies-∀⊎ (inj₁ bf) x = inj₁ (bf x)
⊎∀-implies-∀⊎ (inj₂ cf) x = inj₂ (cf x)

\end{code}
Does the converse hold? If so, prove; if not, explain why.

#### Exercise `∃-distrib-⊎` (recommended)

Show that existentials distribute over disjunction.
\begin{code}
∃-distrib-⊎ : ∀ {A : Set} {B C : A → Set} →
    ∃[ x ] (B x ⊎ C x) ≃ (∃[ x ] B x) ⊎ (∃[ x ] C x)
∃-distrib-⊎ = record
  { to =
    λ { ⟨ a , (inj₁ bc)⟩ → (inj₁ ⟨ a , bc ⟩)
      ; ⟨ a , (inj₂ bc)⟩ → (inj₂ ⟨ a , bc ⟩)
    }
  ; from =
    λ { (inj₁ (⟨ a , b ⟩)) → ⟨ a , (inj₁ b) ⟩
      ; (inj₂ (⟨ a , c ⟩)) → ⟨ a , (inj₂ c) ⟩
    }
  ; from∘to =
    λ { ⟨ a , (inj₁ bc) ⟩ → refl
      ; ⟨ a , (inj₂ bc) ⟩ → refl }
  ; to∘from =
    λ { (inj₁ (⟨ a , b ⟩)) → refl
      ; (inj₂ (⟨ a , c ⟩)) → refl }
  }

\end{code}

#### Exercise `∃×-implies-×∃`

Show that an existential of conjunctions implies a conjunction of existentials.
\begin{code}
∃×-implies-×∃ : ∀ {A : Set} { B C : A → Set } →
     ∃[ x ] (B x × C x) → (∃[ x ] B x) × (∃[ x ] C x)
∃×-implies-×∃ ⟨ x , ⟨ Bx , Cx ⟩ ⟩ = ⟨ ⟨ x , Bx ⟩ , ⟨ x , Cx ⟩ ⟩
-- ∃×-implies-×∃ ⟨ x , bANDc ⟩ = ⟨ ⟨ x , (proj₁ bANDc) ⟩ , ⟨ x , (proj₂ bANDc) ⟩ ⟩

\end{code}
Does the converse hold? If so, prove; if not, explain why.

No the converse does not hold as the x that indexes into B may not be the same x that indexes into C.

#### Exercise `∃-even-odd`

How do the proofs become more difficult if we replace `m * 2` and `1 + m * 2`
by `2 * m` and `2 * m + 1`?  Rewrite the proofs of `∃-even` and `∃-odd` when
restated in this way.

#### Exercise `∃-+-≤`

Show that `y ≤ z` holds if and only if there exists a `x` such that
`x + y ≡ z`.

\begin{code}
{-
+nzero : ∀ (n : ℕ) → n + zero ≡ n
+nzero zero = refl
+nzero (suc n) =
  begin
    suc (n + zero)
  ≡⟨ cong suc (+-comm n zero) ⟩
    suc (zero + n)
  ≡⟨⟩
    suc n
  ∎

+lemma_helper : ∀ (n m) → n ∸ m + suc m ≡ suc n
+lemma_helper zero m =
  begin
    zero ∸ m + suc m
  ≡⟨⟩
    zero + suc 

∃-+-≤ : ∀ (x y z : ℕ) → y ≤ z ≃ ∃[ x ] (x + y ≡ z)
∃-+-≤ x y z = record
  { to = λ {(z≤n) → ⟨ (z ∸ y), +nzero z ⟩ ; (s≤s n≤m) → ⟨ (z ∸ y) , {!!} ⟩ }
  ; from = {!!}
  ; from∘to = {!!}
  ; to∘from = {!!} }
-}
\end{code}

#### Exercise `∃¬-implies-¬∀` (recommended)

Show that existential of a negation implies negation of a universal.
\begin{code}
∃¬-implies-¬∀ : ∀ {A : Set} {B : A → Set}
  → ∃[ x ] (¬ B x)
    --------------
  → ¬ (∀ x → B x)
∃¬-implies-¬∀ ⟨ x , f ⟩ = λ x₁ → f (x₁ x)









--∃¬-implies-¬∀ ⟨ a , f ⟩ b = f (b a) 

\end{code}
Does the converse hold? If so, prove; if not, explain why.


#### Exercise `Bin-isomorphism` (stretch) {#Bin-isomorphism}

Recall that Exercises
[Bin][plfa.Naturals#Bin],
[Bin-laws][plfa.Induction#Bin-laws], and
[Bin-predicates][plfa.Relations#Bin-predicates]
define a datatype of bitstrings representing natural numbers.

    data Bin : Set where
      nil : Bin
      x0_ : Bin → Bin
      x1_ : Bin → Bin

And ask you to define the following functions and predicates.

    to   : ℕ → Bin
    from : Bin → ℕ
    Can  : Bin → Set

And to establish the following properties.

    from (to n) ≡ n

    ----------
    Can (to n)

    Can x
    ---------------
    to (from x) ≡ x

Using the above, establish that there is an isomorphism between `ℕ` and
`∃[ x ](Can x)`.


## Decidable

#### Exercise `_<?_` (recommended)

Analogous to the function above, define a function to decide strict inequality.
\begin{code}
¬s<z : ∀ {m : ℕ} → ¬ (suc m < zero)
¬s<z ()

¬z<z : ¬(zero < zero)
¬z<z ()

¬s<s : ∀ {m n : ℕ} → ¬ (m < n) → ¬ (suc m < suc n)
¬s<s ¬m<n (s<s m<n) = (¬m<n) m<n 

_<?_ : ∀ (m n : ℕ) → Dec (m < n)
zero  <? zero  = no ¬z<z
zero  <? suc n = yes z<s
suc m <? zero  = no ¬s<z
suc m <? suc n with m <? n
...               | yes m<n  = yes (s<s m<n)
...               | no  ¬m<n = no  (¬s<s ¬m<n)
\end{code}

#### Exercise `_≡ℕ?_`

Define a function to decide whether two naturals are equal.
\begin{code}

¬s≡z : ∀ (n : ℕ) → ¬(suc n ≡ zero)
¬s≡z n = λ ()

¬z≡s : ∀ (n : ℕ) → ¬(zero ≡ suc n)
¬z≡s n = λ ()

cpp : ∀ {n m : ℕ} → (suc n ≡ suc m) → (n ≡ m)
cpp {zero} {zero} sz≡sz = refl
cpp {suc n} {zero} ()
cpp {zero} {suc n} ()
cpp {suc n} {suc m} = λ {refl → refl}

lemma-helper : ∀ {n m : ℕ} → ¬ (n ≡ m) → ¬ (suc n ≡ suc m)
lemma-helper eq = contraposition cpp eq

_≡ℕ?_ : ∀ (m n : ℕ) → Dec (m ≡ n)
_≡ℕ?_ zero zero = yes refl
_≡ℕ?_ (suc n) zero = no (¬s≡z n)
_≡ℕ?_ zero (suc n) = no (¬z≡s n)
_≡ℕ?_ (suc n) (suc m) with n ≡ℕ? m
...                      | yes n≡m = yes (cong suc n≡m)
...                      | no ¬n≡m = no (lemma-helper ¬n≡m)

\end{code}


#### Exercise `erasure`

Show that erasure relates corresponding boolean and decidable operations.
\begin{code}
postulate
  ∧-× : ∀ {A B : Set} (x : Dec A) (y : Dec B) → ⌊ x ⌋ ∧ ⌊ y ⌋ ≡ ⌊ x ×-dec y ⌋
  ∨-× : ∀ {A B : Set} (x : Dec A) (y : Dec B) → ⌊ x ⌋ ∨ ⌊ y ⌋ ≡ ⌊ x ⊎-dec y ⌋
  not-¬ : ∀ {A : Set} (x : Dec A) → not ⌊ x ⌋ ≡ ⌊ ¬? x ⌋
\end{code}
  
#### Exercise `iff-erasure` (recommended)

Give analogues of the `_⇔_` operation from 
Chapter [Isomorphism][plfa.Isomorphism#iff],
operation on booleans and decidables, and also show the corresponding erasure.
\begin{code}

_iff_ : Bool → Bool → Bool 
_iff_ true true   = true
_iff_ true false  = false
_iff_ false true  = false
_iff_ false false = true

_⇔-dec_ : ∀ {A B : Set} → Dec A → Dec B → Dec (A ⇔ B)
_⇔-dec_ (yes a) (yes b) = yes (record { to = λ x → b; from = λ x → a })
_⇔-dec_ (yes a) (no ¬b) = no λ aIFFb →  ¬b (( _⇔_.to aIFFb) a)
_⇔-dec_ (no ¬a) (yes b) = no λ aIFFb → ¬a (( _⇔_.from aIFFb) b)
_⇔-dec_ (no ¬a) (no ¬b) = yes (record { to = λ x → ⊥-elim (¬a x); from = λ y → ⊥-elim (¬b y)})

iff-⇔ : ∀ {A B : Set} (x : Dec A) (y : Dec B) → ⌊ x ⌋ iff ⌊ y ⌋ ≡ ⌊ x ⇔-dec y ⌋  
iff-⇔ (yes a) (yes b) = refl
iff-⇔ (yes a) (no ¬b) = refl
iff-⇔ (no ¬a) (yes b) = refl
iff-⇔ (no ¬a) (no ¬b) = refl

\end{code}

REVISION

\begin{code}

_∘2_ : ∀ {A B C : Set} → (B → C) → (A → B) → (A → C)
--(g ∘2 f) a = g (f a)
(g ∘2 f) = λ a → g (f a)

_+′_ : ℕ → ℕ → ℕ
m +′ zero  = m
m +′ suc n = suc (m +′ n)

same-app2 : ∀ (m n : ℕ) → m +′ n ≡ m + n
same-app2 m n rewrite (+-comm m n) = {!helper m n!} 
  where
  helper : ∀ (m n : ℕ) → m +′ n ≡ n + m
  helper m zero = refl
  helper m (suc n) = cong suc (helper m n)

same : _+′_ ≡ _+_
same = extensionality (λ m → extensionality (λ n → same-app2 m n)) 

trans′ : ∀ {A : Set} {x y z : A}
  → x ≡ y
  → y ≡ z
    -----
  → x ≡ z
trans′ {A} {x} {y} {z} x≡y y≡z =
  begin
    x
  ≡⟨ x≡y ⟩
    y
  ≡⟨ y≡z ⟩
    z
  ∎

≃-refl : ∀ {A : Set}
    -----
  → A ≃ A
≃-refl =
  record
  { to = λ x → x
  ; from = λ x → x
  ; from∘to = λ x → refl
  ; to∘from = λ y → refl }

×-comm : ∀ {A B : Set} → A × B ≃ B × A
×-comm = record
  { to = λ{ ⟨ x , y ⟩ → ⟨ y , x ⟩}
  ; from = λ{ ⟨ y , x ⟩ → ⟨ x , y ⟩ }
  ; from∘to = λ x → refl
  ; to∘from = λ y → refl }

{-
×-assoc : ∀ {A B C : Set} → (A × B) × C ≃ A × (B × C)
×-assoc = record
  { to = λ { ⟨ ⟨ x , y ⟩ , z ⟩ → ⟨ x , ⟨ y , z ⟩ ⟩ }
  ; from = {!!}
  ; from∘to = {!!}
  ; to∘from = {!!} }
-}


≃-sym2 : ∀ {A B : Set}
  → A ≃ B
    -----
  → B ≃ A
≃-sym2 A≃B =
  record
    { to      = _≃_.from A≃B
    ; from    = _≃_.to   A≃B
    ; from∘to = _≃_.to∘from A≃B
    ; to∘from = _≃_.from∘to A≃B
    }

{-
⊤-identityˡ : ∀ {A : Set} → ⊤ × A ≃ A
⊤-identityˡ = record
  { to = λ { ⟨ tt , A ⟩ → A }
  ; from = {!!}
  ; from∘to = {!!}
  ; to∘from = {!!} }
-}

case-⊎2 : ∀ {A B C : Set}
  → (A → C)
  → (B → C)
  → A ⊎ B
    -----------
  → C
case-⊎2 f g (inj₁ a) = f a
case-⊎2 f g (inj₂ b) = g b

⊥-elim2 : ∀ {A : Set}
  → ⊥
    --
  → A
⊥-elim2 ()


∀-elim : ∀ {A : Set} {B : A → Set}
  → (L : ∀ (x : A) → B x)
  → (M : A)
    -----------------
  → B M
∀-elim f m = f m


data even : ℕ → Set
data odd  : ℕ → Set

data even where

  even-zero : even zero

  even-suc : ∀ {n : ℕ}
    → odd n
      ------------
    → even (suc n)

data odd where
  odd-suc : ∀ {n : ℕ}
    → even n
      -----------
    → odd (suc n)

even-∃ : ∀ {n : ℕ} → even n → ∃[ m ] (    m * 2 ≡ n)
odd-∃  : ∀ {n : ℕ} →  odd n → ∃[ m ] (1 + m * 2 ≡ n)

even-∃ even-zero                       =  ⟨ zero , refl ⟩
even-∃ (even-suc o) with odd-∃ o
...                    | ⟨ m , refl ⟩  =  ⟨ suc m , refl ⟩

odd-∃  (odd-suc e)  with even-∃ e
...                    | ⟨ m , refl ⟩  =  ⟨ m , refl ⟩







2≤4 : 2 ≤ 4
2≤4 = s≤s ( s≤s ( z≤n ) )

¬4≤2 : ¬ (4 ≤ 2)
¬4≤2 (s≤s ( s≤s () ))


\end{code}
