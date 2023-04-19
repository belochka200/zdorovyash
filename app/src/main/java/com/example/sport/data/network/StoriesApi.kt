package com.example.sport.data.network

import com.example.sport.data.models.Story

private interface StoriesApi {
    suspend fun loadStories(): List<Story>
}

class StoriesApiImpl : StoriesApi {
    override suspend fun loadStories(): List<Story> {
        val list = mutableListOf<Story>()
        repeat(10) {
            list.add(Story(it, "Image", "Heading $it"))
        }
        return list
    }
}