package com.example.sport.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sport.data.models.ProductCard
import com.example.sport.databinding.ItemProductCardBinding

class ProductsSportItemAdapter(
    private val list: List<ProductCard>,
    private val onClickListener: (ProductCard) -> Unit
) :
    RecyclerView.Adapter<ProductsSportItemAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemProductCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onClickListener(list[position]) }
        holder.binding.apply {
            if (list[position].price != null)
                textViewProductName.text = "${list[position].title}, ${list[position].price}"
            else
                textViewProductName.text = list[position].title
        }
    }
}