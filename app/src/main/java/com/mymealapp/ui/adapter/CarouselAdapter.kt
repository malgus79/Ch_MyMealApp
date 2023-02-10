package com.mymealapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mymealapp.databinding.ItemCarouselBinding
import com.mymealapp.ui.fragment.carousel.MealCarousel

class CarouselAdapter(private var listCarousel: List<MealCarousel>) :
    RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemCarouselBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(meal: MealCarousel) {
            Glide.with(binding.root.context)
                .load(meal.image)
                .into(binding.imgCarousel)

            binding.txtCarouselNameMeal.text = meal.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(listCarousel[position])
    }

    override fun getItemCount(): Int = listCarousel.size
}