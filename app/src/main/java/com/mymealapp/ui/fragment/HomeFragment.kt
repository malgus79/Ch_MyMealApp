package com.mymealapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mymealapp.databinding.FragmentHomeBinding
import com.mymealapp.ui.fragment.carousel.MealCarousel
import com.mymealapp.ui.fragment.carousel.MealCarouselProvider
import com.mymealapp.ui.adapter.CarouselAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterCarousel: CarouselAdapter
    private val mealRandomMutableList: MutableList<MealCarousel> =
        MealCarouselProvider.listRandomMeals.toMutableList()
//    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

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
}


/**  Option Carousel:

private fun setupRandomMeals() {
viewModel.fetchRandomMeal().observe(viewLifecycleOwner) {
when (it) {
is Resource.Loading -> {
binding.progressBar.show()
}
is Resource.Success -> {
binding.progressBar.hide()
if (it.data.meals.isEmpty()) {
binding.imgRandomMeal.hide()
return@observe
}
setupShowRandomMeals(it.data.meals.first())
}
is Resource.Failure -> {
binding.progressBar.hide()
showToast(getString(R.string.error_detail) + it.exception)
}
}
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

binding.cvHomeRandom.setOnClickListener {
findNavController().navigate(
HomeFragmentDirections.actionHomeFragmentToMealDetailFragment(item)
)
}
}

 **/
