package com.mymealapp.ui.categories

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mymealapp.R
import com.mymealapp.core.*
import com.mymealapp.databinding.FragmentCategoriesBinding
import com.mymealapp.ui.categories.adapter.CategoriesAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var adapterCategories: CategoriesAdapter
    private val viewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        adapterCategories = CategoriesAdapter()

        swipeRefresh()
        setupCategoriesMeal()

        return binding.root
    }

    private fun swipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.setColorSchemeResources(
                R.color.purple_700,
                R.color.red_theme,
            )
            binding.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(requireContext(), R.color.grey_loading)
            )
            Handler(Looper.getMainLooper()).postDelayed({
                setupCategoriesMeal()
            }, 500)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setupCategoriesMeal() {
        viewModel.fetchCategoriesMeal()
        viewModel.categoriesState.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is CategoriesState.Loading -> {
                        hideElements(containerError.root)
                        progressBar.apply {
                            if (swipeRefreshLayout.isRefreshing) hide() else show()
                        }
                    }
                    is CategoriesState.Success -> {
                        hideElements(containerError.root, progressBar)
                        swipeRefreshLayout.hideRefresh()

                        if (it.categories.isEmpty()) {
                            hideElements(rvCategoriesMeal)
                            return@observe
                        }

                        adapterCategories.setCategoriesList(it.categories)
                        setupCategoriesRecyclerView()
                    }
                    is CategoriesState.Failure -> {
                        hideElements(progressBar, rvCategoriesMeal)
                        showElements(containerError.root)
                        swipeRefreshLayout.hideRefresh()

                        btnRetry()

                        if (it.error != null) {
                            val errorMessage = getString(it.error.errorMessage)
                            containerError.textView.text = errorMessage
                        }
                    }
                }
            }
        }
    }

    private fun btnRetry() {
        binding.containerError.btnRetry.setRetryAction {
            setupCategoriesMeal()
        }
    }

    private fun setupCategoriesRecyclerView() {
        binding.rvCategoriesMeal.setupRecyclerView(
            adapterCategories,
            resources.getInteger(R.integer.columns_categories),
            LandingAnimator(),
            true
        )
    }
}