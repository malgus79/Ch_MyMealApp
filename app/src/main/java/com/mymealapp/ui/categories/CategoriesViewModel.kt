package com.mymealapp.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.mymealapp.core.Resource
import com.mymealapp.domain.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val repo: RepositoryInterface) : ViewModel() {

    fun fetchCategoriesMeal() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(repo.getCategoriesMeal()))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}