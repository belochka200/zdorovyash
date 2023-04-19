package com.example.sport.ui.uistate

sealed class HomeScreenUiState {
    object Loading : HomeScreenUiState()
    object Error : HomeScreenUiState()
    data class Content(
        val temperature: Int? = null,
        val precipitation: String? = null,
    ) : HomeScreenUiState()
}
