package com.mymealapp.domain

import com.mymealapp.model.data.MealList
import com.mymealapp.model.remote.RemoteDataSource
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun getMealsByName(mealName: String): MealList {
        return remoteDataSource.getMealsByName(mealName)
    }
}