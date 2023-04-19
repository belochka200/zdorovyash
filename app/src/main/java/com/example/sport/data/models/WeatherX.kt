package com.example.sport.data.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)