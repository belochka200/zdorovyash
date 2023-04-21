package com.example.sport.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.sport.data.models.ProductCard
import com.example.sport.databinding.FragmentSportDetailBinding
import com.example.sport.ui.adapters.ProductsSportItemAdapter
import com.example.sport.ui.adapters.WhatIsUseAdapter
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
                                description = uiState.description,
                                locations = uiState.location,
                                products = uiState.products
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

    private fun showContent(
        image: String,
        title: String,
        season: String,
        description: String,
        locations: List<String>,
        products: List<String>
    ) {
        hideLoading()
        // fixme исправить весь код
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
//            chipSportSeason.text = season
            toolbar.title = title
            textViewWhatIsUse
            val tempProducts = mutableListOf<ProductCard>()
            var name: String = ""
            products.forEachIndexed { index, item ->
                if (index == 0 || index % 4 == 0)
                    name = item
                else
                    tempProducts.add(
                        ProductCard(
                            name,
                            item.split('|').first().toString(),
                            item.split('|').last()
                        )
                    )
            }
            recyclerViewProducts.adapter = ProductsSportItemAdapter(tempProducts) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
                startActivity(intent)
            }
            val tempLocations = mutableListOf<ProductCard>()
            locations.forEachIndexed { index, item ->
                Log.d("Locations", item)
                val tempSplit = item.split("|")
                tempLocations.add(
                    ProductCard(
                        tempSplit.last(),
                        tempSplit.first().toString(),
                        null
                    )
                )
            }
            val tempWhatIsUse = mutableSetOf<String>()
            tempProducts.forEach {
                tempWhatIsUse.add(it.title)
            }
            val l = mutableListOf<String>()
            tempWhatIsUse.forEach {
                l.add(it)
            }
            recyclerViewWhatIsUse.adapter = WhatIsUseAdapter(l)
            recyclerViewLocations.adapter = ProductsSportItemAdapter(tempLocations) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
                startActivity(intent)
            }
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