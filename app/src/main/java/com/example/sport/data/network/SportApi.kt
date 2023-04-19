package com.example.sport.data.network

import com.example.sport.data.models.SportItem

private interface SportApi {
    // load last 10 elements
    suspend fun loadSportItems(): List<SportItem>
//    fun loadAllSportItems(): Flow<List<SportItem>>
}

class SportApiImpl : SportApi {
    override suspend fun loadSportItems(): List<SportItem> {
        return listOf(
            SportItem(0, "Test", "image")
        )
    }

//    override fun loadAllSportItems(): Flow<List<SportItem>> {
//        val list = mutableListOf<SportItem>()
//        return flow {
//            repeat(10) {
//                delay(1000)
//                list.add(SportItem(it, "123", "title $it"))
//                emit(list)
//            }
//        }
//    }
}