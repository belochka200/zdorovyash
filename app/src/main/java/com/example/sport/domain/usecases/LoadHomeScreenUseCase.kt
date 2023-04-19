package com.example.sport.domain.usecases

import android.location.Location
import com.example.sport.data.network.WeatherApiImpl

private interface LoadHomeScreenUseCase {

    /**
     * @return Pair<String, String>, где первое значение - температура, второе - текущие осадки
     * */
    suspend fun loadWeather(location: Location): Pair<Int, String> // todo: заменить String на класс осадков
}

class LoadHomeScreenUseCaseImpl(private val weatherApiImpl: WeatherApiImpl) : LoadHomeScreenUseCase {
    override suspend fun loadWeather(location: Location): Pair<Int, String> {
        val response = weatherApiImpl.getWeather(location)
        val weatherTemperature = response.main.temp.toInt()
        val weatherDescription = response.weather[0].description
        return Pair(weatherTemperature, weatherDescription)
    }
}