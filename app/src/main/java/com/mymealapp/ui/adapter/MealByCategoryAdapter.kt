package com.mymealapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import com.mymealapp.databinding.ItemMealByCategoryBinding
import com.mymealapp.model.data.MealByCategory

class MealByCategoryAdapter(
    private val context: Context,
    var itemClickListener: OnMealByCategoryClickListener
) : RecyclerView.Adapter<MealByCategoryAdapter.ViewHolder>() {

    private var mealByCategoryList = listOf<MealByCategory>()

    fun setMealByCategoryList(mealByCategoryList: List<MealByCategory>) {
        this.mealByCategoryList = mealByCategoryList
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemMealByCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mealByCategoryList[position])
    }

    override fun getItemCount(): Int = mealByCategoryList.size

    inner class ViewHolder(private val binding: ItemMealByCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(mealByCategory: MealByCategory) {
            Glide.with(context)
                .load(mealByCategory.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(binding.imgMealByCategoryMeal)

            binding.txtMealByCategoryName.text = mealByCategory.name

            binding.cvContainerItemMealByCategory.setOnClickListener {
                itemClickListener.onMealByCategoryClick(mealByCategory)
            }
        }
    }

    interface OnMealByCategoryClickListener {
        fun onMealByCategoryClick(mealByCategory: MealByCategory)
    }
}