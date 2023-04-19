package com.example.sport.ui.viewmodels

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.sport.SportApplication
import com.example.sport.data.network.WeatherApiImpl
import com.example.sport.domain.usecases.LoadHomeScreenUseCaseImpl
import com.example.sport.ui.uistate.HomeScreenUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val weatherApiImpl: WeatherApiImpl) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(HomeScreenUiState.Loading)
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    fun refreshWeather(location: Location) {
        _uiState.value = HomeScreenUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = LoadHomeScreenUseCaseImpl(weatherApiImpl).loadWeather(location)
            _uiState.value =
                HomeScreenUiState.Content(
                    temperature = response["temp"].toString().toInt(),
                    precipitation = response["description"].toString(),
                    weatherIcon = response["icon"].toString(),
                    city = response["city"].toString()
                )
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
//            val response = LoadHomeScreenUseCaseImpl(weatherApiImpl).loadWeather()
            _uiState.value = HomeScreenUiState.Content()
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return HomeViewModel(
                    (application as SportApplication).weatherApiImpl
                ) as T
            }
        }
    }
}
