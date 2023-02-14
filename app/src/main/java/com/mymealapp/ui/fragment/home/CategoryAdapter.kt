package com.mymealapp.ui.fragment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import com.mymealapp.databinding.ItemInHomeCategoriesMealBinding
import com.mymealapp.model.data.Category

class CategoryAdapter(
    private val context: Context,
    var itemClickListener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categoryList = listOf<Category>()

    fun setCategoryList(categoryList: List<Category>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemInHomeCategoriesMealBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size

    inner class ViewHolder(private val binding: ItemInHomeCategoriesMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(category: Category) {
            Glide.with(context)
                .load(category.imageCategory)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(binding.imgCategoriesMeal)

            binding.txtCategoriesName.text = category.nameCategory

            binding.cvContainerItemCategories.setOnClickListener {
                itemClickListener.onCategoryClick(category.nameCategory.toString())
            }
        }
    }

    interface OnCategoryClickListener {
        fun onCategoryClick(categoryName: String)
    }
}