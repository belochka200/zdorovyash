package com.example.sport.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sport.R
import com.example.sport.databinding.FragmentHomeBinding
import com.example.sport.ui.uistate.HomeScreenUiState
import com.example.sport.ui.viewmodels.HomeViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class Home : Fragment(R.layout.fragment__home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.Factory }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                // get weather and update ui state
            } else {
                Toast.makeText(requireContext(), "Location permission not granted", Toast.LENGTH_LONG).show()
                // show error message
//                requireActivity().finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is HomeScreenUiState.Content -> {
                            showUiContent(
                                currentTemperature = uiState.temperature,
                                currentPrecipitation = uiState.precipitation
                            )
                            when (checkLocationPermission()) {
                                true -> {
                                    val fusedLocationClient =
                                        LocationServices.getFusedLocationProviderClient(
                                            requireContext()
                                        )
                                    fusedLocationClient.lastLocation.addOnSuccessListener {

                                    }
                                }
                                false -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                                null -> {} // should show ui notification
                            }
                        }

                        HomeScreenUiState.Error -> {}
                        HomeScreenUiState.Loading -> {}
                    }
                }
            }
        }
    }

    private fun checkLocationPermission(): Boolean? {
        return when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is granted
                Toast.makeText(requireContext(), "Permission is granted", Toast.LENGTH_LONG).show()
                true
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                // should show ui notification
                Toast.makeText(requireContext(), "Should show notification", Toast.LENGTH_LONG)
                    .show()
                null
            }

            else -> false
        }
    }

    private fun showUiContent(currentTemperature: Int, currentPrecipitation: String) {
        binding.apply {
            textViewCurrentTemperature.text = currentTemperature.toString()
            textViewCurrentPrecipitation.text = currentPrecipitation
        }
        checkLocationPermission()
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