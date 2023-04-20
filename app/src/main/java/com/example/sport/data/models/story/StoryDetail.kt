package com.example.sport.data.models.story

import kotlinx.serialization.Serializable

@Serializable
data class StoryDetail(
    val pk: Int,
    val heading: String,
    val description: String,
    val imge: String,
)