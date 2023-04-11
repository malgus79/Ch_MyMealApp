package com.mymealapp.ui.area.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mymealapp.core.loadImage
import com.mymealapp.data.model.Meal
import com.mymealapp.databinding.ItemMealsByAreaBinding
import com.mymealapp.ui.area.AreaFragmentDirections

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
            with(binding) {
                loadImage(root.context, meal.image.toString(), imgMeal)

                txtTitleMeal.text = meal.name

                mcvContainer.setOnClickListener {
                    val action =
                        AreaFragmentDirections.actionAreaFragmentToMealDetailByCategoryFragment(
                            meal.idMeal
                        )
                    itemView.findNavController().navigate(action)
                }
            }
        }
    }
}