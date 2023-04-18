package com.example.sport.data.network

const val WEATHER_API_KEY = "53af612e6252b27564da95ca8b0a540c"

interface WeatherApi {
    suspend fun getWeather(): Int
}

class WeatherApiImpl : WeatherApi {
    override suspend fun getWeather(): Int {
        return 1
    }
}
