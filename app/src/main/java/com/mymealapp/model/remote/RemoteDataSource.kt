package com.mymealapp.model.remote

import com.mymealapp.core.Resource
import com.mymealapp.model.data.Meal
import com.mymealapp.model.data.MealList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
}