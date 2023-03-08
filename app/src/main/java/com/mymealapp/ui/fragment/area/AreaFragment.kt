package com.mymealapp.ui.fragment.area

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

        setupMealByArea()
        setupAllAreasList()


        return binding.root
    }

    private fun setupMealByArea() {
        viewModel.fetchAllAreas().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.show()
                    binding.rvMealsByArea.hide()
                    binding.txtTitleArea.hide()
                }
                is Resource.Success -> {
                    binding.progressBar.hide()
                    if (it.data.meals.isEmpty()) {
                        binding.rvMealsByArea.hide()
                        return@observe
                    }
                    setupMealByAreaRecyclerView()
                    adapterMealByArea.setMealByAreaList(it.data.meals)
                    binding.txtTitleArea.show()
                }
                is Resource.Failure -> {
                    binding.progressBar.hide()
                    showToast(getString(R.string.error_detail))
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
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.show()
                    binding.rvAllAreas.hide()
                }
                is Resource.Success -> {
                    binding.progressBar.hide()
                    if (it.data.meals.isEmpty()) {
                        binding.rvAllAreas.hide()
                        return@observe
                    }
                    setupAreaRecyclerView()
                    adapterAllAreas.setAllAreaList(it.data.meals.toMutableList())
                }
                is Resource.Failure -> {
                    binding.progressBar.hide()
                    showToast(getString(R.string.error_detail))
                }
            }
        }
    }

    private fun setupAreaRecyclerView() {
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

    override fun onAreaClick(area: String) {
        viewModel.setAllAreas(area)
        binding.txtTitleArea.text = area
    }
}