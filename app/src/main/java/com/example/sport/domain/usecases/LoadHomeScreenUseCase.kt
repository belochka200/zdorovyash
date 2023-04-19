package com.example.sport.domain.usecases

import android.location.Location
import android.util.Log
import com.example.sport.data.network.WeatherApiImpl

private interface LoadHomeScreenUseCase {

    /**
     * @return Pair<String, String>, где первое значение - температура, второе - текущие осадки
     * */
    suspend fun loadWeather(location: Location): Map<String, Any> // todo: заменить String на класс осадков
}

class LoadHomeScreenUseCaseImpl(private val weatherApiImpl: WeatherApiImpl) : LoadHomeScreenUseCase {
    override suspend fun loadWeather(location: Location): Map<String, Any> {
        val response = weatherApiImpl.getWeather(location)
        val weatherTemperature = response.main.temp.toInt()
        val weatherDescription = response.weather[0].description
        val city = response.name
        Log.d("weather", response.toString())
        val icon = weatherApiImpl.getWeatherIcon(response.weather[0].icon)
        val answer = mutableMapOf<String, Any>()
        answer["temp"] = weatherTemperature
        answer["description"] = weatherDescription
        answer["icon"] = icon
        answer["city"] = city
        return answer
    }
}