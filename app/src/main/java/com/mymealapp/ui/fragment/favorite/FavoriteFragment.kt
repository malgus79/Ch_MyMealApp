package com.mymealapp.ui.fragment.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mymealapp.R
import com.mymealapp.core.Resource
import com.mymealapp.core.hide
import com.mymealapp.core.show
import com.mymealapp.core.showToast
import com.mymealapp.databinding.FragmentFavoriteBinding
import com.mymealapp.model.data.Meal
import com.mymealapp.ui.fragment.favorite.adapter.FavoriteAdapter
import com.mymealapp.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
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
        binding.rvFavoriteMeal.apply {
            adapter = ScaleInAnimationAdapter(adapterFavorite)
            itemAnimator = LandingAnimator().apply { addDuration = 300 }
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.columns_favorite),
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            show()
        }
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