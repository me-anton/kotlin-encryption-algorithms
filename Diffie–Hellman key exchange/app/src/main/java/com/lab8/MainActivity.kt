package com.lab8

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.text.TextWatcher as TextWatcher

const val KEY_EXCHANGE = 1
const val SENDERS_KEY_EXTRA = "sender's public key"
const val P_EXTRA = "p"
const val G_EXTRA = "g"

class MainActivity : AppCompatActivity() {
    private var x = -1
    private var p = -1
    private lateinit var secretKeyTV: TextView
    private lateinit var secretKeyMemberTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        secretKeyTV             = findViewById(R.id.secretKeyTV)
        secretKeyMemberTV       = findViewById(R.id.secretKeyMemberTV)
        val xTV                 = findViewById<TextView>(R.id.xTV)
        val publicKeyTV         = findViewById<TextView>(R.id.senderPublicKeyTV)
        val pET                 = findViewById<EditText>(R.id.pET)
        val gET                 = findViewById<EditText>(R.id.gET)
        val pgRandomizeBTN      = findViewById<Button>(R.id.pgRandomizeBTN)
        val makePublicKeyBTN    = findViewById<Button>(R.id.makePublicKeyBTN)
        val sendPublicKeyBTN    = findViewById<Button>(R.id.sendPublicKeyBTN)
        val infoFAB             = findViewById<FloatingActionButton>(R.id.infoFAB)
        val etFactory           = Editable.Factory()
        var senderPublicKey     = -1

        pgRandomizeBTN.setOnClickListener {
            val (p, g) = generatePG(10)
            pET.text = etFactory.newEditable(p.toString())
            gET.text = etFactory.newEditable(g.toString())
            setX(generateLocalSecret(), xTV)
        }

        makePublicKeyBTN.setOnClickListener {
            p = pET.getInt()
            val g = gET.getInt()
            val x = getX(xTV)

            if (p.isPrime()) {
                if (g.isPrimitiveRootOf(p)) {
                    senderPublicKey = calculatePublicKey(g, x, p)
                    publicKeyTV.text = senderPublicKey.toString()
                    publicKeyTV.visibility = View.VISIBLE
                    sendPublicKeyBTN.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                        applicationContext,
                        "g must be a primitive root of p",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "p must be prime",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        sendPublicKeyBTN.setOnClickListener {
            if (senderPublicKey != -1) {
                val intent = Intent(this, RecipientActivity::class.java).apply {
                    putExtra(SENDERS_KEY_EXTRA, senderPublicKey)
                    putExtra(P_EXTRA, pET.getInt())
                    putExtra(G_EXTRA, gET.getInt())
                }
                startActivityForResult(intent, KEY_EXCHANGE)
            }
        }

        val onVarsChange = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                xTV.visibility = View.INVISIBLE
                publicKeyTV.visibility = View.INVISIBLE
                sendPublicKeyBTN.visibility = View.INVISIBLE
                secretKeyMemberTV.visibility = View.INVISIBLE
                secretKeyTV.visibility = View.INVISIBLE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        pET.addTextChangedListener(onVarsChange)
        gET.addTextChangedListener(onVarsChange)

        infoFAB.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                KEY_EXCHANGE -> {
                    if (data != null) {
                        val recipientsPublicKey = data.getIntExtra(RECIPIENTS_KEY_EXTRA, -1)
                        val secretKey = calculateSecretKey(recipientsPublicKey, x, p)
                        secretKeyTV.text = secretKey.toString()
                        secretKeyMemberTV.visibility = View.VISIBLE
                        secretKeyTV.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setX(x: Int, xTV: TextView) {
        this.x = x
        xTV.text = "x = %d".format(x)
        xTV.visibility = View.VISIBLE
    }

    private fun getX(xTV: TextView): Int {
        val xText = xTV.text.toString()
        return if (xText.isNotEmpty()) {
            // from `x = {num}` get the {num}
            xText.substring(4).toInt()
        } else {
            val x = generateLocalSecret()
            setX(x, xTV)
            x
        }
    }
}
