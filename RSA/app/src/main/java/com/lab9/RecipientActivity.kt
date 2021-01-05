package com.lab9

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val MESSAGE_EXTRA = "message"

class RecipientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipient)

        val nTV             = findViewById<TextView>(R.id.nTV)
        val eTV             = findViewById<TextView>(R.id.eTV)
        val messageET       = findViewById<EditText>(R.id.messageET)
        val sendMessageBTN  = findViewById<Button>(R.id.sendMessageBTN)
        val infoFAB          = findViewById<FloatingActionButton>(R.id.infoFAB)
        val n               = intent.getIntExtra(N_EXTRA, -1)
        val e               = intent.getIntExtra(E_EXTRA, -1)

        nTV.assignExpression(n, "N")
        eTV.assignExpression(e, "e")

        sendMessageBTN.setOnClickListener {
            val encryptedMessage = encrypt(messageET.text.toString(), n, e)
            intent = Intent().apply {
                putExtra(MESSAGE_EXTRA, encryptedMessage)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        infoFAB.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }
    }

    private fun encrypt(message: String, n: Int, e: Int): Array<ByteArray> {
        val N = n.toBigInteger()
        val E = e.toBigInteger()
        return message.map {
            it.toInt().toBigInteger().modPow(E, N).toByteArray()
        }.toTypedArray()
    }
}
