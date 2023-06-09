package com.example.sport.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import com.example.sport.data.models.sport.SportItem
import com.example.sport.databinding.ItemSportCardBinding

class SportCardAdapter(
    private val sportCardList: List<SportItem>,
    private val imageLoader: ImageLoader,
    private val onItemClickListener: (id: Int) -> Unit
) : RecyclerView.Adapter<SportCardAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemSportCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSportCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = sportCardList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onItemClickListener(sportCardList[position].id) }
        holder.binding.apply {
            heading.text = sportCardList[position].title
            image.load(sportCardList[position].image, imageLoader = imageLoader) { crossfade(500) }
        }
    }
}