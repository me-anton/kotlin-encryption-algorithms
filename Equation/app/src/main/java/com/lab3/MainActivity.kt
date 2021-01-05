package com.lab3
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val xInput = findViewById<EditText>(R.id.xET)
        val yInput = findViewById<EditText>(R.id.yET)
        val zInput = findViewById<EditText>(R.id.zET)
        val resultTV = findViewById<TextView>(R.id.resultTV)

        findViewById<Button>(R.id.submit).setOnClickListener {
            val phi = getPhi(xInput, yInput, zInput)
            resultTV.text = phi
        }
    }

    fun getPhi(xInput : EditText, yInput : EditText, zInput : EditText): String {
        val etUnpacker = fun (et: EditText): Double {
            return et.text.toString().toDouble()
        }

        val x = etUnpacker.invoke(xInput)
        val y = etUnpacker.invoke(yInput)
        val z = etUnpacker.invoke(zInput)
        return calculate(x, y, z).toString()
    }

    fun calculate(x: Double, y: Double, z: Double): Double {
        val numerator = Math.exp(Math.abs(x - y)) * Math.pow(Math.abs(x - y), x + y)
        val denominator = Math.atan(x) + Math.atan(z)
        val secondMember = Math.cbrt(Math.pow(x, 6.0) + Math.pow(Math.log(y), 2.0))
        return numerator/denominator + secondMember
    }
}
