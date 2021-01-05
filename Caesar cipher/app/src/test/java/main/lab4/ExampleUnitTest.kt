package main.lab4

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val alphaBig = CharArray(26, {(it+65).toChar()})
    val alphaSmall = CharArray(26, {(it+97).toChar()})
    val alphabet = alphaBig + alphaSmall


    @Test
    fun test_sheet() {

    }

    @Test
    fun testAlphabet() {
        print(alphabet)
    }

}
