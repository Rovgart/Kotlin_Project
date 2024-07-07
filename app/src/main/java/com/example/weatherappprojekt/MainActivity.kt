package com.example.weatherappprojekt

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.roundToInt

data class WeatherResponse(
    val name: String,
    val weather: List<Weather>,
    val main: Main
)

data class Weather(
    val description: String
)

data class Main(
    val temp: Float
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp() {
    var city by remember { mutableStateOf("") }
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Title with cloud icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cloud),
                    contentDescription = "Cloud Icon",
                    modifier = Modifier
                        .size(40.dp)

                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WeatherApp",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    color = Color(android.graphics.Color.parseColor("#02a9ea"))
                )
            }

            OutlinedTextField(
                value = city,
                onValueChange = {
                    city = it
                    error = false // reset error on text change
                },
                label = { Text("Enter city name") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(android.graphics.Color.parseColor("#02a9ea")),
                    unfocusedBorderColor = Color(android.graphics.Color.parseColor("#02a9ea"))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        loading = true
                        delay(2000) // Simulate loading for 2 seconds
                        weather = fetchWeather(city)
                        loading = false
                        if (weather == null) {
                            error = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#02a9ea"))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Get Weather",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (error) {
                Text(
                    text = "Invalid city name, please try again.",
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            weather?.let {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "City: ${it.name}",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Temperature: ${it.main.temp.roundToInt()}°C",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Description: ${it.weather[0].description}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Black)
            }
        }
    }
}

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

