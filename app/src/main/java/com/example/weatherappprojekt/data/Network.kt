package com.example.weatherappprojekt.data

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.roundToInt

suspend fun fetchWeather(city: String): WeatherResponse? {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://open-weather13.p.rapidapi.com/city/$city/EN")
                .get()
                .addHeader("x-rapidapi-key", "8a7aeb50admsh65a7200976309c2p10bee7jsn9cbef9cbcb04")
                .addHeader("x-rapidapi-host", "open-weather13.p.rapidapi.com")
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                Log.e("fetchWeather", "Unexpected response code: ${response.code}:${response.networkResponse}")
                return@withContext null
            }

            val responseBody = response.body?.string() ?: return@withContext null
            val weatherResponse = Gson().fromJson(responseBody, WeatherResponse::class.java)
            // Fahrenheit to Celsius
            val tempCelsius = ((weatherResponse.main.temp - 32) * 5 / 9).roundToInt()
            weatherResponse.copy(main = weatherResponse.main.copy(temp = tempCelsius.toFloat()))
        } catch (e: Exception) {
            Log.e("fetchWeather", "Error fetching weather", e)
            null
        }
    }
}
