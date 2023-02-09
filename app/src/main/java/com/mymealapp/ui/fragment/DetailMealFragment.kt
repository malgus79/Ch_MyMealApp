package com.mymealapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import com.mymealapp.databinding.FragmentDetailMealBinding
import com.mymealapp.model.data.Meal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMealFragment : Fragment() {

    private lateinit var binding: FragmentDetailMealBinding

    private lateinit var meal: Meal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().let {
            DetailMealFragmentArgs.fromBundle(it).also { args ->
                meal = args.meal
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailMealBinding.inflate(inflater, container, false)

        showDetailMeal()

        return binding.root
    }

    private fun showDetailMeal() {
        Glide.with(binding.root.context)
            .load(meal.strMealThumb)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.gradient)
            .centerCrop()
            .into(binding.imgAppBar)

        val category = "${getString(R.string.category_detail)} ${meal.strCategory} "
        val area = "${getString(R.string.area_detail)} ${meal.strArea} "
        binding.toolbar.title = meal.strMeal
        binding.txtCategory.text = category
        binding.txtArea.text = area
        binding.txtInstructionDescriptions.text = meal.strInstructions
    }
}