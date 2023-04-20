package com.example.sport.data.network

import android.util.Log
import com.example.sport.data.models.story.Story
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL

private const val BASE_URL_STORIES = "https://neuromantics.online/stories"

private interface StoriesApi {
    suspend fun loadStories(): List<Story>
}

class StoriesApiImpl : StoriesApi {
    override suspend fun loadStories(): List<Story> {
        val json = Json { ignoreUnknownKeys = true }
        val response = URL(BASE_URL_STORIES).readText()
        Log.d("Response stories", response)
//        return json.decodeFromString(response)
        return emptyList() // fixme: delete stories
    }
}