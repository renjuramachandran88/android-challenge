package com.tpro.brokenapp.tpro.brokenapp.tpro.broeknapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class CalculationViewModel : ViewModel() {

     val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

     val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private fun isValidInput(text1: String, text2: String): Boolean {
        val regex = "^\\d+(\\.\\d+)?\$".toRegex()
        return text1.isNotEmpty() && text2.isNotEmpty() &&
                regex.matches(text1) && regex.matches(text2)
    }

    fun addLargeNumbers(num1: String, num2: String) {
        viewModelScope.launch {
            if (!isValidInput(num1, num2)) {
                _error.postValue("Number is in invalid format")
            } else {
                val sum = withContext(Dispatchers.IO) {
                    BigDecimal(num1).add(BigDecimal(num2))
                }
                _result.postValue(sum.toString())
            }
        }
    }
}