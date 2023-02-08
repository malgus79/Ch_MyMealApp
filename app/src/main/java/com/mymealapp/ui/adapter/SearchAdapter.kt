package com.mymealapp.ui.adapter

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
import com.mymealapp.ui.fragment.SearchFragmentDirections

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.VieHolder>() {

    private lateinit var mealList: List<Meal>

    inner class VieHolder(private val binding: ItemSearchMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(meal: Meal) {
            Glide.with(binding.root.context)
                .load(meal.strMealThumb)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(binding.imgSearchMeal)

            binding.mcvSearchImage.setOnClickListener {
                val action = SearchFragmentDirections.actionSearchFragmentToMealDetailFragment(meal)
                this.itemView.findNavController().navigate(action)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        val binding =
            ItemSearchMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VieHolder(binding)
    }

    override fun onBindViewHolder(holder: VieHolder, position: Int) {
        holder.setData(mealList[position])
    }

    override fun getItemCount(): Int = mealList.size
}