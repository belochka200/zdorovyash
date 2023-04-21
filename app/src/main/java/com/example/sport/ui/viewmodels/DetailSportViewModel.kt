package com.example.sport.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.sport.SportApplication
import com.example.sport.data.network.SportApiImpl
import com.example.sport.domain.usecases.LoadOneSportItemByIdUseCaseImpl
import com.example.sport.ui.uistate.DetailSportUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailSportViewModel(private val sportApiImpl: SportApiImpl) : ViewModel() {
    private val _uiState: MutableStateFlow<DetailSportUiState> = MutableStateFlow(DetailSportUiState.Loading)
    val uiState: StateFlow<DetailSportUiState> = _uiState.asStateFlow()

    fun loadSportItemById(id: Int) {
        _uiState.value = DetailSportUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = LoadOneSportItemByIdUseCaseImpl(sportApiImpl).loadOneSportById(id)
                val locations: List<String> = response.locations.split(";/r/n")[0].split(";").map { it.trim() } // получаем каждый элемент локации
                val products: List<String> = response.products.split(";/r/n")[0].split(";").map { it.trim() } // получаем кажды элемент продуктов
                Log.d("Products Locations", locations.toString())
                Log.d("Products Locations", products.toString())
                _uiState.value = DetailSportUiState.Content(
                    id = response.id,
                    title = response.title,
                    description = response.description,
                    season = "Сезон: лето", // fixme фикс сезона, получечние с сервера
                    image = response.image,
                    location = locations,
                    products = products
                )
            } catch (e: Exception) {
                Log.e("Exception", e.toString())
                _uiState.value = DetailSportUiState.Error
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return DetailSportViewModel(
                    (application as SportApplication).sportApiImpl
                ) as T
            }
        }
    }
}
