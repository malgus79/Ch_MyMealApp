package com.mymealapp.ui.fragment.search

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
import com.mymealapp.databinding.FragmentSearchBinding
import com.mymealapp.ui.fragment.search.adapter.SearchAdapter
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
        binding.searchView.setQuery("", true)

        setupMealSearchView()
        showMealSearched()
        initMealSearched()

        return binding.root
    }

    private fun showMealSearched() {
        viewModel.fetchMealList.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {
                        emptyContainer.root.hide()
                    }
                    is Resource.Success -> {
                        if (it.data.isEmpty()) {
                            rvSearchMeal.hide()
                            emptyContainer.root.show()
                            return@observe
                        }
                        setupRecyclerView()
                        adapterSearch.setMealSearchedList(it.data)
                        emptyContainer.root.hide()

                    }
                    is Resource.Failure -> {
                        Log.d(TAG, "Error: " + it.exception)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvSearchMeal.apply {
            adapter = adapterSearch
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.columns_search),
                StaggeredGridLayoutManager.VERTICAL
            )
            itemAnimator = LandingAnimator().apply { addDuration = 300 }
            setHasFixedSize(true)
            show()
        }
    }

    private fun setupMealSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.setMeal(query)
                }
                return true
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

    private fun initMealSearched() {
        viewModel.mutableMealName.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.searchView.setQuery(it, false)
            }
        }
    }
}