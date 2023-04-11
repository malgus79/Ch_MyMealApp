package com.mymealapp.ui.search

import androidx.lifecycle.*
import com.mymealapp.core.Resource
import com.mymealapp.domain.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo: RepositoryInterface) : ViewModel() {

    private val _mutableMealName = MutableLiveData<String>()
    val mutableMealName: LiveData<String> = _mutableMealName

    fun setMeal(mealName: String) {
        _mutableMealName.value = mealName
    }

//    init {
//        setMeal("Arrabiata")
//    }

    val fetchMealList = _mutableMealName.distinctUntilChanged().switchMap {
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