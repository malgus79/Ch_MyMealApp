package com.mymealapp.ui.area

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mymealapp.R
import com.mymealapp.core.hide
import com.mymealapp.core.hideElements
import com.mymealapp.core.hideRefresh
import com.mymealapp.core.setRetryAction
import com.mymealapp.core.setupRecyclerView
import com.mymealapp.core.show
import com.mymealapp.core.showElements
import com.mymealapp.data.model.Area
import com.mymealapp.databinding.FragmentAreaBinding
import com.mymealapp.ui.area.adapter.AllAreasAdapter
import com.mymealapp.ui.area.adapter.MealByAreaAdapter
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
        setupArea()
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
                setupArea()
            }, 500)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setupArea() {
        viewModel.fetchArea()
        viewModel.areaState.observe(viewLifecycleOwner) {
            with(binding) {
                when (it) {
                    is AreaState.Loading -> {
                        hideElements(containerError.root, rvMealsByArea)
                        progressBar.apply {
                            if (swipeRefreshLayout.isRefreshing) hide() else show()
                        }
                    }

                    is AreaState.SuccessAreaList -> {
                        hideElements(containerError.root, progressBar)
                        showElements(rvAllAreas)
                        swipeRefreshLayout.hideRefresh()

                        if (it.areas.isEmpty()) {
                            hideElements(rvAllAreas)
                            return@observe
                        }

                        setupAllAreasListRecyclerView()
                        adapterAllAreas.setAllAreaList(it.areas.toMutableList())
                    }

                    is AreaState.SuccessByArea -> {
                        hideElements(containerError.root, progressBar)
                        showElements(rvMealsByArea, txtTitleArea)
                        swipeRefreshLayout.hideRefresh()

                        if (it.meal.isEmpty()) {
                            hideElements(rvMealsByArea)
                            return@observe
                        }

                        setupMealByAreaRecyclerView()
                        adapterMealByArea.setMealByAreaList(it.meal)
                    }

                    is AreaState.Failure -> {
                        hideElements(progressBar, rvAllAreas, rvMealsByArea, txtTitleArea)
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

    private fun setupMealByAreaRecyclerView() {
        binding.rvMealsByArea.setupRecyclerView(
            adapterMealByArea,
            resources.getInteger(R.integer.columns_meals_by_area),
            LandingAnimator(),
            true
        )
    }

    private fun btnRetry() {
        binding.containerError.btnRetry.setRetryAction {
            //setupAllAreasList()
            //setupMealByArea()
            setupArea()
        }
    }

    private fun setupAllAreasListRecyclerView() {
        binding.rvAllAreas.apply {
            adapter = adapterAllAreas
            layoutManager = GridLayoutManager(
                requireContext(),
                resources.getInteger(R.integer.columns_all_areas),
                GridLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
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
        setupArea()
    }
}