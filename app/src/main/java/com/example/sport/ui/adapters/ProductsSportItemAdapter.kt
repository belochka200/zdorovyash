package com.example.sport.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sport.R
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
            if (list[position].price != null) {
                textViewProductName.text = "${list[position].title}, ${list[position].price}"
                val image = when (list[position].title) {
                    "Футболка" -> R.drawable.football_shirt
                    "Шорты" -> R.drawable.football_shorts
                    "Бутсы" -> R.drawable.football_shoe
                    "Гольфы" -> R.drawable.football_socks
                    "Шорты спортивные" -> R.drawable.football_shorts
                    "Шапка" -> R.drawable.ski_beanie
                    "Комбинезон для лыжных гонок" -> R.drawable.ski_coat
                    "Ботинки лыжные" -> R.drawable.ski_boots
                    else -> R.drawable.football_shoe
                }
                textViewProductName.setCompoundDrawablesWithIntrinsicBounds(holder.itemView.context.getDrawable(image), null, null, null)
            }
            else{
                textViewProductName.text = list[position].title
                textViewProductName.setCompoundDrawablesWithIntrinsicBounds(holder.itemView.context.getDrawable(R.drawable.round_location_on_24), null, null, null)
            }
        }
    }
}