package com.mymealapp.ui.fragment.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import com.mymealapp.databinding.ItemSearchMealBinding
import com.mymealapp.model.data.Meal

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.VieHolder>() {

    private var mealList = listOf<Meal>()

    fun setMealSearchedList(mealList: List<Meal>) {
        this.mealList = mealList
    }

    inner class VieHolder(private val binding: ItemSearchMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(meal: Meal) {
            Glide.with(binding.root.context)
                .load(meal.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(binding.imgSearchMeal)

            binding.txtTitle.text = meal.name

            binding.cvContainer.setOnClickListener {
                val action = SearchFragmentDirections.actionSearchFragmentToMealDetailFragment(meal)
                this.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        val itemBinding =
            ItemSearchMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: VieHolder, position: Int) {
        holder.setData(mealList[position])
    }

    override fun getItemCount(): Int = mealList.size
}