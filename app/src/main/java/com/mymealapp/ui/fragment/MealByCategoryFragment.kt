package com.mymealapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.mymealapp.R
import com.mymealapp.core.Resource
import com.mymealapp.core.hide
import com.mymealapp.core.show
import com.mymealapp.core.showToast
import com.mymealapp.databinding.FragmentMealByCategoryBinding
import com.mymealapp.ui.adapter.MealByCategoryAdapter
import com.mymealapp.viewmodel.MealByCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator

@AndroidEntryPoint
class MealByCategoryFragment : Fragment() {

    private lateinit var binding: FragmentMealByCategoryBinding
    private lateinit var adapterMealByCategory: MealByCategoryAdapter
    private val args by navArgs<MealByCategoryFragmentArgs>()
    private val viewModel: MealByCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealByCategoryBinding.inflate(inflater, container, false)
        adapterMealByCategory = MealByCategoryAdapter(requireContext())
        setupMealByCategories()

        return binding.root
    }

    private fun setupMealByCategories() {
        viewModel.setMealByCategories(args.nameOfCategory)
        showMealsByCategories()
    }

    private fun showMealsByCategories() {
        viewModel.fetchMealByCategories.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.show()
                }
                is Resource.Success -> {
                    binding.progressBar.hide()
                    if (it.data.meals.isEmpty()) {
                        binding.rvMealByCategoryMeal.hide()
                        return@observe
                    }
                    setupMealByCategoryRecyclerView()
                    adapterMealByCategory.setMealByCategoryList(it.data.meals)
                }
                is Resource.Failure -> {
                    binding.progressBar.hide()
                    showToast(getString(R.string.error_detail) + it.exception)
                }
            }
        }
    }

    private fun setupMealByCategoryRecyclerView() {
        binding.rvMealByCategoryMeal.apply {
            adapter = adapterMealByCategory
            layoutManager =
                GridLayoutManager(
                    requireContext(),
                    resources.getInteger(R.integer.meals_by_category),
                    GridLayoutManager.VERTICAL,
                    false
                )
            itemAnimator = LandingAnimator().apply { addDuration = 300 }
            setHasFixedSize(true)
            show()
        }
    }
}