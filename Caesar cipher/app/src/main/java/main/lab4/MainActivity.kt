package main.lab4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val alphaBig = CharArray(26, {(it+65).toChar()})
    private val alphaSmall = CharArray(26, {(it+97).toChar()})
    private val alphabet = alphaBig + alphaSmall

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputET = findViewById<EditText>(R.id.input)
        val keyET = findViewById<EditText>(R.id.key)
        val encryptBTN = findViewById<Button>(R.id.encryptBTN)
        val decryptBTN = findViewById<Button>(R.id.decryptBTN)
        val cipherTextTV = findViewById<TextView>(R.id.cipherText)

        fun buttonClick(method: ((String, Int) -> String)) {
            val input = getStringInput(inputET)
            if (inputIsValid(input)) {
                val key = getStringInput(keyET).toInt()
                cipherTextTV.text = method(input, key)
            } else {
                Toast.makeText(applicationContext, "Input can contain only contain latin letters in upper or lower case", Toast.LENGTH_LONG).show()
            }
        }
        val encrypt = method@{ text:String, key:Int ->
            return@method text.map { shiftChar(it, key) }.toString()
        }
        val decrypt = method@{ text:String, key:Int ->
            return@method text.map { reverseShiftChar(it, key) }.toString()
        }
        encryptBTN.setOnClickListener{buttonClick(encrypt)}
        decryptBTN.setOnClickListener{buttonClick(decrypt)}
    }

    private fun getStringInput(input: EditText): String {
        return input.text.toString()
    }

    private fun shiftChar(c:Char, shiftBy:Int): Char {
        return alphabet[(alphabet.indexOf(c) + shiftBy) % alphabet.size]
    }

    private fun reverseShiftChar(c:Char, shiftBy:Int): Char {
        return alphabet[(alphabet.indexOf(c) - shiftBy) % alphabet.size]
    }

    private fun inputIsValid(input: String): Boolean {
        input.forEach { if (!alphabet.contains(it)) return false }
        return true
    }
}
