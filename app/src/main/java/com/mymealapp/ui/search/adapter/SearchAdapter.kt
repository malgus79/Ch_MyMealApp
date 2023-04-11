package com.mymealapp.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mymealapp.core.loadImage
import com.mymealapp.data.model.Meal
import com.mymealapp.databinding.ItemSearchMealBinding
import com.mymealapp.ui.search.SearchFragmentDirections

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.VieHolder>() {

    private var mealList = listOf<Meal>()

    fun setMealSearchedList(mealList: List<Meal>) {
        this.mealList = mealList
    }

    inner class VieHolder(private val binding: ItemSearchMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(meal: Meal) {
            with(binding) {
                loadImage(root.context, meal.image.toString(), imgSearchMeal)

                txtTitle.text = meal.name

                cvContainer.setOnClickListener {
                    val action = SearchFragmentDirections.actionSearchFragmentToMealDetailFragment(meal)
                    itemView.findNavController().navigate(action)
                }
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