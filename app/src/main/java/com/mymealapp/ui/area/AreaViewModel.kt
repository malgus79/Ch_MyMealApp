package com.mymealapp.ui.area

import androidx.lifecycle.*
import com.mymealapp.core.Resource
import com.mymealapp.domain.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AreaViewModel @Inject constructor(private val repo: RepositoryInterface) : ViewModel() {

    private val mutableAllAreas = MutableLiveData<String>()
    val mutableAreaSelected: LiveData<String> = mutableAllAreas

    init {
        mutableAllAreas.value = "American"
    }

    fun setAllAreas(area: String) {
        mutableAllAreas.value = area
    }

    fun fetchMealByArea() =
        mutableAllAreas.distinctUntilChanged().switchMap {
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(Resource.Loading)
                try {
                    emit(Resource.Success(repo.getMealByArea(it)))
                } catch (e: Exception) {
                    emit(Resource.Failure(e))
                }
            }
        }

    fun fetchAllAreaList() =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                emit(Resource.Success(repo.getAllAreaList("list")))
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
}