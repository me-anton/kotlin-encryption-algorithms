package com.lab5and6

import org.junit.Test





/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
typealias Vector = Array<Int>
typealias Matrix = Array<Vector>

class ExampleUnitTest {
    private val alphaSmall = CharArray(26) { (it+97).toChar() }
    private val dictionary = alphaSmall + listOf('(',')','=')

    val matrix = arrayOf(
        arrayOf(1, 2, 3),
        arrayOf(5, 3, 1),
        arrayOf(4, 1, 2)
    )
    val vector = arrayOf(1, 2, 3)

    @Test
    fun test() {


    }



    fun Vector.print() {
        this.forEach { print("$it ") }
        println()
    }

    fun Matrix.print() {
        for (r in this) {
            r.print()
        }
    }








    @Test
    fun testDecrypt() {
        val key = "ozonizer)".toMatrix3columns()
        val msg = "evq()x".toMatrix3columns()
        val invKey = key.inverseForDict()
        val decryptedMsg = msg.map { invKey * it }.toTypedArray()
        println(matrixtoString(decryptedMsg))
    }

    @Test
    fun testMatrixMultiplication() {
        val m1 = arrayOf(
            arrayOf(1, 2, 3),
            arrayOf(4, 5, 6),
            arrayOf(7, 8, 9)
        )
        val m2 = arrayOf(
            arrayOf(9, 8, 7),
            arrayOf(6, 5, 4),
            arrayOf(3, 2, 1)
        )

        m2.map { m1 * it }.toTypedArray().print()
    }

    /**************************/

    /** test matrix inversion */

    fun Matrix.inverseForDict(): Matrix {
        val inv = Matrix(3) { Vector(3) {0} }
        val det = this.det()
        if (det != 0) {
            val invDet = inverseModulo(det, dictionary.size)
            if (invDet != -1) {
                for (i in 0 until 3) {
                    for (j in 0 until 3) {
                        inv[i][j] = Math.floorMod(
                            (((this[(j + 1) % 3][(i + 1) % 3] * this[(j + 2) % 3][(i + 2) % 3]) - (this[(j + 1) % 3][(i + 2) % 3] * this[(j + 2) % 3][(i + 1) % 3])) * invDet),
                            dictionary.size
                        )
                    }
                }
                return inv
            }
        }
        throw IllegalArgumentException("Bad key")
    }

    fun Matrix.det(): Int { //only 3x3
        var det = 0
        for(i in 0 until 3)
            det += this[0][i] * (this[1][(i+1)%3] * this[2][(i+2)%3] - this[1][(i+2)%3] * this[2][(i+1)%3])
        return det
    }

    fun inverseModulo(num: Int, mod: Int): Int {
        for (i in Math.ceil(((mod - 1) / num).toDouble()).toInt() until mod) {
            if (Math.floorMod(num * i, mod) == 1) return i
        }
        return -1
    }

    /*****************/

    /** test encrypt */

    @Test
    fun testEncrypt() {
        val key = "ozonizer)".toMatrix3columns()
        val msg = "hello".toMatrix3columns()
        println(matrixtoString(testEncrypt(msg, key)))
    }

    fun testEncrypt(msg: Matrix, key: Matrix): Matrix {
        return msg.map { key * it  }.toTypedArray()
    }

    private fun String.toMatrix3columns(): Matrix {
        val columns: Int
        val parsableString =
            if (this.length % 3 == 0) {
                this
            } else {
                if (this.length % 3 == 1) {
                    this + dictionary.last()
                } else {
                    this + dictionary.last() + dictionary.last()
                }
            }
        columns = parsableString.length / 3
        val result = Matrix(columns) { Vector(3) {0} }
        val chars = parsableString.chunked(3)
        return result.mapIndexed { i, v ->
            v.mapIndexed { j, c ->
                dictionary.indexOf(chars[i][j])
            }.toTypedArray()
        }.toTypedArray()
    }

    private operator fun Matrix.times(vector: Vector): Vector {
        val result = Vector(vector.size) {0}
        for ((i, m) in this.withIndex()) {
            for ((j, v) in vector.withIndex()) {
                result[i] += m[j] * v
            }
            result[i] %= dictionary.size
        }
        return result
    }

    private fun matrixtoString(matrix: Matrix): String {
        val msg = StringBuilder()
        matrix.forEach {
            it.forEach {
                msg.append(charFromDict(it))
            }
        }
        return msg.toString()
    }

    private fun charFromDict(c: Int): String {
        return dictionary[c].toString()
    }
}
