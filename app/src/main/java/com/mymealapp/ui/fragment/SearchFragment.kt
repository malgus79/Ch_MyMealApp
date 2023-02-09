package com.mymealapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mymealapp.R
import com.mymealapp.core.Resource
import com.mymealapp.core.hide
import com.mymealapp.core.show
import com.mymealapp.core.showToast
import com.mymealapp.databinding.FragmentSearchBinding
import com.mymealapp.ui.adapter.SearchAdapter
import com.mymealapp.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapterSearch: SearchAdapter
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        adapterSearch = SearchAdapter()

        setupMealSearView()
        setupObserver()

        return binding.root
    }

    private fun setupObserver() {
        viewModel.fetchMealList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.emptyContainer.root.hide()
                }
                is Resource.Success -> {
                    if (it.data?.meals == null) {
                        binding.rvSearchMeal.hide()
                        binding.emptyContainer.root.show()
                        return@observe
                    }
                    setupRecyclerView()
                    adapterSearch.setMealSearchedList(it.data.meals)
                    binding.emptyContainer.root.hide()

                }
                is Resource.Failure -> {
                    showToast(getString(R.string.error_detail) + it.exception)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvSearchMeal.apply {
            adapter = adapterSearch
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.search_columns),
                StaggeredGridLayoutManager.VERTICAL
            )
            itemAnimator = LandingAnimator().apply { addDuration = 300 }
            setHasFixedSize(true)
            show()
        }
    }

    private fun setupMealSearView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.setMeal(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    viewModel.setMeal(newText)
                } else {
                    binding.rvSearchMeal.hide()
                }
                return true
            }
        })
    }
}