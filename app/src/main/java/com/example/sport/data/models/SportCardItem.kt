package com.example.sport.data.models

import kotlinx.serialization.Serializable

@Serializable
data class SportCardItem(
    val image: String,
    val pk: Int,
    val title: String
)