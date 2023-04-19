package com.example.sport.domain.models

data class Weather(
    val city: String,
    val description: String,
    val temp: Int,
    val tempMin: Int,
    val tempMax: Int,
)