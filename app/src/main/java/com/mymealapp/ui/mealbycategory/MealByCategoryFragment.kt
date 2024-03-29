package com.mymealapp.ui.mealbycategory

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mymealapp.R
import com.mymealapp.core.*
import com.mymealapp.databinding.FragmentMealByCategoryBinding
import com.mymealapp.ui.mealbycategory.adapter.MealByCategoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator

@AndroidEntryPoint
class MealByCategoryFragment : Fragment() {

    private lateinit var binding: FragmentMealByCategoryBinding
    private val adapterMealByCategory: MealByCategoryAdapter = MealByCategoryAdapter()
    private val args by navArgs<MealByCategoryFragmentArgs>()
    private val viewModel: MealByCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealByCategoryBinding.inflate(inflater, container, false)

        setupMealByCategories()

        return binding.root
    }

    private fun setupMealByCategories() {
        viewModel.setMealByCategories(args.nameOfCategory)
        showDataMealsByCategories()
    }

    private fun showDataMealsByCategories() {
        viewModel.fetchMealByCategories.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {
                        progressBar.show()
                    }
                    is Resource.Success -> {
                        progressBar.hide()
                        if (it.data.meals.isEmpty()) {
                            rvMealByCategoryMeal.hide()
                            return@observe
                        }
                        setupMealByCategoryRecyclerView()
                        adapterMealByCategory.setMealByCategoryList(it.data.meals)
                        countMealByCategories(it.data.meals.size)
                        showTitleMealByCategory()
                    }
                    is Resource.Failure -> {
                        progressBar.hide()
                        showToast(getString(R.string.error_detail))
                    }
                }
            }
        }
    }

    private fun setupMealByCategoryRecyclerView() {
        binding.rvMealByCategoryMeal.setupRecyclerView(
            adapterMealByCategory,
            resources.getInteger(R.integer.columns_meals_by_category),
            LandingAnimator(),
            true
        )
    }

    private fun showTitleMealByCategory() {
        val mealByCategoryTitle =
            //"${getString(R.string.meal_by_category_title)} " +
            "${args.nameOfCategory} "
        binding.txtMealByCategoryTitle.text = mealByCategoryTitle
    }

    @SuppressLint("SetTextI18n")
    private fun countMealByCategories(size: Int) {
        binding.txtCountOfMealByCategory.text = "($size)"
    }
}