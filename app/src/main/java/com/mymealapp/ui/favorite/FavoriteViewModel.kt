package com.mymealapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mymealapp.core.Resource
import com.mymealapp.data.model.Meal
import com.mymealapp.domain.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repo: RepositoryInterface) : ViewModel() {

    fun fetchFavoriteMeal() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emitSource(repo.getFavoritesMeals().map { Resource.Success(it) })
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun deleteFavoriteMeal(meal: Meal) {
        viewModelScope.launch {
            repo.deleteFavoriteMeal(meal)
        }
    }

}