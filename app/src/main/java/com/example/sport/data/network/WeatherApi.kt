package com.example.sport.data.network

import android.location.Location
import android.util.Log
import com.example.sport.data.models.weather.WeatherAnswer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL

const val WEATHER_API_KEY = "53af612e6252b27564da95ca8b0a540c"

private interface WeatherApi {
    suspend fun getWeather(location: Location): WeatherAnswer
}

class WeatherApiImpl : WeatherApi {
    override suspend fun getWeather(location: Location): WeatherAnswer {
        val json = Json { ignoreUnknownKeys = true }
        val response =
            URL("https://api.openweathermap.org/data/2.5/weather?lat=${location.latitude}&lon=${location.longitude}&appid=$WEATHER_API_KEY&lang=ru&units=metric").readText()
        Log.d("Response weather", response)
        return json.decodeFromString(response)
    }
}

