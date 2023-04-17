package com.mymealapp.ui.area

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
class AreaViewModel @Inject constructor(private val repo: RepositoryInterface) : BaseViewModel() {

    private val mutableAllAreas = MutableLiveData<String>()
    val mutableAreaSelected: LiveData<String> = mutableAllAreas

    private val _areaState = MutableLiveData<AreaState>()
    val areaState: LiveData<AreaState> = _areaState

    init {
        mutableAllAreas.value = "American"
    }

    fun setAllAreas(area: String) {
        mutableAllAreas.value = area
    }

    fun fetchArea() {
        _areaState.value = AreaState.Loading
        viewModelScope.launch {
            repo.getAllAreaList("list").fold(
                onSuccess = { areaList ->
                    _areaState.postValue(AreaState.SuccessAreaList(areaList))
                    val area = mutableAllAreas.value.toString()
                    repo.getMealByArea(area).fold(
                        onSuccess = { meals ->
                            _areaState.postValue(AreaState.SuccessByArea(meals))
                        },
                        onError = { code, _ ->
                            val error = handleError(code.validateHttpErrorCode())
                            _areaState.postValue(AreaState.Failure(error))
                        },
                        onException = { exception ->
                            val error = handleError(exception.toError())
                            _areaState.postValue(AreaState.Failure(error))

                        }
                    )
                },
                onError = { code, _ ->
                    val error = handleError(code.validateHttpErrorCode())
                    _areaState.postValue(AreaState.Failure(error))
                },
                onException = { exception ->
                    val error = handleError(exception.toError())
                    _areaState.postValue(AreaState.Failure(error))

                }
            )
        }
    }
}