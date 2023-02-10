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