package com.mymealapp.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mymealapp.R
import com.mymealapp.core.Resource
import com.mymealapp.core.hide
import com.mymealapp.core.show
import com.mymealapp.databinding.FragmentHomeBinding
import com.mymealapp.ui.fragment.carousel.CarouselAdapter
import com.mymealapp.ui.fragment.carousel.MealCarousel
import com.mymealapp.ui.fragment.carousel.MealCarouselProvider
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var adapterCarousel: CarouselAdapter
    private val adapterPopular: PopularAdapter = PopularAdapter()

    private val mealRandomMutableList: MutableList<MealCarousel> =
        MealCarouselProvider.listRandomMeals.toMutableList()

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupPopularMeals()
        setupCarousel()

        return binding.root
    }

    private fun setupCarousel() {
        adapterCarousel = CarouselAdapter(mealRandomMutableList)
        binding.rvCarousel.apply {
            this.adapter = adapterCarousel
            set3DItem(true)
            setAlpha(true)
        }
    }

    private fun setupPopularMeals() {
        val categoriesNames = arrayOf(
            "Beef", "Breakfast", "Chicken", "Dessert", "Goat", "Lamb",
            "Miscellaneous", "Pasta", "Pork", "Seafood", "Side", "Starter", "Vegan", "Vegetarian"
        )
        val randomCategory = categoriesNames[Random.nextInt(categoriesNames.size)]
        val titlePopularMeals = "${getString(R.string.title_popular_meals)} $randomCategory "

        viewModel.fetchPopularMeals(randomCategory).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.show()
                }
                is Resource.Success -> {
                    binding.progressBar.hide()
                    if (it.data.meals.isEmpty()) {
                        binding.rvPopularMeal.hide()
                        return@observe
                    }
                    setupPopularMealsRecyclerView()
                    adapterPopular.setMealPopularList(it.data.meals)
                    binding.txtTitlePopular.text = titlePopularMeals
                }
                is Resource.Failure -> {
                    binding.progressBar.hide()
                }
            }
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
}