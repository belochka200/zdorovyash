package com.example.sport

import android.app.Application
import com.example.sport.data.network.WeatherApiImpl
import com.google.android.material.color.DynamicColors

class SportApplication : Application() {

    val weatherApiImpl: WeatherApiImpl by lazy {
        WeatherApiImpl()
    }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}