package com.example.sport.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.sport.R
import com.example.sport.databinding.FragmentHomeBinding
import com.example.sport.ui.uistate.HomeScreenUiState
import com.example.sport.ui.viewmodels.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class Home : Fragment(R.layout.fragment__home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.Factory }

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    if (it != null)
                        homeViewModel.refreshWeather(it)
                    else {
                        Toast.makeText(requireContext(), "Введите город", Toast.LENGTH_LONG).show()
//                        findNavController().navigate(R.id.action_homeScreen_to_settings)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Введите город", Toast.LENGTH_LONG).show()
//                findNavController().navigate(R.id.action_homeScreen_to_settings)
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
                                currentPrecipitation = uiState.precipitation,
                                weatherIcon = uiState.weatherIcon,
                                city = uiState.city
                            )
                        }

                        HomeScreenUiState.Error -> {}
                        HomeScreenUiState.Loading -> showLoading()
                    }
                }
            }
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

    private fun showUiContent(currentTemperature: Int?, currentPrecipitation: String?, weatherIcon: String?, city: String?) {
        hideLoading()
        if (currentTemperature == null || currentPrecipitation == null)
            when (checkLocationPermission()) {
                true -> {
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { lastLocation ->
                        if (lastLocation != null)
                            homeViewModel.refreshWeather(lastLocation)
                        else {
                            Toast.makeText(requireContext(), "Введите город", Toast.LENGTH_LONG).show()
//                            findNavController().navigate(R.id.action_homeScreen_to_settings)
                        }
                    }
                }

                false -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        binding.apply {
            textViewCurrentPrecipitation.text = currentPrecipitation?.firstLetterUppercase() ?: ""
            textViewCurrentTemperature.text = when {
                currentTemperature == null -> ""
                currentTemperature > 0 -> "+${getString(R.string.temperature_mask, currentTemperature)}"
                else -> getString(R.string.temperature_mask, currentTemperature)
            }
            textViewCity.text = city ?: ""
//            imageWeatherIcon.load()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                true
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                showLocationRequiredDialog()
                false
            }

            else -> false
        }
    }

    private fun showLocationRequiredDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.location_required)
            .setMessage("Для определения погоды приложению необходим доступ к вашему местоположению")
            .setPositiveButton(R.string.allow) { dialog, _ ->
                dialog.cancel()
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            .setNegativeButton(R.string.not_allow) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showLoading() {
        binding.apply {
            linearProgressIndicator.isVisible = true
            groupTopBar.isVisible = false
        }
    }

    private fun hideLoading() {
        binding.apply {
            linearProgressIndicator.isVisible = false
            groupTopBar.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun String.firstLetterUppercase(): CharSequence {
    return this[0].uppercase() + this.substring(1)
}
