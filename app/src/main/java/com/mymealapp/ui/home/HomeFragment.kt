package com.mymealapp.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mymealapp.R
import com.mymealapp.core.Resource
import com.mymealapp.core.hide
import com.mymealapp.core.hideElements
import com.mymealapp.core.hideRefresh
import com.mymealapp.core.loadImage
import com.mymealapp.core.setRetryAction
import com.mymealapp.core.setupRecyclerView
import com.mymealapp.core.show
import com.mymealapp.core.showElements
import com.mymealapp.data.model.Meal
import com.mymealapp.databinding.FragmentHomeBinding
import com.mymealapp.domain.common.getSuccess
import com.mymealapp.ui.home.adapter.CategoryAdapter
import com.mymealapp.ui.home.adapter.PopularAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val adapterPopular: PopularAdapter = PopularAdapter()
    private val adapterCategory: CategoryAdapter = CategoryAdapter()

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        swipeRefresh()
        setupAllMealsInHome()

        return binding.root
    }

    private fun swipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.setColorSchemeResources(
                R.color.purple_700,
                R.color.red_theme
            )
            binding.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(requireContext(), R.color.grey_loading)
            )
            Handler(Looper.getMainLooper()).postDelayed({
                setupAllMealsInHome()
            }, 500)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setupAllMealsInHome() {

        val categoriesNames = arrayOf(
            "Beef", "Breakfast", "Chicken", "Dessert", "Lamb",
            "Miscellaneous", "Pasta", "Pork", "Seafood", "Side", "Starter", "Vegan", "Vegetarian"
        )
        val randomCategory = categoriesNames[Random.nextInt(categoriesNames.size)]
        val titlePopularMeals = "${getString(R.string.title_popular_meals)} $randomCategory "

        viewModel.fetchAllMealsInHome(randomCategory).observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {
                        hideElements(containerError.root)
                        progressBar.apply {
                            if (swipeRefreshLayout.isRefreshing) hide() else show()
                        }
                    }

                    is Resource.Success -> {
                        swipeRefreshLayout.isRefreshing = false
                        progressBar.hide()
                        showTitlesInHome(titlePopularMeals)

                        setupShowRandomMeals(it.data.first.meals.first())

                        setupPopularMealsRecyclerView()
                        adapterPopular.setMealPopularList(it.data.second.meals)

                        setupCategoriesRecyclerView()
                        it.data.third.getSuccess()
                            ?.let { it1 -> adapterCategory.setCategoryList(it1) }
                    }

                    is Resource.Failure -> {
                        swipeRefreshLayout.hideRefresh()
                        hideElements(
                            progressBar,
                            rvPopularMeal,
                            rvCategoriesMeal,
                            cardViewHomeRandom,
                            txtTitleRandom,
                            txtTitleCategories,
                            txtTitlePopular
                        )
                        showElements(containerError.root)

                        val errorMessage = getString(R.string.not_found_error)
                        containerError.textView.text = errorMessage

                        btnRetry()

                    }
                }
            }
        }
    }

    private fun btnRetry() {
        binding.containerError.btnRetry.setRetryAction {
            setupAllMealsInHome()
        }
    }

    private fun showTitlesInHome(titlePopularMeals: String) {
        with(binding) {
            txtTitleRandom.show()
            cardViewHomeRandom.show()
            txtTitlePopular.text = titlePopularMeals
            txtTitleCategories.show()
        }
    }

    private fun setupShowRandomMeals(item: Meal) {
        loadImage(binding.root.context, item.image.toString(), binding.imgRandomMeal)

        binding.cardViewHomeRandom.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToMealDetailFragment(
                    item
                )
            )
        }
    }

    private fun setupPopularMealsRecyclerView() {
        binding.rvPopularMeal.setupRecyclerView(
            adapterPopular,
            resources.getInteger(R.integer.columns_popular),
            LandingAnimator(),
            false
        )
    }

    private fun setupCategoriesRecyclerView() {
        binding.rvCategoriesMeal.setupRecyclerView(
            adapterCategory,
            resources.getInteger(R.integer.columns_categories_in_home),
            LandingAnimator(),
            true
        )
    }
}