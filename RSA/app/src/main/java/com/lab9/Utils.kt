package com.lab9

import android.text.Editable
import android.widget.EditText
import android.widget.TextView
import java.math.BigInteger
import java.security.SecureRandom
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.random.nextInt

private val etFactory = Editable.Factory()
private val random = SecureRandom()

fun randomPrime(bitLengthRange: IntRange): Int {
    val bitLength = Random.nextInt(bitLengthRange)
    return BigInteger.probablePrime(bitLength, random).toInt()
}

fun EditText.assign(value: Int) {
    this.text = etFactory.newEditable(value.toString())
}

fun TextView.assignExpression(value: Int, valueName: String) {
    this.text = String.format("%s = %d", valueName, value)
}

fun findCoprime(n: Int): Int {
    for (i in 2..sqrt(n.toDouble()).toInt()) {
        if (i.isCoprime(n)) {
            return i
        }
    }
    return -1
}

fun Int.isCoprime(n: Int): Boolean {
    return gcd(this, n) == 1
}

fun lcm(a: Int, b: Int): Int {
    return a * b / gcd(a, b)
}

fun gcd(a: Int, b: Int): Int {
    if (b == 0) {
        return a
    }
    return if (a < b) {
        gcd(b, a)
    } else gcd(b, a % b)
}

fun Int.isPrime(): Boolean {
    if (this <= 1) {
        return false
    }
    if (this <= 3) {
        return true
    }
    if (this % 2 == 0 || this % 3 == 0) {
        return false
    }
    var i = 5
    while (i * i <= this) {
        if (this % i == 0 || this % (i + 2) == 0) {
            return false
        }
        i += 6
    }
    return true
}

fun EditText.getInt(): Int {
    return this.text.toString().toInt()
}