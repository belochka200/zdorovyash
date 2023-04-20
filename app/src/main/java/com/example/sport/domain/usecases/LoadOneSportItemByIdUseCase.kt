package com.example.sport.domain.usecases

import com.example.sport.data.models.sport.SportItem
import com.example.sport.data.network.SportApiImpl

private interface LoadOneSportItemByIdUseCase {
    suspend fun loadOneSportById(id: Int): SportItem
}

class LoadOneSportItemByIdUseCaseImpl(private val sportApiImpl: SportApiImpl) : LoadOneSportItemByIdUseCase {
    override suspend fun loadOneSportById(id: Int): SportItem {
        return sportApiImpl.loadOneItemById(id)
    }
}