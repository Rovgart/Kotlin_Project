package com.example.weatherappprojekt.data

data class WeatherResponse(
    val name: String,
    val weather: List<Weather>,
    val main: Main
)



