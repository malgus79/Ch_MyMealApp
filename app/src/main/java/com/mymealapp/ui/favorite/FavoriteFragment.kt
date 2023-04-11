package com.mymealapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mymealapp.R
import com.mymealapp.core.*
import com.mymealapp.data.model.Meal
import com.mymealapp.databinding.FragmentFavoriteBinding
import com.mymealapp.ui.favorite.adapter.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator

@AndroidEntryPoint
class FavoriteFragment : Fragment(), FavoriteAdapter.OnMealFavoriteClickListener {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapterFavorite: FavoriteAdapter

    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        adapterFavorite = FavoriteAdapter(requireContext(), this)

        setupFavoriteMeal()

        return binding.root
    }

    private fun setupFavoriteMeal() {
        viewModel.fetchFavoriteMeal().observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {
                        progressBar.show()
                    }
                    is Resource.Success -> {
                        progressBar.hide()
                        if (it.data.isEmpty()) {
                            rvFavoriteMeal.hide()
                            emptyContainer.root.show()
                            return@observe
                        }
                        adapterFavorite.setFavoriteMealList(it.data)
                        setupFavoriteRecyclerView()
                    }
                    is Resource.Failure -> {
                        progressBar.hide()
                        showToast(getString(R.string.error_detail) + it.exception)
                    }
                }
            }
        }
    }

    private fun setupFavoriteRecyclerView() {
        binding.rvFavoriteMeal.setupRecyclerView(
            adapterFavorite,
            resources.getInteger(R.integer.columns_favorite),
            LandingAnimator(),
            true
        )
    }

    override fun onMealClick(meal: Meal) {
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToMealDetailFragment(
                meal
            )
        )
    }

    override fun onMealLongClick(meal: Meal) {
        viewModel.deleteFavoriteMeal(meal)
        showToast(getString(R.string.removed_meal))
    }
}