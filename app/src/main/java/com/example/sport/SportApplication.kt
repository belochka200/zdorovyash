package com.example.sport

//import com.example.sport.data.network.StoriesApiImpl
import android.app.Application
import com.example.sport.data.network.SportApiImpl
import com.example.sport.data.network.WeatherApiImpl

class SportApplication : Application() {

    val weatherApiImpl by lazy { WeatherApiImpl() }

    //    val storiesApiImpl by lazy { StoriesApiImpl() }
    val sportApiImpl by lazy { SportApiImpl() }

    override fun onCreate() {
        super.onCreate()
//        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}