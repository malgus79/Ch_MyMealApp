package com.mymealapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import com.mymealapp.databinding.ItemCategoriesMealBinding
import com.mymealapp.model.data.Category
import com.mymealapp.ui.fragment.CategoriesFragmentDirections

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
            Glide.with(binding.root.context)
                .load(categories.imageCategory)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(binding.imgCategoriesMeal)

            binding.txtCategoriesName.text = categories.nameCategory
            binding.txtCategoriesDescription.text = categories.descriptionCategory

            binding.cvContainerItemCategories.setOnClickListener {
                val action =
                    CategoriesFragmentDirections.actionCategoriesFragmentToMealByCategoryFragment(
                        categories.nameCategory.toString()
                    )
                this.itemView.findNavController().navigate(action)
            }
        }
    }
}