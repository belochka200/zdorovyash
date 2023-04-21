package com.example.sport.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sport.databinding.ItemWhatIsUseBinding

class WhatIsUseAdapter(private val list: List<String>) : RecyclerView.Adapter<WhatIsUseAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemWhatIsUseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWhatIsUseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.checkbox.text = list[position]
    }
}