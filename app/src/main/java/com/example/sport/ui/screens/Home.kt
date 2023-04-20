package com.example.sport.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.sport.R
import com.example.sport.data.models.SportCardItem
import com.example.sport.data.models.Story
import com.example.sport.databinding.FragmentHomeBinding
import com.example.sport.ui.adapters.SportCardAdapter
import com.example.sport.ui.adapters.StoryCardAdapter
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
                                maxTemp = uiState.temperatureMax,
                                minTemp = uiState.temperatureMin,
                                currentPrecipitation = uiState.precipitation,
                                weatherIcon = uiState.weatherIcon,
                                city = uiState.city,
                                stories = uiState.storiesCards,
                                sportCards = uiState.sportCards
                            )
                        }

                        HomeScreenUiState.NoLocation -> noLocation()
                        HomeScreenUiState.Error -> {}
                        HomeScreenUiState.Loading -> showLoading()
                    }
                }
            }
        }
    }

    private fun noLocation() {
        hideLoading()
        if (checkLocationPermission())
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null)
                    homeViewModel.refreshWeather(location)
//                else
                // todo: Найти город
            }
        else
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
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
        binding.apply {
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_item__home_settings -> {
                        findNavController().navigate(R.id.action_homeScreen_to_settings)
                        true
                    }

                    else -> false
                }
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                if (checkLocationPermission())
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null)
                            homeViewModel.refreshWeather(location)
                        else
                            Toast.makeText(requireContext(), "Введите город", Toast.LENGTH_LONG).show()
                    }
                else
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    private fun showUiContent(
        currentTemperature: Int,
        maxTemp: Int,
        minTemp: Int,
        currentPrecipitation: String,
        weatherIcon: String,
        city: String,
        stories: List<Story>,
        sportCards: List<SportCardItem>
    ) {
        hideLoading()
        binding.apply {
            textViewCurrentPrecipitation.text =
                currentPrecipitation.replaceFirstChar { it.uppercase() }
            textViewCurrentTemperature.text =
                if (currentTemperature > 0)
                    "+${getString(R.string.temperature_mask, currentTemperature)}"
                else
                    getString(R.string.temperature_mask, currentTemperature)
            textViewTempMaxMin.text = getString(R.string.temp_mask_max_min, maxTemp, minTemp)
            textViewCity.text = city
//            imageWeatherIcon.load()
            recyclerViewStories.adapter = StoryCardAdapter(stories)
            recyclerViewSports.adapter = SportCardAdapter(sportCards)
            nestedScrollView.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    if (scrollY > textViewCurrentPrecipitation.bottom)
                        toolbar.title =
                            if (currentTemperature > 0)
                                "+${
                                    getString(
                                        R.string.temperature_mask,
                                        currentTemperature
                                    )
                                }, $currentPrecipitation"
                            else
                                "${
                                    getString(
                                        R.string.temperature_mask,
                                        currentTemperature
                                    )
                                }, $currentPrecipitation"
                    else
                        toolbar.title = getString(R.string.app_name)
                })
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
        }
    }

    private fun hideLoading() {
        binding.apply {
            linearProgressIndicator.isVisible = false
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
