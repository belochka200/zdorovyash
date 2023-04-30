package com.example.sport.ui.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.sport.SportApplication
import com.example.sport.data.network.SportApiImpl
import com.example.sport.data.network.WeatherApiImpl
import com.example.sport.domain.usecases.LoadHomeScreenUseCaseImpl
import com.example.sport.ui.uistate.HomeScreenUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherApiImpl: WeatherApiImpl,
//    private val storiesApiImpl: StoriesApiImpl,
    private val sportApiImpl: SportApiImpl
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(HomeScreenUiState.Loading)
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun refreshWeather(location: Location) {
        _uiState.value = HomeScreenUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val useCase = LoadHomeScreenUseCaseImpl(weatherApiImpl, sportApiImpl)
                val weatherResponse = useCase.loadWeather(location)
                Log.d("Icon", weatherResponse.icon)
                val sportItems = useCase.loadSportCards()
                _uiState.value =
                    HomeScreenUiState.Content(
                        temperature = weatherResponse.temp,
                        precipitation = weatherResponse.description,
                        weatherIcon = weatherResponse.icon, // fixme иконка загрузки
                        city = weatherResponse.city,
                        sportCards = sportItems,
                        temperatureMax = weatherResponse.tempMax,
                        temperatureMin = weatherResponse.tempMin
                    )
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("Error API", e.toString())
                _uiState.value = HomeScreenUiState.Error
                _isLoading.value = false
            }
        }
    }

    fun hideSplash() {
        _isLoading.value = false
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = HomeScreenUiState.NoLocation
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return HomeViewModel(
                    (application as SportApplication).weatherApiImpl,
//                    application.storiesApiImpl,
                    application.sportApiImpl
                ) as T
            }
        }
    }
}
