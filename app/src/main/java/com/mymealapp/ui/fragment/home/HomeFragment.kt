package com.mymealapp.ui.fragment.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import com.mymealapp.core.Resource
import com.mymealapp.core.hide
import com.mymealapp.core.show
import com.mymealapp.core.showToast
import com.mymealapp.databinding.FragmentHomeBinding
import com.mymealapp.model.data.Meal
import com.mymealapp.ui.fragment.home.adapter.CategoryAdapter
import com.mymealapp.ui.fragment.home.adapter.PopularAdapter
import com.mymealapp.viewmodel.HomeViewModel
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
                        if (swipeRefreshLayout.isRefreshing) {
                            progressBar.hide()
                        } else {
                            progressBar.show()
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
                        adapterCategory.setCategoryList(it.data.third.categories)
                    }
                    is Resource.Failure -> {
                        swipeRefreshLayout.isRefreshing = false
                        progressBar.hide()
                        showToast(getString(R.string.error_detail))
                    }
                }
            }
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
        Glide.with(binding.root.context)
            .load(item.image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.gradient)
            .centerCrop()
            .into(binding.imgRandomMeal)

        binding.cardViewHomeRandom.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToMealDetailFragment(
                    item
                )
            )
        }
    }

    private fun setupPopularMealsRecyclerView() {
        binding.rvPopularMeal.apply {
            adapter = adapterPopular
            layoutManager =
                GridLayoutManager(
                    requireContext(),
                    resources.getInteger(R.integer.columns_popular),
                    GridLayoutManager.HORIZONTAL,
                    false
                )
            itemAnimator = LandingAnimator().apply { addDuration = 300 }
            setHasFixedSize(true)
            show()
        }
    }

    private fun setupCategoriesRecyclerView() {
        binding.rvCategoriesMeal.apply {
            adapter = adapterCategory
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.columns_categories_in_home),
                StaggeredGridLayoutManager.VERTICAL
            )
            itemAnimator = LandingAnimator().apply { addDuration = 300 }
            setHasFixedSize(true)
            show()
        }
    }
}