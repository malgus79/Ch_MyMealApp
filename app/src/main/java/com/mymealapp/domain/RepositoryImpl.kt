package com.mymealapp.domain

import androidx.lifecycle.LiveData
import com.mymealapp.core.Resource
import com.mymealapp.model.data.Meal
import com.mymealapp.model.data.MealList
import com.mymealapp.model.data.asMealEntity
import com.mymealapp.model.local.LocalDataSource
import com.mymealapp.model.local.MealEntity
import com.mymealapp.model.remote.RemoteDataSource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

//    suspend fun getMealsByName(mealName: String): MealList {
//        localDataSource.deleteCachedMeal()
//        remoteDataSource.getMealsByName(mealName).meals.forEach {
//            localDataSource.saveMeal(it.toMealEntity())
//        }
//        return localDataSource.getMeals()
//    }

    /*------------------------------ Search ------------------------------*/
    suspend fun getMealsByName(mealSearched: String?): Flow<Resource<List<Meal>>> =
        callbackFlow {
            trySend(getCachedMeals(mealSearched))

            remoteDataSource.getMealsByName(mealSearched.toString()).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        for (meal in it.data) {
                            saveMeal(meal.asMealEntity())
                        }
                        trySend(getCachedMeals(mealSearched))
                    }
                    is Resource.Failure -> {
                        trySend(getCachedMeals(mealSearched))
                    }
                    else -> {}
                }
            }
            awaitClose { cancel() }
        }

    /*------------------------------ Favorites ------------------------------*/
    private suspend fun getCachedMeals(mealSearched: String?): Resource<List<Meal>> {
        return localDataSource.getCachedMeals(mealSearched)
    }

    private suspend fun saveMeal(meal: MealEntity) {
        localDataSource.saveMeal(meal)
    }

    suspend fun isMealFavorite(meal: Meal): Boolean {
        return localDataSource.isMealFavorite(meal)
    }

    suspend fun deleteFavoriteMeal(meal: Meal) {
        localDataSource.deleteMeal(meal)
    }

    suspend fun saveFavoriteMeal(meal: Meal) {
        localDataSource.saveFavoriteMeal(meal)
    }

    fun getFavoritesMeals(): LiveData<List<Meal>> {
        return localDataSource.getFavoritesMeals()
    }

    /*------------------------------ Random ------------------------------*/
    suspend fun getRandomMeal(): MealList {
        return remoteDataSource.getRandomMeal()
    }
}