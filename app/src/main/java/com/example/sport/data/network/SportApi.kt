package com.example.sport.data.network

import android.util.Log
import com.example.sport.data.models.sport.SportItem
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL

private const val BASE_URL_SPORTS = "https://neuromantics.online/sports"

private interface SportApi {
    suspend fun loadSportItems(): List<SportItem>
    suspend fun loadOneItemById(id: Int): SportItem
}

class SportApiImpl : SportApi {
    override suspend fun loadSportItems(): List<SportItem> {
        val json = Json { ignoreUnknownKeys = true }
        val response = URL(BASE_URL_SPORTS).readText()
        Log.d("Response", response)
        return json.decodeFromString(response)
    }

    override suspend fun loadOneItemById(id: Int): SportItem {
        val json = Json { ignoreUnknownKeys = true }
        val response = URL("$BASE_URL_SPORTS/$id").readText()
        Log.d("Response", response)
        return json.decodeFromString(response)
    }
}