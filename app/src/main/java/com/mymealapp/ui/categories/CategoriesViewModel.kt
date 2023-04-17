package com.mymealapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mymealapp.domain.RepositoryInterface
import com.mymealapp.domain.common.fold
import com.mymealapp.domain.common.toError
import com.mymealapp.domain.common.validateHttpErrorCode
import com.mymealapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val repo: RepositoryInterface) :
    BaseViewModel() {

    private val _categoriesState = MutableLiveData<CategoriesState>()
    val categoriesState: LiveData<CategoriesState> = _categoriesState

    fun fetchCategoriesMeal() {
        _categoriesState.value = CategoriesState.Loading
        viewModelScope.launch {
            repo.getCategoriesMeal().fold(
                onSuccess = {
                    _categoriesState.postValue(CategoriesState.Success(it))
                },
                onError = { code, _ ->
                    val error = handleError(code.validateHttpErrorCode())
                    _categoriesState.postValue(CategoriesState.Failure(error))
                },
                onException = { exception ->
                    val error = handleError(exception.toError())
                    _categoriesState.postValue(CategoriesState.Failure(error))
                }
            )
        }
    }
}