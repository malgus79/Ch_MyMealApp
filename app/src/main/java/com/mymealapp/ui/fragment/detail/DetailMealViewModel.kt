package com.mymealapp.ui.fragment.detail

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymealapp.domain.RepositoryImpl
import com.mymealapp.model.data.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    suspend fun isMealFavorite(meal: Meal): Boolean =
        repo.isMealFavorite(meal)

    /*------------------------------ Detail meal by category ------------------------------*/
    private var _mealDetailMutableLiveData = MutableLiveData<Meal>()
    val mealDetailLiveData: LiveData<Meal> = _mealDetailMutableLiveData

    fun fetchMealDetailsById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repo.getMealDetailsById(id)

                if (result.meals.isNotEmpty()) {
                    _mealDetailMutableLiveData.postValue(result.meals[0])
                } else {
                    Log.d(TAG, "fetchMealDetailsById: notSuccessful")
                }
            } catch (e: Exception) {
                Log.d(TAG, "fetchMealDetailsById: ${e.message}")
            }
        }
    }
}