package com.example.sport.domain.usecases

import android.location.Location
import com.example.sport.data.models.SportCard
import com.example.sport.data.models.SportCardItem
import com.example.sport.data.models.SportItem
import com.example.sport.data.models.Story
import com.example.sport.data.network.SportApiImpl
import com.example.sport.data.network.StoriesApiImpl
import com.example.sport.data.network.WeatherApiImpl
import com.example.sport.domain.models.Weather

private interface LoadHomeScreenUseCase {
    suspend fun loadWeather(location: Location): Weather // todo: заменить Map на класс осадков
    suspend fun loadStory(): List<Story>
    suspend fun loadSportCards(): List<SportCard>
}

class LoadHomeScreenUseCaseImpl(private val weatherApiImpl: WeatherApiImpl, private val storiesApi: StoriesApiImpl, private val sportApiImpl: SportApiImpl) : LoadHomeScreenUseCase {
    override suspend fun loadWeather(location: Location): Weather {
        val response = weatherApiImpl.getWeather(location)
        val weatherTemperature = response.main.temp.toInt()
        val weatherDescription = response.weather[0].description
        val city = response.name
        // fixme другие иконки
//        val icon = weatherApiImpl.getWeatherIcon(response.weather[0].icon)
        return Weather(
            city = response.name,
            description = response.weather.first().description,
            temp = response.main.temp.toInt(),
            tempMax = response.main.temp_max.toInt(),
            tempMin = response.main.temp_min.toInt(),
        )
    }

    override suspend fun loadStory(): List<Story> {
        return storiesApi.loadStories()
    }

    override suspend fun loadSportCards(): List<SportCard> {
        return sportApiImpl.loadSportItems()
    }
}