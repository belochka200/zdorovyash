package com.example.sport.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sport.R
import com.example.sport.databinding.FragmentHomeBinding
import com.example.sport.ui.uistate.HomeScreenUiState
import com.example.sport.ui.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

class Home : Fragment(R.layout.fragment__home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is HomeScreenUiState.Content -> showUiContent(
                            currentTemperature = uiState.temperature,
                            currentPrecipitation = uiState.precipitation
                        )

                        HomeScreenUiState.Error -> {}
                        HomeScreenUiState.Loading -> {}
                    }
                }
            }
        }
    }

    private fun showUiContent(currentTemperature: Int, currentPrecipitation: String) {
        binding.apply {
            textViewCurrentTemperature.text = currentTemperature.toString()
            textViewCurrentPrecipitation.text = currentPrecipitation
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}