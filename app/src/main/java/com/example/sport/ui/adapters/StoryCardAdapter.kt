package com.example.sport.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sport.data.models.Story
import com.example.sport.databinding.ItemStoryBinding

class StoryCardAdapter(private val listSportCards: List<Story>) : RecyclerView.Adapter<StoryCardAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listSportCards.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            heading.text = listSportCards[position].heading
            image.load(listSportCards[position]) { crossfade(500) }
        }
    }
}