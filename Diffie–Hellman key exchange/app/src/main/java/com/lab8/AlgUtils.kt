package com.lab8

import android.widget.EditText
import java.math.BigInteger
import java.security.SecureRandom
import kotlin.math.sqrt
import kotlin.random.Random

private val random = SecureRandom()

public fun generatePG(bitLength: Int): Pair<Int, Int> {
    val p = randomPrime(bitLength)
    val g = generateG(p)
    if (g == -1) {
        //retry
        return generatePG(bitLength)
    }
    return Pair(p, g)
}

public fun generateLocalSecret(): Int {
    return Random.nextInt(5, 10)
}

public fun calculatePublicKey(g: Int, localSecret: Int, p: Int): Int {
    return g.toBigInteger().modPow(localSecret.toBigInteger(), p.toBigInteger()).toInt()
}

public fun calculateSecretKey(foreignPublicKey: Int, localSecret: Int, p: Int): Int {
    return foreignPublicKey.toBigInteger().modPow(localSecret.toBigInteger(), p.toBigInteger()).toInt()
}

private fun randomPrime(bitLength: Int): Int {
    return BigInteger.probablePrime(bitLength, random).toInt()
}

private fun generateG(p: Int): Int {
    return findPrimitiveRoot(p)
}

private fun findPrimitiveRoot(n: Int): Int {
    val phi = n - 1
    val s = findPrimeFactors(phi)

    for (r in 2..phi) {
        if (r.isPrimitiveRootOf(n, s, phi)) {
            return r
        }
    }

    return -1
}

fun Int.isPrimitiveRootOf(n: Int, primeFactors: HashSet<Int>? = null, phi: Int = n-1): Boolean {
    val s = primeFactors ?: findPrimeFactors(phi)
    var isRoot = true
    for (a in s) {
        if (this.toBigInteger()
                .modPow((phi / a).toBigInteger(), n.toBigInteger())
                == BigInteger.ONE) {
            isRoot = false
            break
        }
    }
    return isRoot
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

private fun findPrimeFactors(N: Int): HashSet<Int> {
    val s = HashSet<Int>()
    var n = N
    while (n % 2 == 0) {
        s.add(2)
        n /= 2
    }

    for (i in 3..sqrt(n.toDouble()).toInt() step 2) {
        while (n % i == 0)
        {
            s.add(i)
            n /= i
        }
    }
    if (n > 2) {
        s.add(n)
    }
    return s
}

fun EditText.getInt(): Int {
    return this.text.toString().toInt()
}