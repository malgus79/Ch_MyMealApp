package com.mymealapp.model.remote

import com.mymealapp.core.Resource
import com.mymealapp.model.data.CategoryList
import com.mymealapp.model.data.Meal
import com.mymealapp.model.data.MealByCategoryList
import com.mymealapp.model.data.MealList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: ApiService) {

//    suspend fun getMealsByName(mealName: String): MealList {
//        return api.getMealsByName(mealName)
//    }

    suspend fun getMealsByName(mealSearched: String): Flow<Resource<List<Meal>>> =
        callbackFlow {
            trySend(Resource.Success(api.getMealsByName(mealSearched).meals ?: listOf()))
            awaitClose { close() }
        }

    suspend fun getRandomMeal(): MealList {
        return api.getRandomMeal()
    }

    suspend fun getCategoriesMeal(): CategoryList {
        return api.getCategoriesMeal()
    }

    suspend fun getMealByCategory(nameOfCategory: String): MealByCategoryList {
        return api.getMealByCategory(nameOfCategory)
    }

    suspend fun getMealDetailsById(id: String): MealList {
        return api.getMealDetailsById(id)
    }

    suspend fun getPopularMeals(categoryName: String): MealByCategoryList {
        return api.getMealByCategory(categoryName)
    }
}