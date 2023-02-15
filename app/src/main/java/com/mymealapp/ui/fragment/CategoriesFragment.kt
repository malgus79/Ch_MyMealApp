package com.mymealapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mymealapp.R
import com.mymealapp.core.Resource
import com.mymealapp.core.hide
import com.mymealapp.core.show
import com.mymealapp.core.showToast
import com.mymealapp.databinding.FragmentCategoriesBinding
import com.mymealapp.ui.adapter.CategoriesAdapter
import com.mymealapp.viewmodel.CategoriesViewModel
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
        setupCategoriesMeal()

        return binding.root
    }

    private fun setupCategoriesMeal() {
        viewModel.fetchCategoriesMeal().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.show()
                }
                is Resource.Success -> {
                    binding.progressBar.hide()
                    if (it.data.categories.isEmpty()) {
                        binding.rvCategoriesMeal.hide()
                        return@observe
                    }
                    adapterCategories.setCategoriesList(it.data.categories)
                    setupCategoriesRecyclerView()
                }
                is Resource.Failure -> {
                    binding.progressBar.hide()
                    showToast(getString(R.string.error_detail))
                }
            }
        }
    }

    private fun setupCategoriesRecyclerView() {
        binding.rvCategoriesMeal.apply {
            adapter = adapterCategories
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.columns_categories),
                StaggeredGridLayoutManager.VERTICAL
            )
            itemAnimator = LandingAnimator().apply { addDuration = 300 }
            setHasFixedSize(true)
            show()
        }
    }
}