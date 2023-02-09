package com.mymealapp.viewmodel

import androidx.lifecycle.*
import com.mymealapp.core.Resource
import com.mymealapp.domain.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo: RepositoryImpl) : ViewModel() {

    private val mutableMealName = MutableLiveData<String>()

    fun setMeal(mealName: String) {
        mutableMealName.value = mealName
    }

    init {
        setMeal("Arrabiata")
    }

    val fetchMealList = mutableMealName.distinctUntilChanged().switchMap {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                //emit(Resource.Success(repo.getMealsByName(mealName)))
                repo.getMealsByName(it).collectLatest { emit(it) }
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
    }
}