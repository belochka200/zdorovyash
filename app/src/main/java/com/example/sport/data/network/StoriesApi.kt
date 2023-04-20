package com.example.sport.data.network

import com.example.sport.data.models.Story
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL

private const val BASE_URL_STORIES = "https://neuromantics.online/stories"

private interface StoriesApi {
    suspend fun loadStories(): List<Story>
}

class StoriesApiImpl : StoriesApi {
    override suspend fun loadStories(): List<Story> {
        val response = URL(BASE_URL_STORIES).readText()
        return Json.decodeFromString(response)
    }
}