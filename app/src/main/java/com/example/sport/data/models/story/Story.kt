package com.example.sport.data.models.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Story(
    val id: Int,
    val description: String,
    val heading: String
)
