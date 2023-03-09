package com.mymealapp.ui.fragment.area

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mymealapp.R
import com.mymealapp.core.Resource
import com.mymealapp.core.hide
import com.mymealapp.core.show
import com.mymealapp.core.showToast
import com.mymealapp.databinding.FragmentAreaBinding
import com.mymealapp.model.data.Area
import com.mymealapp.ui.fragment.area.adapter.AllAreasAdapter
import com.mymealapp.ui.fragment.area.adapter.MealByAreaAdapter
import com.mymealapp.viewmodel.AreaViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.LandingAnimator

@AndroidEntryPoint
class AreaFragment : Fragment(), AllAreasAdapter.OnAreaClickListener {

    private lateinit var binding: FragmentAreaBinding

    private var areasMutableList: MutableList<Area> = mutableListOf()

    private var adapterAllAreas: AllAreasAdapter =
        AllAreasAdapter(areasMutableList, this)

    private var adapterMealByArea: MealByAreaAdapter =
        MealByAreaAdapter()

    private val viewModel: AreaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAreaBinding.inflate(inflater, container, false)

        swipeRefresh()
        setupMealByArea()
        setupAllAreasList()
        showTitleSelectedArea()

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
                setupMealByArea()
                setupAllAreasList()
            }, 500)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setupMealByArea() {
        viewModel.fetchMealByArea().observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {
                        rvMealsByArea.hide()
                        txtTitleArea.hide()

                        if (swipeRefreshLayout.isRefreshing) {
                            progressBar.hide()
                        } else {
                            progressBar.show()
                        }
                    }
                    is Resource.Success -> {
                        progressBar.hide()
                        swipeRefreshLayout.isRefreshing = false
                        if (it.data.meals.isEmpty()) {
                            binding.rvMealsByArea.hide()
                            return@observe
                        }
                        setupMealByAreaRecyclerView()
                        adapterMealByArea.setMealByAreaList(it.data.meals)
                        txtTitleArea.show()
                    }
                    is Resource.Failure -> {
                        progressBar.hide()
                        swipeRefreshLayout.isRefreshing = false
                        showToast(getString(R.string.error_detail))
                    }
                }
            }
        }
    }

    private fun setupMealByAreaRecyclerView() {
        binding.rvMealsByArea.apply {
            adapter = adapterMealByArea
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.columns_meals_by_area),
                StaggeredGridLayoutManager.VERTICAL
            )
            itemAnimator = LandingAnimator().apply { addDuration = 300 }
            setHasFixedSize(true)
            show()
        }
    }

    private fun setupAllAreasList() {
        viewModel.fetchAllAreaList().observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is Resource.Loading -> {
                        rvAllAreas.hide()

                        if (swipeRefreshLayout.isRefreshing) {
                            progressBar.hide()
                        } else {
                            progressBar.show()
                        }
                    }
                    is Resource.Success -> {
                        progressBar.hide()
                        if (it.data.meals.isEmpty()) {
                            rvAllAreas.hide()
                            return@observe
                        }
                        setupAllAreasListRecyclerView()
                        adapterAllAreas.setAllAreaList(it.data.meals.toMutableList())
                    }
                    is Resource.Failure -> {
                        progressBar.hide()
                        showToast(getString(R.string.error_detail))
                    }
                }
            }
        }
    }

    private fun setupAllAreasListRecyclerView() {
        binding.rvAllAreas.apply {
            adapter = adapterAllAreas
            layoutManager = StaggeredGridLayoutManager(
                resources.getInteger(R.integer.columns_all_areas),
                StaggeredGridLayoutManager.VERTICAL
            )
            itemAnimator = LandingAnimator().apply { addDuration = 300 }
            setHasFixedSize(true)
            show()
        }
    }

    private fun showTitleSelectedArea() {
        viewModel.mutableAreaSelected.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.txtTitleArea.text = it
            }
        }
    }

    override fun onAreaClick(area: String) {
        viewModel.setAllAreas(area)
        binding.txtTitleArea.text = area
    }
}