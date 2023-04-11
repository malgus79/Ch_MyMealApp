package com.mymealapp.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mymealapp.core.Resource
import com.mymealapp.data.model.*
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: MealDao) {

    suspend fun saveMeal(meal: MealEntity) {
        dao.saveMeal(meal)
    }

    suspend fun getMeals(): MealList {
        return dao.getAllMeals().toMealList()
    }

    suspend fun isMealFavorite(meal: Meal): Boolean {
        return dao.getMealById(meal.idMeal) != null
    }

    suspend fun deleteMeal(meal: Meal) {
        return dao.deleteFavoriteMeal(meal.asFavoriteEntity())
    }

    suspend fun saveFavoriteMeal(meal: Meal) {
        return dao.saveFavoriteMeal(meal.asFavoriteEntity())
    }

    suspend fun deleteCachedMeal() {
        return dao.deleteCachedMeal()
    }

    fun getFavoritesMeals(): LiveData<List<Meal>> {
        return dao.getAllFavoritesMealsWithChanges().map { it.asMealList() }
    }

    suspend fun getCachedMeals(mealSearched: String?): Resource<List<Meal>> {
        return Resource.Success(dao.getMeals(mealSearched).asMealList())
    }
}