package com.lab8

import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun test() {

    }

    private fun findPrimeNumbers(n: Int): List<Int> {
        val primes = BooleanArray(n + 1)
        Arrays.fill(primes, true)
        //loop from 2 to x until x*x becomes greater than n
        run {
            var i = 2
            while (i * i < n) {
                if (primes[i]) {
                    var j = i * i
                    while (j <= n) {
                        primes[j] = false
                        j += i
                    }
                }
                i++
            }
        }
        val primeNumbers: MutableList<Int> = ArrayList()
        for (i in 2..n) {
            if (primes[i]) primeNumbers.add(i)
        }
        return primeNumbers
    }

    private fun isPrime(num: Int, primes: List<Int>): Boolean {
        for (i in primes) {
            if (num % i == 0) {
                return false
            }
        }
        return true
    }
}
