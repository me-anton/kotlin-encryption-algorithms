package com.lab9

import org.junit.Test

import org.junit.Assert.*
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun test() {
        println(BigDecimal.valueOf(17).divide(BigDecimal.valueOf(1200).pow(2)))
    }
}
