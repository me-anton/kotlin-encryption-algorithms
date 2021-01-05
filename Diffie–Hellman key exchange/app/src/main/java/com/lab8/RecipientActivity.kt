package com.lab8

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val RECIPIENTS_KEY_EXTRA = "recipient's public key"

class RecipientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipient)

        val yTV                     = findViewById<TextView>(R.id.yTV)
        val sendersPublicKeyTV      = findViewById<TextView>(R.id.sendersPublicKeyTV)
        val recipientsPublicKeyTV   = findViewById<TextView>(R.id.recipientsPublicKeyTV)
        val secretKeyTV             = findViewById<TextView>(R.id.secretKeyTV)
        val backBTN                 = findViewById<Button>(R.id.backBTN)
        val infoFAB                 = findViewById<FloatingActionButton>(R.id.infoFAB)
        val sendersPublicKey        = intent.getIntExtra(SENDERS_KEY_EXTRA, -1)
        val p                       = intent.getIntExtra(P_EXTRA, -1)
        val g                       = intent.getIntExtra(G_EXTRA, -1)
        val y: Int
        val recipientsPublicKey: Int
        val secretKey: Int

        y = generateLocalSecret()
        yTV.text = y.toString()
        sendersPublicKeyTV.text = sendersPublicKey.toString()
        recipientsPublicKey = calculatePublicKey(g, y, p)
        recipientsPublicKeyTV.text = recipientsPublicKey.toString()
        secretKey = calculateSecretKey(sendersPublicKey, y, p)
        secretKeyTV.text = secretKey.toString()

        backBTN.setOnClickListener {
            val intent = Intent().apply {
                putExtra(RECIPIENTS_KEY_EXTRA, recipientsPublicKey)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        infoFAB.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }
    }
}
