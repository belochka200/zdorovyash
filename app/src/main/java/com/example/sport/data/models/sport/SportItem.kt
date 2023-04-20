package com.example.sport.data.models.sport

import kotlinx.serialization.Serializable

@Serializable
data class SportItem(
    val id: Int,
    val title: String,
    val description: String,
    val season: Int,
    val image: String,
)