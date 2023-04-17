package com.mymealapp.domain

import androidx.lifecycle.LiveData
import com.mymealapp.core.Resource
import com.mymealapp.data.model.*
import kotlinx.coroutines.flow.Flow
import com.mymealapp.domain.common.Result

interface RepositoryInterface {

    suspend fun getMealsByName(mealSearched: String?): Flow<Resource<List<Meal>>>

    /*------------------------------ Favorites ------------------------------*/
    suspend fun isMealFavorite(meal: Meal): Boolean

    suspend fun deleteFavoriteMeal(meal: Meal)

    suspend fun saveFavoriteMeal(meal: Meal)

    fun getFavoritesMeals(): LiveData<List<Meal>>

    /*------------------------------ Random ------------------------------*/
    suspend fun getRandomMeal(): MealList

    /*------------------------------ Categories ------------------------------*/
    suspend fun getCategoriesMeal(): Result<List<Category>>

    /*------------------------------ Meal by category ------------------------------*/
    suspend fun getMealByCategory(nameOfCategory: String): MealByCategoryList

    /*------------------------------ Detail meal by category ------------------------------*/
    suspend fun getMealDetailsById(id: String): MealList

    /*------------------------------ Popular meals ------------------------------*/
    suspend fun getPopularMeals(categoryName: String): MealByCategoryList

    /*------------------------------ All area list ------------------------------*/
    suspend fun getAllAreaList(area:String): Result<List<Area>>

    /*------------------------------ Meal by area ------------------------------*/
    suspend fun getMealByArea(area: String): Result<List<Meal>>
}