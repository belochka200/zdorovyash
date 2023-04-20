package com.example.sport.data.network

import com.example.sport.data.models.SportCardItem
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL

private const val BASE_URL_SPORTS = "https://neuromantics.online/sports"

private interface SportApi {
    suspend fun loadSportItems(): List<SportCardItem>
}

class SportApiImpl : SportApi {
    override suspend fun loadSportItems(): List<SportCardItem> {
        val response = URL(BASE_URL_SPORTS).readText()
        return Json.decodeFromString(response)
    }
}