package com.mymealapp.ui.fragment.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import com.mymealapp.databinding.ItemFavoriteMealBinding
import com.mymealapp.model.data.Meal

class FavoriteAdapter(
    private val context: Context,
    private val itemClickListener: OnMealFavoriteClickListener
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private var mealFavoriteList = listOf<Meal>()

    fun setFavoriteMealList(mealFavoriteList: List<Meal>) {
        this.mealFavoriteList = mealFavoriteList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemFavoriteMealBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mealFavoriteList[position])
    }

    override fun getItemCount(): Int = mealFavoriteList.size

    inner class ViewHolder(private val binding: ItemFavoriteMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(meal: Meal) {
            Glide.with(context)
                .load(meal.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(binding.imgFavoriteMeal)

            binding.txtFavoriteNameMeal.text = meal.name

            binding.cvContainerItemFavorite.setOnClickListener {
                itemClickListener.onMealClick(meal)
            }

            binding.cvContainerItemFavorite.setOnLongClickListener {
                itemClickListener.onMealLongClick(meal)
                return@setOnLongClickListener true
            }
        }
    }

    interface OnMealFavoriteClickListener {
        fun onMealClick(meal: Meal)
        fun onMealLongClick(meal: Meal)
    }
}