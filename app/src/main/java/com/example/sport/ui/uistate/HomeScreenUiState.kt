package com.example.sport.ui.uistate

sealed class HomeScreenUiState {
    object Loading : HomeScreenUiState()
    object Error : HomeScreenUiState()
    data class Content(
        val temperature: Int,
        val precipitation: String,
    ) : HomeScreenUiState()
}
