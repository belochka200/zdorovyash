package com.example.sport.ui.screens

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.example.sport.R
import com.example.sport.databinding.FragmentSportDetailBinding
import com.example.sport.ui.uistate.DetailSportUiState
import com.example.sport.ui.viewmodels.DetailSportViewModel
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.launch

class SportDetail : Fragment(R.layout.fragment__sport_detail) {
    private var _binding: FragmentSportDetailBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val detailSportViewModel: DetailSportViewModel by viewModels { DetailSportViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailSportViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is DetailSportUiState.Content -> {
                            showContent(
                                image = uiState.image,
                                season = uiState.season,
                                title = uiState.title,
                                description = uiState.description
                            )
                        }

                        DetailSportUiState.Error -> {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
                        }

                        DetailSportUiState.Loading -> showLoading()
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
        _binding = FragmentSportDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun showContent(image: String, title: String, season: String, description: String) {
        hideLoading()
        binding.apply {
            val imageLoader = ImageLoader.Builder(requireContext())
                .components {
                    if (Build.VERSION.SDK_INT >= 28)
                        add(ImageDecoderDecoder.Factory())
                    else
                        add(GifDecoder.Factory())
                }.build()
            imageMascot.load(image, imageLoader = imageLoader) { crossfade(500) }
            textViewSportTitle.text = title
            chipSportSeason.text = season
//            textViewDescription.text = description
            toolbar.title = title
        }
    }

    private fun showLoading() {
        detailSportViewModel.loadSportItemById(arguments?.getInt("sportId") ?: -1)
        binding.linearProgressIndicator.isVisible = true
    }

    private fun hideLoading() {
        binding.linearProgressIndicator.isVisible = false
    }
}