package com

typealias Vector = Array<Int>
typealias Matrix = Array<Vector>


private val alphaSmall = CharArray(26) { (it + 97).toChar() }
val DICTIONARY = alphaSmall + listOf('(', ')', '=')

fun String.toMatrix3columns(chunking: (CharSequence, Int) -> List<String> = CharSequence::chunked): Matrix {
    val columns: Int
    val parsableString = getParsableString(this)
    columns = parsableString.length / 3
    val stringChunks = chunking(parsableString, 3)
    return Matrix(columns) { getDictIndicesVector(stringChunks[it]) }
}

fun getParsableString(string: String): String {
    return if (string.length % 3 == 0) {
        string
    } else {
        if (string.length % 3 == 2) {
            string + DICTIONARY.last()
        } else {
            string + DICTIONARY.last() + DICTIONARY.last()
        }
    }
}

fun getDictIndicesVector(string: String): Vector {
    return Vector(string.length) {
        DICTIONARY.indexOf(string[it])
    }
}

fun matrixToString(matrix: Matrix): String {
    val msg = StringBuilder()
    matrix.forEach {
        it.forEach {
            msg.append(charFromDict(it))
        }
    }
    return msg.toString()
}

private fun charFromDict(c: Int): String {
    return DICTIONARY[c].toString()
}