package com.mymealapp.domain

import com.mymealapp.model.data.Meal
import com.mymealapp.model.data.MealList
import com.mymealapp.model.local.LocalDataSource
import com.mymealapp.model.local.toMealEntity
import com.mymealapp.model.remote.RemoteDataSource
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
    ) {

    suspend fun getMealsByName(mealName: String): MealList {
        localDataSource.deleteCachedMeal()
        remoteDataSource.getMealsByName(mealName).meals.forEach {
            localDataSource.saveMeal(it.toMealEntity())
        }
        return localDataSource.getMeals()
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
}


