package com.mymealapp.ui.mealbycategory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mymealapp.core.loadImage
import com.mymealapp.data.model.MealByCategory
import com.mymealapp.databinding.ItemMealByCategoryBinding
import com.mymealapp.ui.mealbycategory.MealByCategoryFragmentDirections

class MealByCategoryAdapter : RecyclerView.Adapter<MealByCategoryAdapter.ViewHolder>() {

    private var mealByCategoryList = listOf<MealByCategory>()

    fun setMealByCategoryList(mealByCategoryList: List<MealByCategory>) {
        this.mealByCategoryList = mealByCategoryList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemMealByCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mealByCategoryList[position])
    }

    override fun getItemCount(): Int = mealByCategoryList.size

    inner class ViewHolder(private val binding: ItemMealByCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(mealByCategory: MealByCategory) {
            with(binding) {
                loadImage(
                    root.context,
                    mealByCategory.image.toString(),
                    imgMealByCategoryMeal
                )

                txtMealByCategoryName.text = mealByCategory.name

                cvContainerItemMealByCategory.setOnClickListener {
                    val action =
                        MealByCategoryFragmentDirections.actionMealByCategoryFragmentToMealDetailByCategoryFragment(
                            mealByCategory.idMeal.toString()
                        )
                    itemView.findNavController().navigate(action)
                }
            }
        }
    }
}