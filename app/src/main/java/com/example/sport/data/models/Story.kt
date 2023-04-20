package com.example.sport.data.models

import kotlinx.serialization.Serializable


@Serializable
data class Story(
    val id: Int,
    val image: String,
    val description: String,
    val heading: String
)
