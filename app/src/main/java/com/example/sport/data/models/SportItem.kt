package com.example.sport.data.models

import kotlinx.serialization.Serializable

@Serializable
data class SportItem(
    val id: Int,
    val heading: String,
    val image: String,
)