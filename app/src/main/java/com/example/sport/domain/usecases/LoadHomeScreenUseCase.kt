package com.example.sport.domain.usecases

import android.location.Location
import android.util.Log
import com.example.sport.data.models.sport.SportItem
import com.example.sport.data.models.story.Story
import com.example.sport.data.network.SportApiImpl
import com.example.sport.data.network.StoriesApiImpl
import com.example.sport.data.network.WeatherApiImpl
import com.example.sport.domain.models.Weather

private interface LoadHomeScreenUseCase {
    suspend fun loadWeather(location: Location): Weather
    suspend fun loadStory(): List<Story>
    suspend fun loadSportCards(): List<SportItem>
}

class LoadHomeScreenUseCaseImpl(
    private val weatherApiImpl: WeatherApiImpl,
    private val storiesApi: StoriesApiImpl,
    private val sportApiImpl: SportApiImpl
) : LoadHomeScreenUseCase {
    override suspend fun loadWeather(location: Location): Weather {
        val response = weatherApiImpl.getWeather(location)
        // fixme другие иконки
//        val icon = weatherApiImpl.getWeatherIcon(response.weather[0].icon)
        val icon = "_" + response.weather[0].icon.substring(response.weather[0].icon.length - 3) + ".svg"
        return Weather(
            city = response.name,
            description = response.weather.first().description,
            temp = response.main.temp.toInt(),
            tempMax = response.main.temp_max.toInt(),
            tempMin = response.main.temp_min.toInt(),
            icon = icon
        )
    }

    override suspend fun loadStory(): List<Story> {
        return storiesApi.loadStories()
    }

    override suspend fun loadSportCards(): List<SportItem> {
        return sportApiImpl.loadSportItems()
    }
}