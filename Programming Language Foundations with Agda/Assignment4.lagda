---
title     : "Assignment4: TSPL Assignment 4"
layout    : page
permalink : /Assignment4/
---

\begin{code}
module Assignment4 where
\end{code}

## YOUR NAME AND EMAIL GOES HERE
Oliver Goldstein s1424164

## Introduction

This assignment is due **4pm Thursday 15 November** (Week 9).

You must do _all_ the exercises labelled "(recommended)".

Exercises labelled "(stretch)" are there to provide an extra challenge.
You don't need to do all of these, but should attempt at least a few.

Exercises without a label are optional, and may be done if you want
some extra practice.

Submit your homework using the "submit" command.
Please ensure your files execute correctly under Agda!

_IMPORTANT_ For ease of marking, when modifying the given code please write

  -- begin
  -- end
before and after code you add, to indicate your changes.


## Imports

\begin{code}
import Relation.Binary.PropositionalEquality as Eq
open Eq using (_≡_; refl; sym; trans; cong; cong₂; _≢_)
open import Data.Empty using (⊥; ⊥-elim)
open import Data.Nat using (ℕ; zero; suc; _+_; _*_)
open import Data.Product using (_×_; ∃; ∃-syntax) renaming (_,_ to ⟨_,_⟩)
open import Data.String using (String)
open import Data.String.Unsafe using (_≟_)
open import Relation.Nullary using (¬_; Dec; yes; no)
\end{code}


## DeBruijn


\begin{code}
module DeBruijn where
\end{code}

Remember to indent all code by two spaces.

\begin{code}
  open import plfa.DeBruijn
\end{code}


#### Exercise (`mul`) (recommended)

Write out the definition of a lambda term that multiplies
two natural numbers, now adapted to the inherently typed
DeBruijn represenation.

