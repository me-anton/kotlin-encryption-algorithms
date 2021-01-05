package com

import kotlin.math.ceil

fun Matrix.det(): Int { //only 3x3
    var det = 0
    for(i in 0 until 3)
        det += this[0][i] * (this[1][(i+1)%3] * this[2][(i+2)%3] - this[1][(i+2)%3] * this[2][(i+1)%3])
    return det
}

fun inverseModulo(num: Int, mod: Int): Int {
    if (num == 0) return 0
    for (i in ceil(((mod - 1) / num).toDouble()).toInt() until mod) {
        if (floorMod(num * i, mod) == 1) return i
    }
    return -1
}

fun Matrix.inverseDet(mod: Int): Int {
    return inverseModulo(this.det(), mod)
}

fun floorMod(a: Int, m: Int): Int {
    val mod = a % m
    return if (mod >= 0)
        mod
    else
        mod + m
}