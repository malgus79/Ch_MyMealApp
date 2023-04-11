package com.mymealapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mymealapp.core.loadImage
import com.mymealapp.data.model.MealByCategory
import com.mymealapp.databinding.ItemPopularMealBinding
import com.mymealapp.ui.home.HomeFragmentDirections

class PopularAdapter : RecyclerView.Adapter<PopularAdapter.VieHolder>() {

    private var mealPopularList = listOf<MealByCategory>()

    fun setMealPopularList(mealPopularList: List<MealByCategory>) {
        this.mealPopularList = mealPopularList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VieHolder {
        val itemBinding =
            ItemPopularMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: VieHolder, position: Int) {
        holder.setData(mealPopularList[position])
    }

    override fun getItemCount(): Int = mealPopularList.size

    inner class VieHolder(private val binding: ItemPopularMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(mealPopular: MealByCategory) {
            loadImage(binding.root.context, mealPopular.image.toString(), binding.imgMealPopular)

            binding.txtTitlePopular.text = mealPopular.name

            binding.cvImgPopular.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToMealDetailByCategoryFragment(
                        mealPopular.idMeal.toString()
                    )
                this.itemView.findNavController().navigate(action)
            }
        }
    }
}