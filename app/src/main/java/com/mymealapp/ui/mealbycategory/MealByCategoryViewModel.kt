package com.mymealapp.ui.mealbycategory

import androidx.lifecycle.*
import com.mymealapp.core.Resource
import com.mymealapp.domain.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MealByCategoryViewModel @Inject constructor(private val repo: RepositoryInterface) :
    ViewModel() {

    private val mutableMealsByCategories = MutableLiveData<String>()

    fun setMealByCategories(mealByCategory: String) {
        mutableMealsByCategories.value = mealByCategory
    }

    val fetchMealByCategories = mutableMealsByCategories.distinctUntilChanged().switchMap {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                emit(Resource.Success(repo.getMealByCategory(it)))
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
    }
}