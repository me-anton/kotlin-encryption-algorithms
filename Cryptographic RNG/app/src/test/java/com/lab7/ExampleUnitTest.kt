package com.lab7

import org.junit.Test
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun test() {
        val bitSet = BitSet.valueOf("asdfghjkl;yz".toByteArray())
        val key = BitSet.valueOf("qwertyuiophz".toByteArray())
        bitSet.xor(key)
        println(String(bitSet.toByteArray() + 0, StandardCharsets.UTF_16BE))
        (bitSet.toByteArray()).print()

    }

    fun BitSet.print() {
        for (i in 0 until this.size()) {
            print("${this[i]} ")
        }
        println()
    }

    fun ByteArray.print() {
        this.forEach {
            print("$it ")
        }
        println()
    }
}