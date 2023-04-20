package com.example.sport.ui.uistate

sealed class DetailSportUiState {
    object Loading : DetailSportUiState()
    object Error : DetailSportUiState()
    data class Content(
        val id: Int,
        val title: String,
        val description: String,
        val season: String,
        val image: String,
    ) : DetailSportUiState()
}
