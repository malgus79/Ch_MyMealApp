package com.mymealapp.ui.fragment.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.mymealapp.core.Resource
import com.mymealapp.domain.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: RepositoryImpl) : ViewModel() {

//    fun fetchRandomMeal() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
//        emit(Resource.Loading)
//        try {
//            emit(Resource.Success(repo.getRandomMeal()))
//        } catch (e: Exception) {
//            emit(Resource.Failure(e))
//        }
//    }
//
//    fun fetchPopularMeals(categoryName: String) =
//        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
//            emit(Resource.Loading)
//            try {
//                emit(Resource.Success(repo.getPopularMeals(categoryName)))
//            } catch (e: Exception) {
//                emit(Resource.Failure(e))
//            }
//        }
//
//    fun fetchCategoriesMeal() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
//        emit(Resource.Loading)
//        try {
//            emit(Resource.Success(repo.getCategoriesMeal()))
//        } catch (e: Exception) {
//            emit(Resource.Failure(e))
//        }
//    }

    fun fetchAllMealsInHome(categoryName: String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(
                Resource.Success(
                    Triple(
                        repo.getRandomMeal(),
                        repo.getPopularMeals(categoryName),
                        repo.getCategoriesMeal()
                    )
                )
            )
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}