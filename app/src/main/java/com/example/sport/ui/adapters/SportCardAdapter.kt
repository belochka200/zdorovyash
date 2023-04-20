package com.example.sport.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sport.data.models.SportItem
import com.example.sport.databinding.ItemSportCardBinding

class SportCardAdapter(private val sportCardList: List<SportItem>) : RecyclerView.Adapter<SportCardAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemSportCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSportCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = sportCardList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            heading.text = sportCardList[position].title
            image.load(sportCardList[position].image)
        }
    }
}