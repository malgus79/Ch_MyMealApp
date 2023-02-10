package com.mymealapp.model.remote

import com.mymealapp.model.data.CategoryList
import com.mymealapp.model.data.MealList
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search.php")
    suspend fun getMealsByName(
        @Query("s") mealSearched: String
    ): MealList

    @GET("random.php")
    suspend fun getRandomMeal(): MealList

    @GET("categories.php")
    suspend fun getCategoriesMeal(): CategoryList

}