package com.mymealapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymealapp.domain.RepositoryImpl
import com.mymealapp.model.data.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMealViewModel @Inject constructor(private val repo: RepositoryImpl) : ViewModel() {

    fun saveOrDeleteFavoriteMeal(meal: Meal) {
        viewModelScope.launch {
            if (repo.isMealFavorite(meal)) {
                repo.deleteFavoriteMeal(meal)
            } else {
                repo.saveFavoriteMeal(meal)
            }
        }
    }

    suspend fun isMealFavorite(meal: Meal): Boolean? =
        repo.isMealFavorite(meal)
}