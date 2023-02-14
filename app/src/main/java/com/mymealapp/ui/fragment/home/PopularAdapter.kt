package com.mymealapp.ui.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import com.mymealapp.databinding.ItemPopularMealBinding
import com.mymealapp.model.data.MealByCategory

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
            Glide.with(binding.root.context)
                .load(mealPopular.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(binding.imgMealPopular)

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