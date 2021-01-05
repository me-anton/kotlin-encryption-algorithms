package com.lab7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.experimental.xor

class MainActivity : AppCompatActivity() {
    val polinomes = listOf(listOf(3,2,0), listOf(4,3,0), listOf(5,2,0))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputET = findViewById<EditText>(R.id.input)
        val key1ET = findViewById<EditText>(R.id.key1)
        val key2ET = findViewById<EditText>(R.id.key2)
        val key3ET = findViewById<EditText>(R.id.key3)
        val encryptBTN = findViewById<Button>(R.id.encryptBTN)
        val decryptBTN = findViewById<Button>(R.id.decryptBTN)
        val outputTV = findViewById<TextView>(R.id.output)
        val switchioBTN = findViewById<Button>(R.id.switchioBTN)

        fun buttonClick() {
            if (areNotEmpty(inputET, key1ET, key2ET, key3ET)) {
                val inputString = inputET.text.toString()
                val keyInputs = extractText(key1ET, key2ET, key3ET)
                if (keysAreValid(keyInputs)) {
                    val input = BitSet.valueOf(inputString.toByteArray())
                    val key = makeCombinedKey(inputString.length, keyInputs)
                    input.xor(key)
                    outputTV.text =  String(input.toByteArray())
                    switchioBTN.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Invalid keys",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Invalid input",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        encryptBTN.setOnClickListener{ buttonClick() }
        decryptBTN.setOnClickListener{ buttonClick() }

        val editableFactory = Editable.Factory.getInstance()
        switchioBTN.setOnClickListener {
            inputET.text = editableFactory.newEditable(outputTV.text)
        }
    }

    fun areNotEmpty(vararg inputs: EditText): Boolean {
        return inputs.all { it.text.isNotEmpty() }
    }

    fun extractText(vararg keyInputs: EditText): List<CharSequence> {
        return keyInputs.map { it.text }
    }

    fun keysAreValid(keyInputs: List<CharSequence>): Boolean {
        return keyInputs.all {
            it.all {
                it == '0' || it == '1'
            }
        }
    }

    fun makeCombinedKey(length: Int, keyInputs: List<CharSequence>): BitSet {
        val keys = keyInputs.mapIndexed { idx, key -> key.toString().toByteArray().makeFullKey(length, polinomes[idx]) }

        keys[2].mutateIndexed { idx, bit ->
            when (bit) {
                false   -> keys[0][idx]
                true    -> keys[1][idx]
            }
        }
        return keys[2]
    }

    fun BitSet.mutateIndexed(transform: (Int, Boolean) -> Boolean) {
        for (i in 0 until this.size()) {
            this[i] = transform(i, this[i])
        }
    }

    fun ByteArray.makeFullKey(length: Int, polinome: List<Int>): BitSet {
        val polinomeSlice = polinome.slice(IntRange(1, polinome.size - 1))
        val maxIndex = length - 1
        val fullKey = ArrayList<Byte>(length)
        fullKey.addAll(this.toList())

        do {
            val currentSize = fullKey.size
            fullKey.add( fullKey.getNextGammaBit(polinomeSlice, currentSize) )
        } while (currentSize < maxIndex)

        return BitSet.valueOf(fullKey.toByteArray())
    }

    fun List<Byte>.getNextGammaBit(polinomeSlice: List<Int>, thisSize: Int): Byte {
        var bit: Byte = 0

        polinomeSlice.forEach {
            bit = bit.xor(this[thisSize - it - 1])
        }
        return bit
    }
}
