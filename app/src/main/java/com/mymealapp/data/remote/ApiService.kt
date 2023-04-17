package com.mymealapp.data.remote

import com.mymealapp.data.model.AreaList
import com.mymealapp.data.model.CategoryList
import com.mymealapp.data.model.MealByCategoryList
import com.mymealapp.data.model.MealList
import retrofit2.Response
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
    suspend fun getCategoriesMeal(): Response<CategoryList>

    @GET("filter.php")
    suspend fun getMealByCategory(@Query("c") nameOfCategory: String): MealByCategoryList

    @GET("lookup.php")
    suspend fun getMealDetailsById(@Query("i") id: String): MealList

    @GET("filter.php")
    suspend fun getPopularMeals(@Query("c") categoryName: String): MealByCategoryList

    @GET("list.php")
    suspend fun getAllAreaList(@Query(value = "a") area: String): Response<AreaList>

    @GET("filter.php")
    suspend fun getMealByArea(@Query(value = "a") area: String): Response<MealList>
}