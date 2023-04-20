package com.example.sport.ui.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.sport.R
import com.example.sport.ui.viewmodels.HomeViewModel

class MainActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                homeViewModel.isLoading.value
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}