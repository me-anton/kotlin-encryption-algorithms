package com.lab5and6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.*
import com.lab6.CipherBreakingActivity

typealias Vector = Array<Int>
typealias Matrix = Array<Vector>

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputET = findViewById<EditText>(R.id.input)
        val keyET = findViewById<EditText>(R.id.key)
        val encryptBTN = findViewById<Button>(R.id.encryptBTN)
        val decryptBTN = findViewById<Button>(R.id.decryptBTN)
        val cipherTextTV = findViewById<TextView>(R.id.cipherText)
        val activityBTN = findViewById<Button>(R.id.activityBTN)

        fun buttonClick(method: ((Matrix, Matrix) -> Matrix)) {
            val input = getStringInput(inputET)
            if (inputIsValid(input)) {
                val keyInput = getStringInput(keyET)
                if (keyInput.length == 9) {
                    val msg = input.toMatrix3columns()
                    val key = keyInput.toMatrix3columns()
                    try {
                        cipherTextTV.text = matrixToString(method(msg, key))
                    } catch (e: IllegalArgumentException) {
                        Toast.makeText(
                            applicationContext,
                            "Try another key",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Key must by 9 characters long",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Input can only contain latin letters in lower case and symbols =, ) and (",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        val encrypt = { msg: Matrix, key: Matrix ->
            msg.map { key * it }.toTypedArray()
        }
        val decrypt = { msg: Matrix, key: Matrix ->
            msg.map { key.inverseForDict() * it }.toTypedArray()
        }

        encryptBTN.setOnClickListener { buttonClick(encrypt) }
        decryptBTN.setOnClickListener { buttonClick(decrypt) }
        activityBTN.setOnClickListener {
            val intent = Intent(this, CipherBreakingActivity::class.java).apply {
                putExtra("plainText", getStringInput(inputET))
                putExtra("cipherText", cipherTextTV.text)
            }
            startActivity(intent)
        }
    }

    private fun getStringInput(input: EditText): String {
        return input.text.toString().trim()
    }

    private fun inputIsValid(input: String): Boolean {
        input.forEach { if (!DICTIONARY.contains(it)) return false }
        return true
    }

    private operator fun Matrix.times(vector: Vector): Vector {
        val result = Vector(vector.size) {0}
        for ((i, m) in this.withIndex()) {
            for ((j, v) in vector.withIndex()) {
                result[i] += m[j] * v
            }
            result[i] %= DICTIONARY.size
        }
        return result
    }

    private fun Matrix.inverseForDict(): Matrix {
        val inv = Matrix(3) { Vector(3) {0} }
        val det = this.det()
        if (det != 0) {
            val invDet = inverseModulo(det, DICTIONARY.size)
            if (invDet != -1) {
                for (i in 0 until 3) {
                    for (j in 0 until 3) {
                        inv[i][j] = floorMod(
                            (((this[(j + 1) % 3][(i + 1) % 3] * this[(j + 2) % 3][(i + 2) % 3]) - (this[(j + 1) % 3][(i + 2) % 3] * this[(j + 2) % 3][(i + 1) % 3])) * invDet),
                            DICTIONARY.size
                        )
                    }
                }
                return inv
            }
        }
        throw IllegalArgumentException("Bad key")
    }
}
