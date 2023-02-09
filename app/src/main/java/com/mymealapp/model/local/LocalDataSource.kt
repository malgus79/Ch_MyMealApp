package com.mymealapp.model.local

import com.mymealapp.model.data.Meal
import com.mymealapp.model.data.MealList
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
}


