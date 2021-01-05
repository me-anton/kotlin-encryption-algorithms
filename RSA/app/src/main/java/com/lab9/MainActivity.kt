package com.lab9

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.math.BigInteger


const val PM_ME = 1
const val N_EXTRA = "n"
const val E_EXTRA = "e"

class MainActivity : AppCompatActivity() {
    private var e = -1
    private var n = -1
    private var phi = -1
    private var d = -1
    private lateinit var pET: EditText
    private lateinit var qET: EditText
    private lateinit var eET: EditText
    private lateinit var nTV: TextView
    private lateinit var phiTV: TextView
    private lateinit var dTV: TextView
    private lateinit var messageMemberTV: TextView
    private lateinit var messageTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            pET              = findViewById(R.id.pET)
            qET              = findViewById(R.id.qET)
            eET              = findViewById(R.id.eET)
            nTV              = findViewById(R.id.nTV)
            phiTV            = findViewById(R.id.phiTV)
            dTV              = findViewById(R.id.dTV)
            messageMemberTV  = findViewById(R.id.messageMemberTV)
            messageTV        = findViewById(R.id.messageTV)
        val randomizeBTN     = findViewById<Button>(R.id.randomizeBTN)
        val sendPublicKeyBTN = findViewById<Button>(R.id.sendPublicKeyBTN)
        val infoFAB          = findViewById<FloatingActionButton>(R.id.infoFAB)

        randomizeBTN.setOnClickListener {
            randomizeInput()
        }

        sendPublicKeyBTN.setOnClickListener {
            val p = pET.getInt()
            val q = qET.getInt()
            if (p.isPrime() && q.isPrime()) {
                calculateNAndPhi(p, q)
                val e = eET.getInt()
                if (e in 2 until phi && e.isCoprime(phi)) {
                    calculateD(e)
                    assignNPhiD()
                    val intent = Intent(this, RecipientActivity::class.java).apply {
                        putExtra(N_EXTRA, n)
                        putExtra(E_EXTRA, e)
                    }
                    startActivityForResult(intent, PM_ME)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "e must be coprime with φ(n)",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "p and q must be prime",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        val onVarsChange = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                nTV.visibility = View.INVISIBLE
                phiTV.visibility = View.INVISIBLE
                dTV.visibility = View.INVISIBLE
                messageMemberTV.visibility = View.INVISIBLE
                messageTV.visibility = View.INVISIBLE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        pET.addTextChangedListener(onVarsChange)
        qET.addTextChangedListener(onVarsChange)
        eET.addTextChangedListener(onVarsChange)

        infoFAB.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PM_ME -> {
                    if (data != null) {
                        val encryptedMessage =
                            data.getSerializableExtra(MESSAGE_EXTRA) as Array<ByteArray>
                        messageMemberTV.visibility = View.VISIBLE
                        messageTV.text = decryptMessage(encryptedMessage)
                        messageTV.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun decryptMessage(encryptedMessage: Array<ByteArray>): String {
        val D = d.toBigInteger()
        val N = n.toBigInteger()
        return String(
            encryptedMessage.map {
                BigInteger(it).modPow(D, N).toInt().toChar()
            }.toCharArray()
        )
    }

    private fun randomizeInput() {
        val p = randomPrime(IntRange(3,7))
        val q = randomPrime(IntRange(3,7))
        calculateNAndPhi(p, q)
        val e = findCoprime(phi)
        if (e == -1) {
            return randomizeInput()
        }
        pET.assign(p)
        qET.assign(q)
        eET.assign(e)
        this.e = e
        calculateD(e)
        assignNPhiD()
    }

    private fun calculateNAndPhi(p: Int, q: Int) {
        this.n = p * q
        this.phi = lcm(p - 1, q - 1)
    }

    private fun calculateD(e: Int) {
        d = e.toBigInteger().modInverse(phi.toBigInteger()).toInt()
    }

    private fun assignNPhiD() {
        nTV.assignExpression(n, "N")
        phiTV.assignExpression(phi, "φ")
        dTV.assignExpression(d, "d")
        nTV.visibility = View.VISIBLE
        phiTV.visibility = View.VISIBLE
        dTV.visibility = View.VISIBLE
    }
}
