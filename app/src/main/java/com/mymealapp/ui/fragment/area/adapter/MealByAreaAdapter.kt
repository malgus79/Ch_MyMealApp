package com.mymealapp.ui.fragment.area.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import com.mymealapp.databinding.ItemMealsByAreaBinding
import com.mymealapp.model.data.Meal
import com.mymealapp.ui.fragment.area.AreaFragmentDirections

class MealByAreaAdapter :
    RecyclerView.Adapter<MealByAreaAdapter.VieHolder>() {

    private var mealByAreaList = listOf<Meal>()

    fun setMealByAreaList(mealByAreaList: List<Meal>) {
        this.mealByAreaList = mealByAreaList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        val itemBinding =
            ItemMealsByAreaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return VieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: VieHolder, position: Int) {
        holder.setData(mealByAreaList[position])
    }

    override fun getItemCount(): Int = mealByAreaList.size

    inner class VieHolder(private val binding: ItemMealsByAreaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(meal: Meal) {
            Glide.with(binding.root.context)
                .load(meal.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(binding.imgMeal)

            binding.txtTitleMeal.text = meal.name

            binding.mcvContainer.setOnClickListener {
                val action =
                    AreaFragmentDirections.actionAreaFragmentToMealDetailByCategoryFragment(
                        meal.idMeal
                    )
                this.itemView.findNavController().navigate(action)
            }
        }
    }
}