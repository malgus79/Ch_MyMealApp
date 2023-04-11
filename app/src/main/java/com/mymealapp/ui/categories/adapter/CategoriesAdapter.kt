package com.mymealapp.ui.categories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mymealapp.core.loadImage
import com.mymealapp.data.model.Category
import com.mymealapp.databinding.ItemCategoriesMealBinding
import com.mymealapp.ui.categories.CategoriesFragmentDirections

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var categoriesList = listOf<Category>()

    fun setCategoriesList(categoriesList: List<Category>) {
        this.categoriesList = categoriesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemCategoriesMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(categoriesList[position])
    }

    override fun getItemCount(): Int = categoriesList.size

    inner class ViewHolder(private val binding: ItemCategoriesMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(categories: Category) {
            with(binding) {
                loadImage(
                    root.context,
                    categories.imageCategory.toString(),
                    imgCategoriesMeal
                )

                txtCategoriesName.text = categories.nameCategory
                txtCategoriesDescription.text = categories.descriptionCategory

                cvContainerItemCategories.setOnClickListener {
                    val action =
                        CategoriesFragmentDirections.actionCategoriesFragmentToMealByCategoryFragment(
                            categories.nameCategory.toString()
                        )
                    itemView.findNavController().navigate(action)
                }
            }
        }
    }
}