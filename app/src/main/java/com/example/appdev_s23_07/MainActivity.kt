package com.example.appdev_s23_07

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.appdev_s23_07.CurrencyConverter.convertCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var amountEditText: EditText
    private lateinit var convertButton: Button
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        amountEditText = findViewById(R.id.amountEditText)
        convertButton = findViewById(R.id.convertButton)
        resultTextView = findViewById(R.id.resultTextView)

        convertButton.setOnClickListener {
            val amount = amountEditText.text.toString().toDoubleOrNull()
            if (amount != null) {
                lifecycleScope.launch {
                    convertCurrency(amount)
                }
            }
        }
    }

    private suspend fun convertCurrency(amount: Double) {
        val targetCurrency = "EUR" // Replace with your desired target currency code
        val convertedAmount = CurrencyConverter.convertCurrency(amount, targetCurrency, applicationContext)
        val decimalFormat = DecimalFormat("#,###.##")
        withContext(Dispatchers.Main) {
            resultTextView.text = decimalFormat.format(convertedAmount)
        }
    }
}