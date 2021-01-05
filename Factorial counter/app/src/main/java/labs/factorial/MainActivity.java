package labs.factorial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {
    private TextView numberTV;
    private TextView factorialTV;
    private Button minusBtn;
    private Button plusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberTV = findViewById(R.id.numberTV);
        factorialTV = findViewById(R.id.factorialTV);
        minusBtn = findViewById(R.id.minusBtn);
        plusBtn = findViewById(R.id.plusBtn);

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementNumber();
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementNumber();
            }
        });
    }

    private void incrementNumber() {
        BigInteger oldNumber = new BigInteger(numberTV.getText().toString());
        BigInteger newNumber = oldNumber.add(BigInteger.ONE);
        numberTV.setText(String.valueOf(newNumber));

        BigInteger oldFactorial = new BigInteger(factorialTV.getText().toString());
        BigInteger newFactorial = oldFactorial.multiply(newNumber);
        factorialTV.setText(String.valueOf(newFactorial));
    }

    private void decrementNumber() {
        BigInteger oldNumber = new BigInteger(numberTV.getText().toString());
        if (!oldNumber.equals(BigInteger.ZERO)) {
            BigInteger newNumber = oldNumber.subtract(BigInteger.ONE);
            numberTV.setText(String.valueOf(newNumber));

            BigInteger oldFactorial = new BigInteger(factorialTV.getText().toString());
            BigInteger newFactorial = oldFactorial.divide(oldNumber);
            factorialTV.setText(String.valueOf(newFactorial));
        }
    }


}
