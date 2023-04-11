package com.mymealapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mymealapp.core.loadImage
import com.mymealapp.data.model.Category
import com.mymealapp.databinding.ItemInHomeCategoriesMealBinding
import com.mymealapp.ui.home.HomeFragmentDirections

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categoryList = listOf<Category>()

    fun setCategoryList(categoryList: List<Category>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemInHomeCategoriesMealBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size

    inner class ViewHolder(private val binding: ItemInHomeCategoriesMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(category: Category) {
            loadImage(
                binding.root.context,
                category.imageCategory.toString(),
                binding.imgCategoriesMeal
            )

            binding.txtCategoriesName.text = category.nameCategory

            binding.cvContainerItemCategories.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToMealByCategoryFragment(
                        category.nameCategory.toString()
                    )
                this.itemView.findNavController().navigate(action)
            }
        }
    }
}