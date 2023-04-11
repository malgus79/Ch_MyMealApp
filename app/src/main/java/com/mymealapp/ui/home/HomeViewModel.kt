package com.mymealapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.mymealapp.core.Resource
import com.mymealapp.domain.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: RepositoryInterface) : ViewModel() {

    fun fetchAllMealsInHome(categoryName: String) =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                emit(
                    Resource.Success(
                        Triple(
                            repo.getRandomMeal(),
                            repo.getPopularMeals(categoryName),
                            repo.getCategoriesMeal()
                        )
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
}