package com.mymealapp.model.remote

import com.mymealapp.model.data.MealList
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: ApiService) {

    suspend fun getMealsByName(mealName: String): MealList {
        return api.getMealsByName(mealName)
    }
}