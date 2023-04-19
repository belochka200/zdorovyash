package com.example.sport.data.network

import android.location.Location
import com.example.sport.data.models.Weather
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.URL

const val WEATHER_API_KEY = "53af612e6252b27564da95ca8b0a540c"

private interface WeatherApi {
    suspend fun getWeather(location: Location): Weather
    suspend fun getWeatherIcon(imageName: String): URI
}

class WeatherApiImpl : WeatherApi {
    override suspend fun getWeather(location: Location): Weather {
        val response =
            URL("https://api.openweathermap.org/data/2.5/weather?lat=${location.latitude}&lon=${location.longitude}&appid=$WEATHER_API_KEY&lang=ru&units=metric").readText()
        return Json.decodeFromString(response)
    }

    override suspend fun getWeatherIcon(imageName: String): URI {
        val response = URL("http://openweathermap.org/img/w/$imageName.png")
        return response.toURI()
    }
}
