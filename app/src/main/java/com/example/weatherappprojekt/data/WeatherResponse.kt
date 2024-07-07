package com.example.weatherappprojekt.data

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