\begin{code}

  mul : ∀ {Γ} → Γ ⊢ `ℕ ⇒ `ℕ ⇒ `ℕ
  mul = μ ƛ ƛ (case (# 1) (`zero) (plus · # 1 · (# 3 · # 0 · # 1)))

\end{code}

#### Exercise `V¬—→`

Following the previous development, show values do
not reduce, and its corollary, terms that reduce are not
values.


#### Exercise `mul-example` (recommended)

Using the evaluator, confirm that two times two is four.

\begin{code}
{-
  2*2 : ∅ ⊢ `ℕ
  2*2 = mul · two · two

  _ : eval (gas 100) 2*2 ≡
    steps
    ((μ
      (ƛ
       (ƛ
        case (` (S Z)) `zero
        ((μ
          (ƛ
           (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` (S Z)
         · (` (S (S (S Z))) · ` Z · ` (S Z))))))
     · `suc (`suc `zero)
     · `suc (`suc `zero)
     —→⟨ ξ-·₁ (ξ-·₁ β-μ) ⟩
     (ƛ
      (ƛ
       case (` (S Z)) `zero
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` (S Z)
        ·
        ((μ
          (ƛ
           (ƛ
            case (` (S Z)) `zero
            ((μ
              (ƛ
               (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
             · ` (S Z)
             · (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` Z
         · ` (S Z)))))
     · `suc (`suc `zero)
     · `suc (`suc `zero)
     —→⟨ ξ-·₁ (β-ƛ (V-suc (V-suc V-zero))) ⟩
     (ƛ
      case (`suc (`suc `zero)) `zero
      ((μ
        (ƛ
         (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · ` (S Z)
       ·
       ((μ
         (ƛ
          (ƛ
           case (` (S Z)) `zero
           ((μ
             (ƛ
              (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
            · ` (S Z)
            · (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     · `suc (`suc `zero)
     —→⟨ β-ƛ (V-suc (V-suc V-zero)) ⟩
     case (`suc (`suc `zero)) `zero
     ((μ
       (ƛ
        (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
      · `suc (`suc `zero)
      ·
      ((μ
        (ƛ
         (ƛ
          case (` (S Z)) `zero
          ((μ
            (ƛ
             (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
           · ` (S Z)
           · (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · ` Z
       · `suc (`suc `zero)))
     —→⟨ β-suc (V-suc V-zero) ⟩
     (μ
      (ƛ
       (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
     · `suc (`suc `zero)
     ·
     ((μ
       (ƛ
        (ƛ
         case (` (S Z)) `zero
         ((μ
           (ƛ
            (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
          · ` (S Z)
          · (` (S (S (S Z))) · ` Z · ` (S Z))))))
      · `suc `zero
      · `suc (`suc `zero))
     —→⟨ ξ-·₁ (ξ-·₁ β-μ) ⟩
     (ƛ
      (ƛ
       case (` (S Z)) (` Z)
       (`suc
        ((μ
          (ƛ
           (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` Z
         · ` (S Z)))))
     · `suc (`suc `zero)
     ·
     ((μ
       (ƛ
        (ƛ
         case (` (S Z)) `zero
         ((μ
           (ƛ
            (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
          · ` (S Z)
          · (` (S (S (S Z))) · ` Z · ` (S Z))))))
      · `suc `zero
      · `suc (`suc `zero))
     —→⟨ ξ-·₁ (β-ƛ (V-suc (V-suc V-zero))) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     ((μ
       (ƛ
        (ƛ
         case (` (S Z)) `zero
         ((μ
           (ƛ
            (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
          · ` (S Z)
          · (` (S (S (S Z))) · ` Z · ` (S Z))))))
      · `suc `zero
      · `suc (`suc `zero))
     —→⟨ ξ-·₂ V-ƛ (ξ-·₁ (ξ-·₁ β-μ)) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     ((ƛ
       (ƛ
        case (` (S Z)) `zero
        ((μ
          (ƛ
           (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` (S Z)
         ·
         ((μ
           (ƛ
            (ƛ
             case (` (S Z)) `zero
             ((μ
               (ƛ
                (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
              · ` (S Z)
              · (` (S (S (S Z))) · ` Z · ` (S Z))))))
          · ` Z
          · ` (S Z)))))
      · `suc `zero
      · `suc (`suc `zero))
     —→⟨ ξ-·₂ V-ƛ (ξ-·₁ (β-ƛ (V-suc V-zero))) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     ((ƛ
       case (`suc `zero) `zero
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` (S Z)
        ·
        ((μ
          (ƛ
           (ƛ
            case (` (S Z)) `zero
            ((μ
              (ƛ
               (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
             · ` (S Z)
             · (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` Z
         · ` (S Z))))
      · `suc (`suc `zero))
     —→⟨ ξ-·₂ V-ƛ (β-ƛ (V-suc (V-suc V-zero))) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     case (`suc `zero) `zero
     ((μ
       (ƛ
        (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
      · `suc (`suc `zero)
      ·
      ((μ
        (ƛ
         (ƛ
          case (` (S Z)) `zero
          ((μ
            (ƛ
             (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
           · ` (S Z)
           · (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · ` Z
       · `suc (`suc `zero)))
     —→⟨ ξ-·₂ V-ƛ (β-suc V-zero) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     ((μ
       (ƛ
        (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
      · `suc (`suc `zero)
      ·
      ((μ
        (ƛ
         (ƛ
          case (` (S Z)) `zero
          ((μ
            (ƛ
             (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
           · ` (S Z)
           · (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · `zero
       · `suc (`suc `zero)))
     —→⟨ ξ-·₂ V-ƛ (ξ-·₁ (ξ-·₁ β-μ)) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     ((ƛ
       (ƛ
        case (` (S Z)) (` Z)
        (`suc
         ((μ
           (ƛ
            (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
          · ` Z
          · ` (S Z)))))
      · `suc (`suc `zero)
      ·
      ((μ
        (ƛ
         (ƛ
          case (` (S Z)) `zero
          ((μ
            (ƛ
             (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
           · ` (S Z)
           · (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · `zero
       · `suc (`suc `zero)))
     —→⟨ ξ-·₂ V-ƛ (ξ-·₁ (β-ƛ (V-suc (V-suc V-zero)))) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     ((ƛ
       case (`suc (`suc `zero)) (` Z)
       (`suc
        ((μ
          (ƛ
           (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` Z
         · ` (S Z))))
      ·
      ((μ
        (ƛ
         (ƛ
          case (` (S Z)) `zero
          ((μ
            (ƛ
             (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
           · ` (S Z)
           · (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · `zero
       · `suc (`suc `zero)))
     —→⟨ ξ-·₂ V-ƛ (ξ-·₂ V-ƛ (ξ-·₁ (ξ-·₁ β-μ))) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     ((ƛ
       case (`suc (`suc `zero)) (` Z)
       (`suc
        ((μ
          (ƛ
           (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` Z
         · ` (S Z))))
      ·
      ((ƛ
        (ƛ
         case (` (S Z)) `zero
         ((μ
           (ƛ
            (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
          · ` (S Z)
          ·
          ((μ
            (ƛ
             (ƛ
              case (` (S Z)) `zero
              ((μ
                (ƛ
                 (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
               · ` (S Z)
               · (` (S (S (S Z))) · ` Z · ` (S Z))))))
           · ` Z
           · ` (S Z)))))
       · `zero
       · `suc (`suc `zero)))
     —→⟨ ξ-·₂ V-ƛ (ξ-·₂ V-ƛ (ξ-·₁ (β-ƛ V-zero))) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     ((ƛ
       case (`suc (`suc `zero)) (` Z)
       (`suc
        ((μ
          (ƛ
           (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` Z
         · ` (S Z))))
      ·
      ((ƛ
        case `zero `zero
        ((μ
          (ƛ
           (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` (S Z)
         ·
         ((μ
           (ƛ
            (ƛ
             case (` (S Z)) `zero
             ((μ
               (ƛ
                (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
              · ` (S Z)
              · (` (S (S (S Z))) · ` Z · ` (S Z))))))
          · ` Z
          · ` (S Z))))
       · `suc (`suc `zero)))
     —→⟨ ξ-·₂ V-ƛ (ξ-·₂ V-ƛ (β-ƛ (V-suc (V-suc V-zero)))) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     ((ƛ
       case (`suc (`suc `zero)) (` Z)
       (`suc
        ((μ
          (ƛ
           (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` Z
         · ` (S Z))))
      ·
      case `zero `zero
      ((μ
        (ƛ
         (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · `suc (`suc `zero)
       ·
       ((μ
         (ƛ
          (ƛ
           case (` (S Z)) `zero
           ((μ
             (ƛ
              (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
            · ` (S Z)
            · (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · `suc (`suc `zero))))
     —→⟨ ξ-·₂ V-ƛ (ξ-·₂ V-ƛ β-zero) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     ((ƛ
       case (`suc (`suc `zero)) (` Z)
       (`suc
        ((μ
          (ƛ
           (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` Z
         · ` (S Z))))
      · `zero)
     —→⟨ ξ-·₂ V-ƛ (β-ƛ V-zero) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     case (`suc (`suc `zero)) `zero
     (`suc
      ((μ
        (ƛ
         (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · ` Z
       · `zero))
     —→⟨ ξ-·₂ V-ƛ (β-suc (V-suc V-zero)) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     `suc
     ((μ
       (ƛ
        (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
      · `suc `zero
      · `zero)
     —→⟨ ξ-·₂ V-ƛ (ξ-suc (ξ-·₁ (ξ-·₁ β-μ))) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     `suc
     ((ƛ
       (ƛ
        case (` (S Z)) (` Z)
        (`suc
         ((μ
           (ƛ
            (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
          · ` Z
          · ` (S Z)))))
      · `suc `zero
      · `zero)
     —→⟨ ξ-·₂ V-ƛ (ξ-suc (ξ-·₁ (β-ƛ (V-suc V-zero)))) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     `suc
     ((ƛ
       case (`suc `zero) (` Z)
       (`suc
        ((μ
          (ƛ
           (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` Z
         · ` (S Z))))
      · `zero)
     —→⟨ ξ-·₂ V-ƛ (ξ-suc (β-ƛ V-zero)) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     `suc
     case (`suc `zero) `zero
     (`suc
      ((μ
        (ƛ
         (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · ` Z
       · `zero))
     —→⟨ ξ-·₂ V-ƛ (ξ-suc (β-suc V-zero)) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     `suc
     (`suc
      ((μ
        (ƛ
         (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · `zero
       · `zero))
     —→⟨ ξ-·₂ V-ƛ (ξ-suc (ξ-suc (ξ-·₁ (ξ-·₁ β-μ)))) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     `suc
     (`suc
      ((ƛ
        (ƛ
         case (` (S Z)) (` Z)
         (`suc
          ((μ
            (ƛ
             (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
           · ` Z
           · ` (S Z)))))
       · `zero
       · `zero))
     —→⟨ ξ-·₂ V-ƛ (ξ-suc (ξ-suc (ξ-·₁ (β-ƛ V-zero)))) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     `suc
     (`suc
      ((ƛ
        case `zero (` Z)
        (`suc
         ((μ
           (ƛ
            (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
          · ` Z
          · ` (S Z))))
       · `zero))
     —→⟨ ξ-·₂ V-ƛ (ξ-suc (ξ-suc (β-ƛ V-zero))) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     ·
     `suc
     (`suc
      case `zero `zero
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · `zero)))
     —→⟨ ξ-·₂ V-ƛ (ξ-suc (ξ-suc β-zero)) ⟩
     (ƛ
      case (`suc (`suc `zero)) (` Z)
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · ` (S Z))))
     · `suc (`suc `zero)
     —→⟨ β-ƛ (V-suc (V-suc V-zero)) ⟩
     case (`suc (`suc `zero)) (`suc (`suc `zero))
     (`suc
      ((μ
        (ƛ
         (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · ` Z
       · `suc (`suc `zero)))
     —→⟨ β-suc (V-suc V-zero) ⟩
     `suc
     ((μ
       (ƛ
        (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
      · `suc `zero
      · `suc (`suc `zero))
     —→⟨ ξ-suc (ξ-·₁ (ξ-·₁ β-μ)) ⟩
     `suc
     ((ƛ
       (ƛ
        case (` (S Z)) (` Z)
        (`suc
         ((μ
           (ƛ
            (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
          · ` Z
          · ` (S Z)))))
      · `suc `zero
      · `suc (`suc `zero))
     —→⟨ ξ-suc (ξ-·₁ (β-ƛ (V-suc V-zero))) ⟩
     `suc
     ((ƛ
       case (`suc `zero) (` Z)
       (`suc
        ((μ
          (ƛ
           (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
         · ` Z
         · ` (S Z))))
      · `suc (`suc `zero))
     —→⟨ ξ-suc (β-ƛ (V-suc (V-suc V-zero))) ⟩
     `suc
     case (`suc `zero) (`suc (`suc `zero))
     (`suc
      ((μ
        (ƛ
         (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · ` Z
       · `suc (`suc `zero)))
     —→⟨ ξ-suc (β-suc V-zero) ⟩
     `suc
     (`suc
      ((μ
        (ƛ
         (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
       · `zero
       · `suc (`suc `zero)))
     —→⟨ ξ-suc (ξ-suc (ξ-·₁ (ξ-·₁ β-μ))) ⟩
     `suc
     (`suc
      ((ƛ
        (ƛ
         case (` (S Z)) (` Z)
         (`suc
          ((μ
            (ƛ
             (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
           · ` Z
           · ` (S Z)))))
       · `zero
       · `suc (`suc `zero)))
     —→⟨ ξ-suc (ξ-suc (ξ-·₁ (β-ƛ V-zero))) ⟩
     `suc
     (`suc
      ((ƛ
        case `zero (` Z)
        (`suc
         ((μ
           (ƛ
            (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
          · ` Z
          · ` (S Z))))
       · `suc (`suc `zero)))
     —→⟨ ξ-suc (ξ-suc (β-ƛ (V-suc (V-suc V-zero)))) ⟩
     `suc
     (`suc
      case `zero (`suc (`suc `zero))
      (`suc
       ((μ
         (ƛ
          (ƛ case (` (S Z)) (` Z) (`suc (` (S (S (S Z))) · ` Z · ` (S Z))))))
        · ` Z
        · `suc (`suc `zero))))
     —→⟨ ξ-suc (ξ-suc β-zero) ⟩ `suc (`suc (`suc (`suc `zero))) ∎)
    (done (V-suc (V-suc (V-suc (V-suc V-zero)))))
    
  _ = refl
-}

\end{code}

## More

\begin{code}
module More where
\end{code}

Remember to indent all code by two spaces.


### Syntax

\begin{code}
  infix  4 _⊢_
  infix  4 _∋_
  infixl 5 _,_

  infixr 7 _⇒_
  infixr 8 _`⊎_
  infixr 9 _`×_

  infix  5 ƛ_
  infix  5 μ_
  infixl 7 _·_
  infixl 8 _`*_
  infix  8 `suc_
  infix  9 `_
  infix  9 S_
  infix  9 #_
\end{code}

### Types

\begin{code}
  data Type : Set where
    `ℕ    : Type
    _⇒_   : Type → Type → Type
    Nat   : Type
    _`×_  : Type → Type → Type
    _`⊎_  : Type → Type → Type
    `⊤    : Type
    `⊥    : Type
    `List : Type → Type
\end{code}

### Contexts

\begin{code}
  data Context : Set where
    ∅   : Context
    _,_ : Context → Type → Context
\end{code}

### Variables and the lookup judgement

\begin{code}
  data _∋_ : Context → Type → Set where

    Z : ∀ {Γ A}
        ---------
      → Γ , A ∋ A

    S_ : ∀ {Γ A B}
      → Γ ∋ B
        ---------
      → Γ , A ∋ B
\end{code}   

### Terms and the typing judgment

\begin{code}
  data _⊢_ : Context → Type → Set where

    -- variables

    `_ : ∀ {Γ A}
      → Γ ∋ A
        -----
      → Γ ⊢ A

    -- functions

    ƛ_  :  ∀ {Γ A B}
      → Γ , A ⊢ B
        ---------
      → Γ ⊢ A ⇒ B

    _·_ : ∀ {Γ A B}
      → Γ ⊢ A ⇒ B
      → Γ ⊢ A
        ---------
      → Γ ⊢ B

    -- naturals

    `zero : ∀ {Γ}
        ------
      → Γ ⊢ `ℕ

    `suc_ : ∀ {Γ}
      → Γ ⊢ `ℕ
        ------
      → Γ ⊢ `ℕ

    case : ∀ {Γ A}
      → Γ ⊢ `ℕ
      → Γ ⊢ A
      → Γ , `ℕ ⊢ A
        -----
      → Γ ⊢ A

    -- fixpoint

    μ_ : ∀ {Γ A}
      → Γ , A ⊢ A
        ----------
      → Γ ⊢ A

    -- primitive numbers

    con : ∀ {Γ}
      → ℕ
        -------
      → Γ ⊢ Nat

    _`*_ : ∀ {Γ}
      → Γ ⊢ Nat
      → Γ ⊢ Nat
        -------
      → Γ ⊢ Nat

    -- let

    `let : ∀ {Γ A B}
      → Γ ⊢ A
      → Γ , A ⊢ B
        ----------
      → Γ ⊢ B

    -- products

    `⟨_,_⟩ : ∀ {Γ A B}
      → Γ ⊢ A
      → Γ ⊢ B
        -----------
      → Γ ⊢ A `× B

    `proj₁ : ∀ {Γ A B}
      → Γ ⊢ A `× B
        -----------
      → Γ ⊢ A

    `proj₂ : ∀ {Γ A B}
      → Γ ⊢ A `× B
        -----------
      → Γ ⊢ B

    `inj₁ : ∀ {Γ A B}
      → Γ ⊢ A
      --------------
      → Γ ⊢ A `⊎ B

    `inj₂ : ∀ {Γ A B}
      → Γ ⊢ B
      --------------
      → Γ ⊢ A `⊎ B

    case⊎ : ∀ {Γ A B C}
      → Γ ⊢ A `⊎ B
      → Γ , A ⊢ C
      → Γ , B ⊢ C
      ---------
      → Γ ⊢ C

    case⊥ : ∀ {Γ A}
      → Γ ⊢ `⊥
      ----------
      → Γ ⊢ A
    -- alternative formulation of products

    case⊤ : ∀ {Γ A}
      → Γ ⊢ `⊤
      → Γ ⊢ A
      --------------
      → Γ ⊢ A

    `tt : ∀ {Γ}
      → Γ ⊢ `⊤
      
    `[] : ∀ {Γ A}
      -------
      → Γ ⊢ `List A

    _`∷_ : ∀ {Γ A}
      → Γ ⊢ A
      → Γ ⊢ `List A
      -------
      → Γ ⊢ `List A

    caseL : ∀ {Γ A B}
      → Γ ⊢ `List A
      → Γ ⊢ B
      → Γ , A , `List A ⊢ B
      ----------
      → Γ ⊢ B
  
    case× : ∀ {Γ A B C}
      → Γ ⊢ A `× B
      → Γ , A , B ⊢ C
        --------------
      → Γ ⊢ C

\end{code}

### Abbreviating de Bruijn indices

\begin{code}
  lookup : Context → ℕ → Type
  lookup (Γ , A) zero     =  A
  lookup (Γ , _) (suc n)  =  lookup Γ n
  lookup ∅       _        =  ⊥-elim impossible
    where postulate impossible : ⊥

  count : ∀ {Γ} → (n : ℕ) → Γ ∋ lookup Γ n
  count {Γ , _} zero     =  Z
  count {Γ , _} (suc n)  =  S (count n)
  count {∅}     _        =  ⊥-elim impossible
    where postulate impossible : ⊥

  #_ : ∀ {Γ} → (n : ℕ) → Γ ⊢ lookup Γ n
  # n  =  ` count n
\end{code}

## Renaming

\begin{code}
  ext : ∀ {Γ Δ} → (∀ {A} → Γ ∋ A → Δ ∋ A) → (∀ {A B} → Γ , A ∋ B → Δ , A ∋ B)
  ext ρ Z      =  Z
  ext ρ (S x)  =  S (ρ x)

  rename : ∀ {Γ Δ} → (∀ {A} → Γ ∋ A → Δ ∋ A) → (∀ {A} → Γ ⊢ A → Δ ⊢ A)
  rename ρ (` x)          =  ` (ρ x)
  rename ρ (ƛ N)          =  ƛ (rename (ext ρ) N)
  rename ρ (L · M)        =  (rename ρ L) · (rename ρ M)
  rename ρ (`zero)        =  `zero
  rename ρ (`suc M)       =  `suc (rename ρ M)
  rename ρ (case L M N)   =  case (rename ρ L) (rename ρ M) (rename (ext ρ) N)
  rename ρ (μ N)          =  μ (rename (ext ρ) N)
  rename ρ (con n)        =  con n
  rename ρ (M `* N)       =  rename ρ M `* rename ρ N
  rename ρ (`let M N)     =  `let (rename ρ M) (rename (ext ρ) N)
  rename ρ `⟨ M , N ⟩     =  `⟨ rename ρ M , rename ρ N ⟩
  rename ρ (`proj₁ L)     =  `proj₁ (rename ρ L)
  rename ρ (`proj₂ L)     =  `proj₂ (rename ρ L)
  rename ρ (`inj₁ L)      =  `inj₁ (rename ρ L)
  rename ρ (`inj₂ L)      =  `inj₂ (rename ρ L)
  rename ρ (case⊎ L M N)  =  case⊎ (rename ρ L) (rename (ext ρ) M) (rename (ext ρ) N)
  rename ρ (case× L M)    =  case× (rename ρ L) (rename (ext (ext ρ)) M)
  rename ρ (case⊥ A)      =  case⊥ (rename ρ A)
  rename ρ (case⊤ L M)    =  case⊤ (rename ρ L) (rename ρ M) 
  rename ρ (caseL L M N)  =  caseL (rename ρ L) (rename ρ M) (rename (ext (ext ρ)) N)
  rename ρ `[]            =  `[]
  rename ρ `tt            =  `tt
  rename ρ (A `∷ B)       =  (rename ρ A) `∷ (rename ρ B)
  
\end{code}

## Simultaneous Substitution

\begin{code}
  exts : ∀ {Γ Δ} → (∀ {A} → Γ ∋ A → Δ ⊢ A) → (∀ {A B} → Γ , A ∋ B → Δ , A ⊢ B)
  exts σ Z      =  ` Z
  exts σ (S x)  =  rename S_ (σ x)

  subst : ∀ {Γ Δ} → (∀ {C} → Γ ∋ C → Δ ⊢ C) → (∀ {C} → Γ ⊢ C → Δ ⊢ C)
  subst σ (` k)          =  σ k
  subst σ (ƛ N)          =  ƛ (subst (exts σ) N)
  subst σ (L · M)        =  (subst σ L) · (subst σ M)
  subst σ (`zero)        =  `zero
  subst σ (`suc M)       =  `suc (subst σ M)
  subst σ (case L M N)   =  case (subst σ L) (subst σ M) (subst (exts σ) N)
  subst σ (μ N)          =  μ (subst (exts σ) N)
  subst σ (con n)        =  con n
  subst σ (M `* N)       =  subst σ M `* subst σ N
  subst σ (`let M N)     =  `let (subst σ M) (subst (exts σ) N)
  subst σ `⟨ M , N ⟩     =  `⟨ subst σ M , subst σ N ⟩
  subst σ (`proj₁ L)     =  `proj₁ (subst σ L)
  subst σ (`proj₂ L)     =  `proj₂ (subst σ L)
  subst σ (`inj₁ L)      =  `inj₁ (subst σ L)
  subst σ (`inj₂ L)      = `inj₂ (subst σ L)
  subst σ (case⊎ L M N)  =  case⊎ (subst σ L) (subst (exts σ) M) (subst (exts σ) N)
  subst σ (case× L M)    =  case× (subst σ L) (subst (exts (exts σ)) M)
  subst σ (case⊥ L)      =  case⊥ (subst σ L)
  subst σ (case⊤ L M)    =  case⊤ (subst σ L) (subst σ M)
  subst σ (`tt)          =  `tt
  subst σ `[]            =  `[]
  subst σ (x `∷ y)       =  (subst σ x) `∷ (subst σ y)
  subst σ (caseL L E C)  =  caseL (subst σ L) (subst σ E) (subst (exts (exts σ)) C)
\end{code}

## Single and double substitution

\begin{code}
  _[_] : ∀ {Γ A B}
    → Γ , A ⊢ B
    → Γ ⊢ A
    ------------
    → Γ ⊢ B
  _[_] {Γ} {A} N V =  subst {Γ , A} {Γ} σ N
    where
    σ : ∀ {B} → Γ , A ∋ B → Γ ⊢ B
    σ Z      =  V
    σ (S x)  =  ` x

  _[_][_] : ∀ {Γ A B C}
    → Γ , A , B ⊢ C
    → Γ ⊢ A
    → Γ ⊢ B
      ---------------
    → Γ ⊢ C
  _[_][_] {Γ} {A} {B} N V W =  subst {Γ , A , B} {Γ} σ N
    where
    σ : ∀ {C} → Γ , A , B ∋ C → Γ ⊢ C
    σ Z          =  W
    σ (S Z)      =  V
    σ (S (S x))  =  ` x
\end{code}

## Values

\begin{code}
  data Value : ∀ {Γ A} → Γ ⊢ A → Set where

    -- functions

    V-ƛ : ∀ {Γ A B} {N : Γ , A ⊢ B}
        ---------------------------
      → Value (ƛ N)

    -- naturals

    V-zero : ∀ {Γ} →
        -----------------
        Value (`zero {Γ})

    V-suc_ : ∀ {Γ} {V : Γ ⊢ `ℕ}
      → Value V
        --------------
      → Value (`suc V)

    -- primitives

    V-con : ∀ {Γ n}
        ---------------------
      → Value {Γ = Γ} (con n)

    -- products

    V-⟨_,_⟩ : ∀ {Γ A B} {V : Γ ⊢ A} {W : Γ ⊢ B}
      → Value V
      → Value W
        ----------------
      → Value `⟨ V , W ⟩

    V-inj₁ : ∀ {Γ A B} {V : Γ ⊢ A}
      → Value V
      -------------
      → Value (`inj₁ {Γ} {A} {B} V)

    V-inj₂ : ∀ {Γ A B} {V : Γ ⊢ B}
      → Value V
      ---------------
      → Value (`inj₂ {Γ} {A} {B} V)

    V-`[] : ∀ {Γ} {A}
      → Value (`[] {Γ} {A})

    V-`∷ : ∀ {Γ A} {V : Γ ⊢ A} {W : Γ ⊢ `List A}
      → Value V
      → Value W
      → Value (V `∷ W)

    V-`tt : ∀ {Γ}
      → Value ( `tt {Γ} )

\end{code}

Implicit arguments need to be supplied when they are
not fixed by the given arguments.

## Reduction

\begin{code}
  infix 2 _—→_

  data _—→_ : ∀ {Γ A} → (Γ ⊢ A) → (Γ ⊢ A) → Set where

    -- functions

    ξ-·₁ : ∀ {Γ A B} {L L′ : Γ ⊢ A ⇒ B} {M : Γ ⊢ A}
      → L —→ L′
        ---------------
      → L · M —→ L′ · M

    ξ-·₂ : ∀ {Γ A B} {V : Γ ⊢ A ⇒ B} {M M′ : Γ ⊢ A}
      → Value V
      → M —→ M′
        ---------------
      → V · M —→ V · M′

    β-ƛ : ∀ {Γ A B} {N : Γ , A ⊢ B} {V : Γ ⊢ A}
      → Value V
        --------------------
      → (ƛ N) · V —→ N [ V ]

    -- naturals

    ξ-suc : ∀ {Γ} {M M′ : Γ ⊢ `ℕ}
      → M —→ M′
        -----------------
      → `suc M —→ `suc M′

    ξ-case : ∀ {Γ A} {L L′ : Γ ⊢ `ℕ} {M : Γ ⊢ A} {N : Γ , `ℕ ⊢ A}
      → L —→ L′
        -------------------------
      → case L M N —→ case L′ M N

    β-zero :  ∀ {Γ A} {M : Γ ⊢ A} {N : Γ , `ℕ ⊢ A}
        -------------------
      → case `zero M N —→ M

    β-suc : ∀ {Γ A} {V : Γ ⊢ `ℕ} {M : Γ ⊢ A} {N : Γ , `ℕ ⊢ A}
      → Value V
        ----------------------------
      → case (`suc V) M N —→ N [ V ]

    -- fixpoint

    β-μ : ∀ {Γ A} {N : Γ , A ⊢ A}
        ----------------
      → μ N —→ N [ μ N ]

    -- primitive numbers

    ξ-*₁ : ∀ {Γ} {L L′ M : Γ ⊢ Nat}
      → L —→ L′
        -----------------
      → L `* M —→ L′ `* M

    ξ-*₂ : ∀ {Γ} {V M M′ : Γ ⊢ Nat}
      → Value V
      → M —→ M′
        -----------------
      → V `* M —→ V `* M′

    δ-* : ∀ {Γ c d}
        -------------------------------------
      → con {Γ = Γ} c `* con d —→ con (c * d)

    -- let

    ξ-let : ∀ {Γ A B} {M M′ : Γ ⊢ A} {N : Γ , A ⊢ B}
      → M —→ M′
        ---------------------
      → `let M N —→ `let M′ N

    β-let : ∀ {Γ A B} {V : Γ ⊢ A} {N : Γ , A ⊢ B}
      → Value V
        -------------------
      → `let V N —→ N [ V ]

    -- products

    ξ-⟨,⟩₁ : ∀ {Γ A B} {M M′ : Γ ⊢ A} {N : Γ ⊢ B}
      → M —→ M′
        -------------------------
      → `⟨ M , N ⟩ —→ `⟨ M′ , N ⟩

    ξ-⟨,⟩₂ : ∀ {Γ A B} {V : Γ ⊢ A} {N N′ : Γ ⊢ B}
      → Value V
      → N —→ N′
        -------------------------
      → `⟨ V , N ⟩ —→ `⟨ V , N′ ⟩

    ξ-proj₁ : ∀ {Γ A B} {L L′ : Γ ⊢ A `× B}
      → L —→ L′
        ---------------------
      → `proj₁ L —→ `proj₁ L′

    ξ-proj₂ : ∀ {Γ A B} {L L′ : Γ ⊢ A `× B}
      → L —→ L′
        ---------------------
      → `proj₂ L —→ `proj₂ L′

    β-proj₁ : ∀ {Γ A B} {V : Γ ⊢ A} {W : Γ ⊢ B}
      → Value V
      → Value W
        ----------------------
      → `proj₁ `⟨ V , W ⟩ —→ V

    β-proj₂ : ∀ {Γ A B} {V : Γ ⊢ A} {W : Γ ⊢ B}
      → Value V
      → Value W
        ----------------------
      → `proj₂ `⟨ V , W ⟩ —→ W

    -- alternative formulation of products

    ξ-inj₁ : ∀ {Γ A B} {L L` : Γ ⊢ A}
      → L —→ L`
      --------------
      → `inj₁ {Γ} {A} {B} L —→ `inj₁ {Γ} {A} {B} L`

    ξ-case⊥ : ∀ {Γ A} {L L' : Γ ⊢ `⊥}
      → L —→ L'
      -------------- 
      → case⊥ {Γ} {A} L —→ case⊥ {Γ} {A} L'

    ξ-inj₂ : ∀ {Γ A B} {L L` : Γ ⊢ B}
      → L —→ L`
      --------------
      → `inj₂ {Γ} {A} {B} L —→ `inj₂ {Γ} {A} {B} L`

    β-inj₁ : ∀ {Γ A B C} {V : Γ ⊢ A} {M : Γ , A ⊢ C} {N : Γ , B ⊢ C}
      → Value V
      ------------------
      → case⊎ (`inj₁ V) M N —→ M [ V ]

    β-inj₂ : ∀ {Γ A B C} {W : Γ ⊢ B} {M : Γ , A ⊢ C} {N : Γ , B ⊢ C}
      → Value W
      ------------------
      → case⊎ (`inj₂ W) M N —→ N [ W ]

    ξ-case⊎ : ∀ {Γ A B C} {L L' : Γ ⊢ A `⊎ B} {M : Γ , A ⊢ C} {N : Γ , B ⊢ C} 
      → L —→ L'
      -------------
      → case⊎ L M N —→ case⊎ L' M N

    ξ-case× : ∀ {Γ A B C} {L L′ : Γ ⊢ A `× B} {M : Γ , A , B ⊢ C}
      → L —→ L′
        -----------------------
      → case× L M —→ case× L′ M

    β-case× : ∀ {Γ A B C} {V : Γ ⊢ A} {W : Γ ⊢ B} {M : Γ , A , B ⊢ C}
      → Value V
      → Value W
        ----------------------------------
      → case× `⟨ V , W ⟩ M —→ M [ V ][ W ]

    ξ-∷₁ : ∀ {Γ A N} {M : Γ ⊢ A} {M' : Γ ⊢ A}
      → M —→ M'
      -------------------
      → M `∷ N —→ M' `∷ N

    ξ-∷₂ : ∀ {Γ A} {V : Γ ⊢ A} {N : Γ ⊢ `List A} {N' : Γ ⊢ `List A}
      → N —→ N'
      -------------------
      → V `∷ N —→ V `∷ N'

    ξ-caseL : ∀ {Γ A B} {L : Γ ⊢ `List A} {L' : Γ ⊢ `List A} {E : Γ ⊢ B} {C : Γ , A , `List A ⊢ B}
      → L —→ L'
      ------------------
      → caseL L E C —→ caseL L' E C

    β-[] : ∀ {Γ A B} {E : Γ ⊢ B} {C : Γ , A , `List A ⊢ B}
      → caseL `[] E C —→ E

    β-∷ : ∀ {Γ A B} {V : Γ ⊢ A} {W : Γ ⊢ `List A} {E : Γ ⊢ B} {C : Γ , A , `List A ⊢ B}
      → caseL (V `∷ W) E C —→ C [ V ][ W ]

    ξ-case⊤ : ∀ {Γ A} {L : Γ ⊢ `⊤} {L' : Γ ⊢ `⊤} {M : Γ ⊢ A}
      → L —→ L'
      ---------------
      → case⊤ L M —→ case⊤ L' M

    β-case⊤ : ∀ {Γ A} {V : Γ ⊢ `⊤} {M : Γ ⊢ A}
      → Value V
      → case⊤ V M —→ M

\end{code}

## Reflexive and transitive closure

\begin{code}
  infix  2 _—↠_
  infix  1 begin_
  infixr 2 _—→⟨_⟩_
  infix  3 _∎

  data _—↠_ : ∀ {Γ A} → (Γ ⊢ A) → (Γ ⊢ A) → Set where

    _∎ : ∀ {Γ A} (M : Γ ⊢ A)
        --------
      → M —↠ M

    _—→⟨_⟩_ : ∀ {Γ A} (L : Γ ⊢ A) {M N : Γ ⊢ A}
      → L —→ M
      → M —↠ N
        ------
      → L —↠ N

  begin_ : ∀ {Γ} {A} {M N : Γ ⊢ A}
    → M —↠ N
      ------
    → M —↠ N
  begin M—↠N = M—↠N
\end{code}


## Values do not reduce

\begin{code}
  V¬—→ : ∀ {Γ A} {M N : Γ ⊢ A}
    → Value M
      ----------
    → ¬ (M —→ N)
  V¬—→ V-ƛ          ()
  V¬—→ V-zero       ()
  V¬—→ (V-suc VM)   (ξ-suc M—→M′)     =  V¬—→ VM M—→M′
  V¬—→ V-con        ()
  V¬—→ (V-inj₁ VM)  (ξ-inj₁ M—→M')    =  V¬—→ VM M—→M'
  V¬—→ (V-inj₂ VM)  (ξ-inj₂ M—→M')    =  V¬—→ VM M—→M'  
  V¬—→ V-⟨ VM , _ ⟩ (ξ-⟨,⟩₁ M—→M′)    =  V¬—→ VM M—→M′
  V¬—→ V-⟨ _ , VN ⟩ (ξ-⟨,⟩₂ _ N—→N′)  =  V¬—→ VN N—→N′
  V¬—→ V-`[] ()
  V¬—→ (V-`∷ V W)   (ξ-∷₁ M—→M')      =  V¬—→ V M—→M'
  V¬—→ (V-`∷ V W)   (ξ-∷₂ N—→N')      =  V¬—→ W N—→N'
  V¬—→ (V-`tt)      ()
\end{code}


## Progress

\begin{code}
  data Progress {A} (M : ∅ ⊢ A) : Set where

    step : ∀ {N : ∅ ⊢ A}
      → M —→ N
        ----------
      → Progress M

    done :
        Value M
        ----------
      → Progress M

  progress : ∀ {A}
    → (M : ∅ ⊢ A)
      -----------
    → Progress M
  progress (` ())
  progress (ƛ N)                              =  done V-ƛ
  progress (L · M) with progress L
  ...    | step L—→L′                         =  step (ξ-·₁ L—→L′)
  ...    | done V-ƛ with progress M
  ...        | step M—→M′                     =  step (ξ-·₂ V-ƛ M—→M′)
  ...        | done VM                        =  step (β-ƛ VM)
  progress (`zero)                            =  done V-zero
  progress (`suc M) with progress M
  ...    | step M—→M′                         =  step (ξ-suc M—→M′)
  ...    | done VM                            =  done (V-suc VM)
  progress (case L M N) with progress L
  ...    | step L—→L′                         =  step (ξ-case L—→L′)
  ...    | done V-zero                        =  step β-zero
  ...    | done (V-suc VL)                    =  step (β-suc VL)
  progress (μ N)                              =  step β-μ
  progress (con n)                            =  done V-con
  progress (L `* M) with progress L
  ...    | step L—→L′                         =  step (ξ-*₁ L—→L′)
  ...    | done V-con with progress M
  ...        | step M—→M′                     =  step (ξ-*₂ V-con M—→M′)
  ...        | done V-con                     =  step δ-*
  progress (`let M N) with progress M
  ...    | step M—→M′                         =  step (ξ-let M—→M′)
  ...    | done VM                            =  step (β-let VM)
  progress `⟨ M , N ⟩ with progress M
  ...    | step M—→M′                         =  step (ξ-⟨,⟩₁ M—→M′)
  ...    | done VM with progress N
  ...        | step N—→N′                     =  step (ξ-⟨,⟩₂ VM N—→N′)
  ...        | done VN                        =  done (V-⟨ VM , VN ⟩)
  progress (`proj₁ L) with progress L
  ...    | step L—→L′                         =  step (ξ-proj₁ L—→L′)
  ...    | done (V-⟨ VM , VN ⟩)               =  step (β-proj₁ VM VN)
  progress (`proj₂ L) with progress L
  ...    | step L—→L′                         =  step (ξ-proj₂ L—→L′)
  ...    | done (V-⟨ VM , VN ⟩)               =  step (β-proj₂ VM VN)
  progress (`inj₁ L) with progress L
  ...    | step L—→L'                         =  step (ξ-inj₁ L—→L')
  ...    | done VM                            =  done (V-inj₁ VM)
  progress (`inj₂ L) with progress L
  ...    | step L—→L'                         =  step (ξ-inj₂ L—→L')
  ...    | done VN                            =  done (V-inj₂ VN)
  progress (case⊎ L M N) with progress L
  ...    | step L—→L'                         =  step (ξ-case⊎ L—→L')
  ...    | done (V-inj₁ VL)                   =  step (β-inj₁ VL)
  ...    | done (V-inj₂ VL)                   =  step (β-inj₂ VL)
  progress (case× L M) with progress L
  ...    | step L—→L′                         =  step (ξ-case× L—→L′)
  ...    | done (V-⟨ VM , VN ⟩)               =  step (β-case× VM VN)
  progress (case⊥ L) with progress L
  ...    | step L—→L'                         =  step (ξ-case⊥ L—→L')
  ...    | done ()
  progress (`[])                              =  done (V-`[])
  progress (M `∷ N) with progress M
  ...    | step M—→M'                         =  step (ξ-∷₁ M—→M')
  ...    | done VM with progress N
  ...        | step N—→N'                     =  step (ξ-∷₂ N—→N')
  ...        | done VN                        =  done (V-`∷ VM VN)
  progress (caseL L E C) with progress L
  ...    | step L—→L'                         =  step (ξ-caseL L—→L')
  ...    | done (V-`[])                       =  step (β-[])
  ...    | done (V-`∷ VM VN)                  =  step (β-∷)
  progress (case⊤ L M) with progress L
  ...    | step L—→L'                         =  step (ξ-case⊤ L—→L')
  ...    | done VL                            =  step (β-case⊤ VL)
  progress (`tt)                              =  done (V-`tt)
\end{code}


## Evaluation

\begin{code}
  data Gas : Set where
    gas : ℕ → Gas

  data Finished {Γ A} (N : Γ ⊢ A) : Set where

     done :
         Value N
         ----------
       → Finished N

     out-of-gas :
         ----------
         Finished N

  data Steps : ∀ {A} → ∅ ⊢ A → Set where

    steps : ∀ {A} {L N : ∅ ⊢ A}
      → L —↠ N
      → Finished N
        ----------
      → Steps L

  eval : ∀ {A}
    → Gas
    → (L : ∅ ⊢ A)
      -----------
    → Steps L
  eval (gas zero)    L                     =  steps (L ∎) out-of-gas
  eval (gas (suc m)) L with progress L
  ... | done VL                            =  steps (L ∎) (done VL)
  ... | step {M} L—→M with eval (gas m) M
  ...    | steps M—↠N fin                  =  steps (L —→⟨ L—→M ⟩ M—↠N) fin
\end{code}

## Examples

\begin{code}
  cube : ∅ ⊢ Nat ⇒ Nat
  cube = ƛ (# 0 `* # 0 `* # 0)

  _ : cube · con 2 —↠ con 8
  _ = 
    begin
      cube · con 2
    —→⟨ β-ƛ V-con ⟩
      con 2 `* con 2 `* con 2
    —→⟨ ξ-*₁ δ-* ⟩
      con 4 `* con 2
    —→⟨ δ-* ⟩
      con 8
    ∎

  exp10 : ∅ ⊢ Nat ⇒ Nat
  exp10 = ƛ (`let (# 0 `* # 0)
              (`let (# 0 `* # 0)
                (`let (# 0 `* # 2)
                  (# 0 `* # 0))))

  _ : exp10 · con 2 —↠ con 1024
  _ =
    begin
      exp10 · con 2
    —→⟨ β-ƛ V-con ⟩
      `let (con 2 `* con 2) (`let (# 0 `* # 0) (`let (# 0 `* con 2) (# 0 `* # 0)))
    —→⟨ ξ-let δ-* ⟩
      `let (con 4) (`let (# 0 `* # 0) (`let (# 0 `* con 2) (# 0 `* # 0)))
    —→⟨ β-let V-con ⟩
      `let (con 4 `* con 4) (`let (# 0 `* con 2) (# 0 `* # 0))
    —→⟨ ξ-let δ-* ⟩
      `let (con 16) (`let (# 0 `* con 2) (# 0 `* # 0))
    —→⟨ β-let V-con ⟩
      `let (con 16 `* con 2) (# 0 `* # 0)
    —→⟨ ξ-let δ-* ⟩
      `let (con 32) (# 0 `* # 0)
    —→⟨ β-let V-con ⟩
      con 32 `* con 32
    —→⟨ δ-* ⟩
      con 1024
    ∎

  swap× : ∀ {A B} → ∅ ⊢ A `× B ⇒ B `× A
  swap× = ƛ `⟨ `proj₂ (# 0) , `proj₁ (# 0) ⟩

  _ : swap× · `⟨ con 42 , `zero ⟩ —↠ `⟨ `zero , con 42 ⟩
  _ =
    begin
      swap× · `⟨ con 42 , `zero ⟩
    —→⟨ β-ƛ V-⟨ V-con , V-zero ⟩ ⟩
      `⟨ `proj₂ `⟨ con 42 , `zero ⟩ , `proj₁ `⟨ con 42 , `zero ⟩ ⟩
    —→⟨ ξ-⟨,⟩₁ (β-proj₂ V-con V-zero) ⟩
      `⟨ `zero , `proj₁ `⟨ con 42 , `zero ⟩ ⟩
    —→⟨ ξ-⟨,⟩₂ V-zero (β-proj₁ V-con V-zero) ⟩
      `⟨ `zero , con 42 ⟩
    ∎

  swap×-case : ∀ {A B} → ∅ ⊢ A `× B ⇒ B `× A
  swap×-case = ƛ case× (# 0) `⟨ # 0 , # 1 ⟩

  _ : swap×-case · `⟨ con 42 , `zero ⟩ —↠ `⟨ `zero , con 42 ⟩
  _ =
    begin
       swap×-case · `⟨ con 42 , `zero ⟩
     —→⟨ β-ƛ V-⟨ V-con , V-zero ⟩ ⟩
       case× `⟨ con 42 , `zero ⟩ `⟨ # 0 , # 1 ⟩
     —→⟨ β-case× V-con V-zero ⟩
       `⟨ `zero , con 42 ⟩
     ∎

  swap-⊎ : ∀ {A B} → ∅ ⊢ A `⊎ B ⇒ B `⊎ A
  swap-⊎ = ƛ case⊎ (# 0) (`inj₂ (# 0)) (`inj₁ (# 0))
  --_ : swap-⊎ · (inj₁ 42) ?
\end{code}


#### Exercise `More` (recommended in part)

Formalise the remaining constructs defined in this chapter.
Evaluate each example, applied to data as needed,
to confirm it returns the expected answer.

  * sums (recommended)
  * unit type
  * an alternative formulation of unit type
  * empty type (recommended)
  * lists

The formalisation of sums:

\begin{code}



\end{code}

## Bisimulation

(No recommended exercises for this chapter.)

#### Exercise `sim⁻¹`

Show that we also have a simulation in the other direction, and hence that we have
a bisimulation.

#### Exercise `products`

Show that the two formulations of products in
Chapter [More][plfa.More]
are in bisimulation.  The only constructs you need to include are
variables, and those connected to functions and products.
In this case, the simulation is _not_ lock-step.



\begin{code}
module Inference where
\end{code}

Remember to indent all code by two spaces.

### Imports

\begin{code}
  import plfa.More as DB
\end{code}

### Syntax

\begin{code}
  infix   4  _∋_⦂_
  infix   4  _⊢_↑_
  infix   4  _⊢_↓_
  infixl  5  _,_⦂_

  infixr  7  _⇒_

  infix   5  ƛ_⇒_
  infix   5  μ_⇒_
  infix   6  _↑
  infix   6  _↓_
  infixl  7  _·_
  infix   8  `suc_
  infix   9  `_
\end{code}

### Identifiers, types, and contexts

\begin{code}
  Id : Set
  Id = String

  data Type : Set where
    `ℕ    : Type
    _⇒_   : Type → Type → Type
    _`×_  : Type → Type → Type

  data Context : Set where
    ∅     : Context
    _,_⦂_ : Context → Id → Type → Context
\end{code}

### Terms

\begin{code}
  data Term⁺ : Set
  data Term⁻ : Set

  data Term⁺ where
    `_                        : Id → Term⁺
    _·_                       : Term⁺ → Term⁻ → Term⁺
    _↓_                       : Term⁻ → Type → Term⁺
    `proj₁_                   : Term⁺ → Term⁺
    `proj₂_                   : Term⁺ → Term⁺  

  data Term⁻ where
    ƛ_⇒_                     : Id → Term⁻ → Term⁻
    `zero                    : Term⁻
    `suc_                    : Term⁻ → Term⁻
    `case_[zero⇒_|suc_⇒_]    : Term⁺ → Term⁻ → Id → Term⁻ → Term⁻
    μ_⇒_                     : Id → Term⁻ → Term⁻
    _↑                       : Term⁺ → Term⁻
    ⟨_,_⟩                    : Term⁻ → Term⁻ → Term⁻
\end{code}

### Sample terms

\begin{code}
  two : Term⁻
  two = `suc (`suc `zero)

  plus : Term⁺
  plus = (μ "p" ⇒ ƛ "m" ⇒ ƛ "n" ⇒
            `case (` "m") [zero⇒ ` "n" ↑
                          |suc "m" ⇒ `suc (` "p" · (` "m" ↑) · (` "n" ↑) ↑) ])
              ↓ `ℕ ⇒ `ℕ ⇒ `ℕ

  2+2 : Term⁺
  2+2 = plus · two · two
\end{code}

### Lookup 

\begin{code}
  data _∋_⦂_ : Context → Id → Type → Set where

    Z : ∀ {Γ x A}
        --------------------
      → Γ , x ⦂ A ∋ x ⦂ A

    S : ∀ {Γ x y A B}
      → x ≢ y
      → Γ ∋ x ⦂ A
        -----------------
      → Γ , y ⦂ B ∋ x ⦂ A
\end{code}

### Bidirectional type checking

\begin{code}
  data _⊢_↑_ : Context → Term⁺ → Type → Set
  data _⊢_↓_ : Context → Term⁻ → Type → Set

  data _⊢_↑_ where

    ⊢` : ∀ {Γ A x}
      → Γ ∋ x ⦂ A
        -----------
      → Γ ⊢ ` x ↑ A

    _·_ : ∀ {Γ L M A B}
      → Γ ⊢ L ↑ A ⇒ B
      → Γ ⊢ M ↓ A
        -------------
      → Γ ⊢ L · M ↑ B

    ⊢↓ : ∀ {Γ M A}
      → Γ ⊢ M ↓ A
        ---------------
      → Γ ⊢ (M ↓ A) ↑ A

    ⊢proj₁ : ∀ {Γ M A B}
      → Γ ⊢ M ↑ (A `× B)
      ---------
      → Γ ⊢ `proj₁ M ↑ A

    ⊢proj₂ : ∀ {Γ M A B}
      → Γ ⊢ M ↑ (A `× B)
      ---------------
      → Γ ⊢ `proj₂ M ↑ B

  data _⊢_↓_ where

    ⊢ƛ : ∀ {Γ x N A B}
      → Γ , x ⦂ A ⊢ N ↓ B
        -------------------
      → Γ ⊢ ƛ x ⇒ N ↓ A ⇒ B

    ⊢zero : ∀ {Γ}
        --------------
      → Γ ⊢ `zero ↓ `ℕ

    ⊢suc : ∀ {Γ M}
      → Γ ⊢ M ↓ `ℕ
        ---------------
      → Γ ⊢ `suc M ↓ `ℕ

    ⊢case : ∀ {Γ L M x N A}
      → Γ ⊢ L ↑ `ℕ
      → Γ ⊢ M ↓ A
      → Γ , x ⦂ `ℕ ⊢ N ↓ A
        -------------------------------------
      → Γ ⊢ `case L [zero⇒ M |suc x ⇒ N ] ↓ A

    ⊢μ : ∀ {Γ x N A}
      → Γ , x ⦂ A ⊢ N ↓ A
        -----------------
      → Γ ⊢ μ x ⇒ N ↓ A

    ⊢↑ : ∀ {Γ M A B}
      → Γ ⊢ M ↑ A
      → A ≡ B
        -------------
      → Γ ⊢ (M ↑) ↓ B

    ⊢⟨_,_⟩ : ∀ {Γ M N A B}
      → Γ ⊢ M ↓ A
      → Γ ⊢ N ↓ B
      ------------
      → Γ ⊢ ⟨ M , N ⟩ ↓ (A `× B) 

\end{code}


### Type equality

\begin{code}
  _≟Tp_ : (A B : Type) → Dec (A ≡ B)
  `ℕ        ≟Tp `ℕ              =  yes refl
  `ℕ        ≟Tp (A ⇒ B)         =  no λ()
  (A ⇒ B)   ≟Tp `ℕ              =  no λ()
  (A `× B)  ≟Tp `ℕ              =  no λ()
  `ℕ        ≟Tp (B `× A)        =  no λ()
  (A₁ ⇒ B₁) ≟Tp (A `× B)        =  no λ()
  (A `× A₁) ≟Tp (B ⇒ B₁)        =  no λ()
  (A `× A₁) ≟Tp (B `× B₁) with A ≟Tp B | A₁ ≟Tp B₁
  ...  | no A≢    | _         =  no λ{refl → A≢ refl} 
  ...  | yes _    | no B≢     =  no λ{refl → B≢ refl}
  ...  | yes refl | yes refl  =  yes refl
  (A ⇒ B)   ≟Tp (A′ ⇒ B′)       
    with A ≟Tp A′ | B ≟Tp B′
  ...  | no A≢    | _         =  no λ{refl → A≢ refl}
  ...  | yes _    | no B≢     =  no λ{refl → B≢ refl}
  ...  | yes refl | yes refl  =  yes refl
\end{code}

### Prerequisites

\begin{code}
  dom≡ : ∀ {A A′ B B′} → A ⇒ B ≡ A′ ⇒ B′ → A ≡ A′
  dom≡ refl = refl

  rng≡ : ∀ {A A′ B B′} → A ⇒ B ≡ A′ ⇒ B′ → B ≡ B′
  rng≡ refl = refl

  prj₁≡ : ∀ {A B A' B'} → A `× B ≡ A' `× B' → A ≡ A'
  prj₁≡ refl = refl

  prj₂≡ : ∀ {A B A' B'} → A `× B ≡ A' `× B' → B ≡ B'
  prj₂≡ refl = refl

  ℕ≢⇒ : ∀ {A B} → `ℕ ≢ A ⇒ B
  ℕ≢⇒ ()

  ×≢⇒ : ∀ {A B C D} → A `× B ≢ C ⇒ D
  ×≢⇒ ()

  ⊢p≢⇒ : ∀ {A B} → A ≢ A ⇒ B
  ⊢p≢⇒ ()

  ℕ≢× : ∀ {A B} → `ℕ ≢ A `× B
  ℕ≢× ()
\end{code}


### Unique lookup

\begin{code}
  uniq-∋ : ∀ {Γ x A B} → Γ ∋ x ⦂ A → Γ ∋ x ⦂ B → A ≡ B
  uniq-∋ Z Z                 =  refl
  uniq-∋ Z (S x≢y _)         =  ⊥-elim (x≢y refl)
  uniq-∋ (S x≢y _) Z         =  ⊥-elim (x≢y refl)
  uniq-∋ (S _ ∋x) (S _ ∋x′)  =  uniq-∋ ∋x ∋x′
\end{code}

### Unique synthesis

\begin{code}
  uniq-↑ : ∀ {Γ M A B} → Γ ⊢ M ↑ A → Γ ⊢ M ↑ B → A ≡ B
  uniq-↑ (⊢` ∋x) (⊢` ∋x′)         =  uniq-∋ ∋x ∋x′
  uniq-↑ (⊢L · ⊢M) (⊢L′ · ⊢M′)    =  rng≡ (uniq-↑ ⊢L ⊢L′)
  uniq-↑ (⊢↓ ⊢M) (⊢↓ ⊢M′)         =  refl
  uniq-↑ (⊢proj₁ ⊢M') (⊢proj₁ ⊢M) =  prj₁≡ (uniq-↑ ⊢M' ⊢M)
  uniq-↑ (⊢proj₂ ⊢M') (⊢proj₂ ⊢M) =  prj₂≡ (uniq-↑ ⊢M' ⊢M)
  
\end{code}

## Lookup type of a variable in the context

\begin{code}
  ext∋ : ∀ {Γ B x y}
    → x ≢ y
    → ¬ ∃[ A ]( Γ ∋ x ⦂ A )
      -----------------------------
    → ¬ ∃[ A ]( Γ , y ⦂ B ∋ x ⦂ A )
  ext∋ x≢y _  ⟨ A , Z ⟩       =  x≢y refl
  ext∋ _   ¬∃ ⟨ A , S _ ⊢x ⟩  =  ¬∃ ⟨ A , ⊢x ⟩

  lookup : ∀ (Γ : Context) (x : Id)
      -----------------------
    → Dec (∃[ A ](Γ ∋ x ⦂ A))
  lookup ∅ x                        =  no  (λ ())
  lookup (Γ , y ⦂ B) x with x ≟ y
  ... | yes refl                    =  yes ⟨ B , Z ⟩
  ... | no x≢y with lookup Γ x
  ...             | no  ¬∃          =  no  (ext∋ x≢y ¬∃)
  ...             | yes ⟨ A , ⊢x ⟩  =  yes ⟨ A , S x≢y ⊢x ⟩
\end{code}

### Promoting negations

\begin{code}
  ¬arg : ∀ {Γ A B L M}
    → Γ ⊢ L ↑ A ⇒ B
    → ¬ Γ ⊢ M ↓ A
      -------------------------
    → ¬ ∃[ B′ ](Γ ⊢ L · M ↑ B′)
  ¬arg ⊢L ¬⊢M ⟨ B′ , ⊢L′ · ⊢M′ ⟩ rewrite dom≡ (uniq-↑ ⊢L ⊢L′) = ¬⊢M ⊢M′

  ¬switch : ∀ {Γ M A B}
    → Γ ⊢ M ↑ A
    → A ≢ B
      ---------------
    → ¬ Γ ⊢ (M ↑) ↓ B
  ¬switch ⊢M A≢B (⊢↑ ⊢M′ A′≡B) rewrite uniq-↑ ⊢M ⊢M′ = A≢B A′≡B
\end{code}


## Synthesize and inherit types

\begin{code}
  synthesize : ∀ (Γ : Context) (M : Term⁺)
      -----------------------
    → Dec (∃[ A ](Γ ⊢ M ↑ A))

  inherit : ∀ (Γ : Context) (M : Term⁻) (A : Type)
      ---------------
    → Dec (Γ ⊢ M ↓ A)

  synthesize Γ (` x) with lookup Γ x
  ... | no  ¬∃              =  no  (λ{ ⟨ A , ⊢` ∋x ⟩ → ¬∃ ⟨ A , ∋x ⟩ })
  ... | yes ⟨ A , ∋x ⟩      =  yes ⟨ A , ⊢` ∋x ⟩
  synthesize Γ (L · M) with synthesize Γ L
  ... | no  ¬∃              =  no  (λ{ ⟨ _ , ⊢L  · _  ⟩  →  ¬∃ ⟨ _ , ⊢L ⟩ })
  ... | yes ⟨ `ℕ ,    ⊢L ⟩  =  no  (λ{ ⟨ _ , ⊢L′ · _  ⟩  →  ℕ≢⇒ (uniq-↑ ⊢L ⊢L′) })
  ... | yes ⟨ a `× b , ⊢L ⟩ =  no  (λ{ ⟨ fst , ⊢L' · snd ⟩ → ×≢⇒ (uniq-↑ ⊢L ⊢L')})
  ... | yes ⟨ A ⇒ B , ⊢L ⟩ with inherit Γ M A
  ...    | no  ¬⊢M          =  no  (¬arg ⊢L ¬⊢M)
  ...    | yes ⊢M           =  yes ⟨ B , ⊢L · ⊢M ⟩
  synthesize Γ (M ↓ A) with inherit Γ M A
  ... | no  ¬⊢M             =  no  (λ{ ⟨ _ , ⊢↓ ⊢M ⟩  →  ¬⊢M ⊢M })
  ... | yes ⊢M              =  yes ⟨ A , ⊢↓ ⊢M ⟩
  synthesize Γ (`proj₁ M) with synthesize Γ M
  ... | yes ⟨ a ⇒ b , ⊢L ⟩   =  no (λ{ ⟨ a×b , ⊢proj₁ ⊢L' ⟩ → ×≢⇒ (uniq-↑ ⊢L' ⊢L)})
  ... | yes ⟨ `ℕ , ⊢L ⟩      =  no (λ{ ⟨ a×b , ⊢proj₁ ⊢L' ⟩ → ℕ≢× (uniq-↑ ⊢L ⊢L')})
  ... | yes ⟨ a `× b , ⊢L ⟩  =  yes ⟨ a , ⊢proj₁ ⊢L ⟩
  ... | no ¬E                =  no (λ{ ⟨ _ , ⊢proj₁ ⊢L ⟩ → ¬E ⟨ _ , ⊢L ⟩})
  synthesize Γ (`proj₂ M) with synthesize Γ M
  ... | yes ⟨ a ⇒ b , ⊢L ⟩   =  no (λ{ ⟨ a×b , ⊢proj₂ ⊢L' ⟩ → ×≢⇒ (uniq-↑ ⊢L' ⊢L)})
  ... | yes ⟨ `ℕ , ⊢L ⟩      =  no (λ{ ⟨ a×b , ⊢proj₂ ⊢L' ⟩ → ℕ≢× (uniq-↑ ⊢L ⊢L')})
  ... | yes ⟨ a `× b , ⊢L ⟩  =  yes ⟨ b , ⊢proj₂ ⊢L ⟩
  ... | no ¬E                =  no (λ{ ⟨ _ , ⊢proj₂ ⊢L ⟩ → ¬E ⟨ _ , ⊢L ⟩}) 

  inherit Γ (ƛ x ⇒ N) `ℕ      =  no  (λ())
  inherit Γ (ƛ x ⇒ N) (A ⇒ B) with inherit (Γ , x ⦂ A) N B
  ... | no ¬⊢N                =  no  (λ{ (⊢ƛ ⊢N)  →  ¬⊢N ⊢N })
  ... | yes ⊢N                =  yes (⊢ƛ ⊢N)
  inherit Γ `zero `ℕ          =  yes ⊢zero
  inherit Γ `zero (A ⇒ B)     =  no  (λ())
  inherit Γ (`suc M) `ℕ with inherit Γ M `ℕ
  ... | no ¬⊢M                =  no  (λ{ (⊢suc ⊢M)  →  ¬⊢M ⊢M })
  ... | yes ⊢M                =  yes (⊢suc ⊢M)
  inherit Γ (`suc M) (A ⇒ B)  =  no  (λ())
  inherit Γ (`case L [zero⇒ M |suc x ⇒ N ]) A with synthesize Γ L
  ... | no ¬∃                 =  no  (λ{ (⊢case ⊢L  _ _) → ¬∃ ⟨ `ℕ , ⊢L ⟩})
  ... | yes ⟨ _ ⇒ _ , ⊢L ⟩    =  no  (λ{ (⊢case ⊢L′ _ _) → ℕ≢⇒ (uniq-↑ ⊢L′ ⊢L) })
  ... | yes ⟨ a `× b , ⊢L ⟩   =  no  (λ{ (⊢case ⊢L' _ _) → ℕ≢× (uniq-↑ ⊢L' ⊢L) })
  ... | yes ⟨ `ℕ ,    ⊢L ⟩ with inherit Γ M A
  ...    | no ¬⊢M             =  no  (λ{ (⊢case _ ⊢M _) → ¬⊢M ⊢M })
  ...    | yes ⊢M with inherit (Γ , x ⦂ `ℕ) N A
  ...       | no ¬⊢N          =  no  (λ{ (⊢case _ _ ⊢N) → ¬⊢N ⊢N })
  ...       | yes ⊢N          =  yes (⊢case ⊢L ⊢M ⊢N)
  inherit Γ (μ x ⇒ N) A with inherit (Γ , x ⦂ A) N A
  ... | no ¬⊢N                =  no  (λ{ (⊢μ ⊢N) → ¬⊢N ⊢N })
  ... | yes ⊢N                =  yes (⊢μ ⊢N)
  inherit Γ (M ↑) B with synthesize Γ M
  ... | no  ¬∃                =  no  (λ{ (⊢↑ ⊢M _) → ¬∃ ⟨ _ , ⊢M ⟩ })
  ... | yes ⟨ A , ⊢M ⟩ with A ≟Tp B
  ...   | no  A≢B             =  no  (¬switch ⊢M A≢B)
  ...   | yes A≡B             =  yes (⊢↑ ⊢M A≡B)
  inherit Γ ⟨ M , N ⟩ (A `× B) with inherit Γ M A 
  ... | no ¬⊢N                 =  no (λ{ ⊢⟨ x , x₁ ⟩ → ¬⊢N x})
  ... | yes ⊢N with inherit Γ N B
  ...    | no ¬⊢M              =  no (λ{ ⊢⟨ x , x₁ ⟩ → ¬⊢M x₁})
  ...    | yes ⊢M              =  yes ⊢⟨ ⊢N , ⊢M ⟩
  inherit Γ (ƛ x ⇒ x₁) (b `× b₁) = no (λ ())
  inherit Γ `zero (b `× b₁) = no (λ ())
  inherit Γ (`suc x) (b `× b₁) = no (λ ())
  inherit Γ ⟨ x , x₁ ⟩ `ℕ = no (λ ())
  inherit Γ ⟨ x , x₁ ⟩ (b ⇒ b₁) = no (λ ())
\end{code}

### Erasure

\begin{code}
  ∥_∥Tp : Type → DB.Type
  ∥ `ℕ ∥Tp             =  DB.`ℕ
  ∥ A `× B ∥Tp         =  ∥ A ∥Tp DB.`× ∥ B ∥Tp 
  ∥ A ⇒ B ∥Tp          =  ∥ A ∥Tp DB.⇒ ∥ B ∥Tp

  ∥_∥Cx : Context → DB.Context
  ∥ ∅ ∥Cx              =  DB.∅
  ∥ Γ , x ⦂ A ∥Cx      =  ∥ Γ ∥Cx DB., ∥ A ∥Tp

  ∥_∥∋ : ∀ {Γ x A} → Γ ∋ x ⦂ A → ∥ Γ ∥Cx DB.∋ ∥ A ∥Tp
  ∥ Z ∥∋               =  DB.Z
  ∥ S x≢ ⊢x ∥∋         =  DB.S ∥ ⊢x ∥∋

  ∥_∥⁺ : ∀ {Γ M A} → Γ ⊢ M ↑ A → ∥ Γ ∥Cx DB.⊢ ∥ A ∥Tp
  ∥_∥⁻ : ∀ {Γ M A} → Γ ⊢ M ↓ A → ∥ Γ ∥Cx DB.⊢ ∥ A ∥Tp

  ∥ ⊢` ⊢x ∥⁺           =  DB.` ∥ ⊢x ∥∋
  ∥ ⊢L · ⊢M ∥⁺         =  ∥ ⊢L ∥⁺ DB.· ∥ ⊢M ∥⁻
  ∥ ⊢↓ ⊢M ∥⁺           =  ∥ ⊢M ∥⁻
  ∥ ⊢proj₁ ⊢M ∥⁺       =  DB.`proj₁ ∥ ⊢M ∥⁺
  ∥ ⊢proj₂ ⊢N ∥⁺       =  DB.`proj₂ ∥ ⊢N ∥⁺

  ∥ ⊢ƛ ⊢N ∥⁻           =  DB.ƛ ∥ ⊢N ∥⁻
  ∥ ⊢zero ∥⁻           =  DB.`zero
  ∥ ⊢suc ⊢M ∥⁻         =  DB.`suc ∥ ⊢M ∥⁻
  ∥ ⊢case ⊢L ⊢M ⊢N ∥⁻  =  DB.case ∥ ⊢L ∥⁺ ∥ ⊢M ∥⁻ ∥ ⊢N ∥⁻
  ∥ ⊢μ ⊢M ∥⁻           =  DB.μ ∥ ⊢M ∥⁻
  ∥ ⊢↑ ⊢M refl ∥⁻      =  ∥ ⊢M ∥⁺
  ∥ ⊢⟨ ⊢M , ⊢N ⟩ ∥⁻    =  DB.`⟨ ∥ ⊢M ∥⁻ , ∥ ⊢N ∥⁻ ⟩
\end{code}

### Sample terms

\begin{code}
  itwo : Term⁻
  itwo = `suc (`suc `zero)

  iplus : Term⁺
  iplus = (μ "p" ⇒ ƛ "m" ⇒ ƛ "n" ⇒
            `case (` "m") [zero⇒ ` "n" ↑
                          |suc "m" ⇒ `suc (` "p" · (` "m" ↑) · (` "n" ↑) ↑) ])
              ↓ `ℕ ⇒ `ℕ ⇒ `ℕ
\end{code}


#### Exercise `inference-multiplication` (recommended)

\begin{code}
  mul : Term⁺
  mul = (μ "*" ⇒ ƛ "m" ⇒ ƛ "n" ⇒
           `case (` "m") [zero⇒ `zero
                         |suc "m" ⇒ ((iplus · (` "n" ↑)) · (((    (` "*") · (` "m" ↑)    ) · (` "n" ↑)) ↑) ↑) ] ) 
            ↓ `ℕ ⇒ `ℕ ⇒ `ℕ

  
\end{code}

Rewrite your definition of multiplication from
Chapter [Lambda][plfa.Lambda] decorated to support inference, and show
that erasure of the inferred typing yields your definition of
multiplication from Chapter [DeBruijn][plfa.DeBruijn].

\begin{code}
  2*2 : Term⁺
  2*2 = mul · two · two

  --⊢2*2 : ∅ ⊢ 2*2 ↑ `ℕ
  --⊢2*2 = {!!}
\end{code}

-- projs + - +
-- ⟨ , ⟩ - - - - - 

#### Exercise `bidirectional-products` (recommended)

Extend the bidirectional type rules to include products from
Chapter [More][plfa.More].


#### Exercise `bidirectional-rest` (stretch)

Extend the bidirectional type rules to include the rest of the constructs from
Chapter [More][plfa.More].

 
#### Exercise `inference-products` (recommended)

Extend bidirectional inference to include products from
Chapter [More][plfa.More].


#### Exercise `inference-rest` (stretch)

Extend the bidirectional type rules to include the rest of the constructs from
Chapter [More][plfa.More].

## Untyped

#### Exercise (`Type≃⊤`)

Show that `Type` is isomorphic to `⊤`, the unit type.

#### Exercise (`Context≃ℕ`)

Show that `Context` is isomorphic to `ℕ`.

#### Exercise (`variant-1`)

How would the rules change if we want call-by-value where terms
normalise completely?  Assume that `β` should not permit reduction
unless both terms are in normal form.

#### Exercise (`variant-2`)

How would the rules change if we want call-by-value where terms
do not reduce underneath lambda?  Assume that `β`
permits reduction when both terms are values (that is, lambda
abstractions).  What would `2+2ᶜ` reduce to in this case?

#### Exercise `2+2≡four`

Use the evaluator to confirm that `2+2` and `four` normalise to
the same term.

#### Exercise `multiplication-untyped` (recommended)

Use the encodings above to translate your definition of
multiplication from previous chapters with the Scott
representation and the encoding of the fixpoint operator.
Confirm that two times two is four.

mul : ∀ {Γ} → Γ ⊢ ⋆
mul = μ "*" ⇒ ƛ "m" ⇒ ƛ "n" ⇒
        case ` "m"
          [zero⇒ `zero
          |suc "m" ⇒ (plus · ` "n" · (` "*" · ` "m" · ` "n")) ]


#### Exercise `encode-more` (stretch)

Along the lines above, encode all of the constructs of
Chapter [More][plfa.More],
save for primitive numbers, in the untyped lambda calculus.
