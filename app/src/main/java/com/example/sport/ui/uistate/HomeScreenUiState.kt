package com.example.sport.ui.uistate

import com.example.sport.data.models.sport.SportItem
import com.example.sport.data.models.story.Story

sealed class HomeScreenUiState {
    object Loading : HomeScreenUiState()
    object Error : HomeScreenUiState()
    object NoLocation : HomeScreenUiState()
    data class Content(
        val temperature: Int,
        val temperatureMax: Int,
        val temperatureMin: Int,
        val precipitation: String,
        val weatherIcon: String,
        val city: String,
        val storiesCards: List<Story>,
        val sportCards: List<SportItem>,
    ) : HomeScreenUiState()
}
