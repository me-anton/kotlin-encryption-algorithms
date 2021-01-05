package com.lab6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.*
import com.lab5and6.R

typealias Vector = Array<Int>
typealias Matrix = Array<Vector>

class CipherBreakingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cipher_breaking)

        val plainTextET = findViewById<EditText>(R.id.plainTextET)
        val cipherTextET = findViewById<EditText>(R.id.cipherTextET)
        val hackBTN = findViewById<Button>(R.id.hackBTN)
        val keyTV = findViewById<TextView>(R.id.keyTV)

        var (plainText, cipherText) = getBundledStrings(intent)
        plainTextET.setText(plainText)
        cipherTextET.setText(cipherText)

        hackBTN.setOnClickListener {
            plainText = getStringInput(plainTextET)
            cipherText = getStringInput(cipherTextET)
            if (inputIsValid(plainText, cipherText)) {
                val key = getCipherKey(plainText, cipherText)
                keyTV.text = matrixToString(key)
            } else {
                Toast.makeText(applicationContext, "Data is too short or is entered incorrectly", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun inputIsValid(plainText: String, cipherText: String): Boolean {
        return (plainText.length > 6) && (cipherText.length >= plainText.length)
    }

    private fun getBundledStrings(i: Intent): Pair<String, String> {
        return Pair(i.getStringExtra("plainText"), i.getStringExtra("cipherText"))
    }

    private fun getStringInput(input: EditText): String {
        return input.text.toString().trim()
    }

    private fun getCipherKey(plainText: String, cipherText: String): Matrix {
        val cipherTextVectors = cipherText.substring(0, 9).toMatrix3columns(chunking=::chunkTransposed)
        val deltaMatrix = getParsableString(plainText).substring(0, 9).toMatrix3columns()
        val delta = deltaMatrix.inverseDet(DICTIONARY.size)
        return Matrix(3) {i ->
                   Vector(3) { j ->
                       val localDeltaMatrix = deltaMatrix.map { it.copyOf() }.toTypedArray()
                       for (deltaI in 0 until 3) {
                           localDeltaMatrix[deltaI][j] = cipherTextVectors[i][deltaI]
                       }
                       val localDelta = floorMod(localDeltaMatrix.det(), DICTIONARY.size)
                       return@Vector (localDelta * delta) % DICTIONARY.size
                   }
               }
    }

    private fun chunkTransposed(string: CharSequence, size: Int): List<String> {
        return List(string.length / size) { x ->
            String(CharArray(size) { y ->
                string[y * size + x]
            })
        }
    }
}