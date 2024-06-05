package com.tpro.brokenapp.tpro.brokenapp.tpro.broeknapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tpro.brokenapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var firstNumber: EditText
    private lateinit var secondNumber: EditText
    private lateinit var calculateButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var viewModel: CalculationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstNumber = findViewById(R.id.editext1)
        secondNumber = findViewById(R.id.editext2)
        calculateButton = findViewById(R.id.button)
        resultTextView = findViewById(R.id.result)

        viewModel = ViewModelProvider(this).get(CalculationViewModel::class.java)

        calculateButton.setOnClickListener {
            val text1 = firstNumber.text.toString().trim().lowercase()
            val text2 = secondNumber.text.toString().trim().lowercase()
            viewModel.addLargeNumbers(text1, text2)
        }

        viewModel.result.observe(this) {
            resultTextView.text = it
        }

        viewModel.error.observe(this) {
            showErrorToast(it)
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}