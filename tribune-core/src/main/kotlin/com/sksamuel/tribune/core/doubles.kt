package com.sksamuel.tribune.core

import arrow.core.leftNel
import arrow.core.right

/**
 * Extends a [Parser] of output type string to parse that string into a double.
 * If the string cannot be parsed into a double, then the error is generated by the
 * given function [ifError].
 *
 * Note: This parser accepts nullable inputs if the receiver accepts nullable inputs
 * and a null is considered a failing case.
 */
fun <I, E> Parser<I, String, E>.double(ifError: (String) -> E): Parser<I, Double, E> =
   flatMap {
      val d = it.toDoubleOrNull()
      d?.right() ?: ifError(it).leftNel()
   }

fun <I, E> Parser<I, Double, E>.positive(ifError: (Double) -> E): Parser<I, Double, E> =
   flatMap {
      if (it > 0.0) it.right() else ifError(it).leftNel()
   }

fun <I, E> Parser<I, Double, E>.negative(ifError: (Double) -> E): Parser<I, Double, E> =
   flatMap {
      if (it < 0.0) it.right() else ifError(it).leftNel()
   }

/**
 * Chains a [Parser] to convert String? -> non-negative Double.
 */
fun <I, E> Parser<I, Double, E>.nonNegative(ifError: (Double) -> E): Parser<I, Double, E> =
   this.filter({ it >= 0 }, ifError)

fun <I, E> Parser<I, Double, E>.inrange(
   range: ClosedFloatingPointRange<Double>,
   ifError: (Double) -> E,
): Parser<I, Double, E> =
   flatMap {
      if (it in range) it.right() else ifError(it).leftNel()
   }
