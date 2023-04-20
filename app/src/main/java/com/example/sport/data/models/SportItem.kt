package com.example.sport.data.models

import kotlinx.serialization.Serializable


data class SportItem(
    val pk: Int,
    val title: String,
    val image: String,
)