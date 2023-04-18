package com.example.sport

import android.app.Application
import com.google.android.material.color.DynamicColors

class SportApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}