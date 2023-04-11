package com.mymealapp.ui.favorite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mymealapp.core.loadImage
import com.mymealapp.data.model.Meal
import com.mymealapp.databinding.ItemFavoriteMealBinding

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
            with(binding) {
                loadImage(context, meal.image.toString(), imgFavoriteMeal)

                txtFavoriteNameMeal.text = meal.name

                cvContainerItemFavorite.setOnClickListener {
                    itemClickListener.onMealClick(meal)
                }

                cvContainerItemFavorite.setOnLongClickListener {
                    itemClickListener.onMealLongClick(meal)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    interface OnMealFavoriteClickListener {
        fun onMealClick(meal: Meal)
        fun onMealLongClick(meal: Meal)
    }
}