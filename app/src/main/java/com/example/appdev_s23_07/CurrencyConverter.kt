package com.example.appdev_s23_07

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object CurrencyConverter {
    private const val BASE_CURRENCY = "USD"
    private const val API_ACCESS_KEY = "5kkSy5gBQczsiVxdjEYrGQzxGFG1wLWl" // Replace with your API access key
    private const val API_ENDPOINT = "https://api.exchangeratesapi.io/latest?base=$BASE_CURRENCY&access_key=$API_ACCESS_KEY"

    suspend fun convertCurrency(amount: Double, targetCurrency: String, context: Context): Double {
        return withContext(Dispatchers.IO) {
            getConversionRate(context, targetCurrency) * amount
        }
    }

    private suspend fun getConversionRate(context: Context, targetCurrency: String): Double {
        val currentDate = getCurrentDate()
        val jsonFileName = "$currentDate.json"
        val jsonFile = File(context.filesDir, jsonFileName)

        return if (jsonFile.exists()) {
            // Read conversion rate from local storage
            val jsonContent = jsonFile.readText()
            val jsonObject = JSONObject(jsonContent)
            val rates = jsonObject.getJSONObject("rates")
            rates.getDouble(targetCurrency)
        } else {
            // Fetch conversion rate from API and save to local storage
            val jsonContent = withContext(Dispatchers.IO) {
                URL(API_ENDPOINT).readText()
            }

            println(jsonContent) // Print the JSON response for debugging

            val jsonObject = JSONObject(jsonContent)
            val rates = jsonObject.getJSONObject("rates")
            val conversionRate = rates.getDouble(targetCurrency)

            // Save to local storage
            withContext(Dispatchers.IO) {
                jsonFile.writeText(jsonContent)
            }

            conversionRate
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
