package com.mymealapp.domain

import androidx.lifecycle.LiveData
import com.mymealapp.core.Resource
import com.mymealapp.model.data.*
import com.mymealapp.model.local.LocalDataSource
import com.mymealapp.model.local.MealEntity
import com.mymealapp.model.remote.RemoteDataSource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : RepositoryInterface {

//    suspend fun getMealsByName(mealName: String): MealList {
//        localDataSource.deleteCachedMeal()
//        remoteDataSource.getMealsByName(mealName).meals.forEach {
//            localDataSource.saveMeal(it.toMealEntity())
//        }
//        return localDataSource.getMeals()
//    }

    /*------------------------------ Search ------------------------------*/
    override suspend fun getMealsByName(mealSearched: String?): Flow<Resource<List<Meal>>> =
        callbackFlow {
            trySend(getCachedMeals(mealSearched))

            remoteDataSource.getMealsByName(mealSearched.toString()).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        for (meal in it.data) {
                            saveMeal(meal.asMealEntity())
                        }
                        trySend(getCachedMeals(mealSearched))
                    }
                    is Resource.Failure -> {
                        trySend(getCachedMeals(mealSearched))
                    }
                    else -> {}
                }
            }
            awaitClose { cancel() }
        }

    /*------------------------------ Favorites ------------------------------*/
    private suspend fun getCachedMeals(mealSearched: String?): Resource<List<Meal>> {
        return localDataSource.getCachedMeals(mealSearched)
    }

    private suspend fun saveMeal(meal: MealEntity) {
        localDataSource.saveMeal(meal)
    }

    override suspend fun isMealFavorite(meal: Meal): Boolean {
        return localDataSource.isMealFavorite(meal)
    }

    override suspend fun deleteFavoriteMeal(meal: Meal) {
        localDataSource.deleteMeal(meal)
    }

    override suspend fun saveFavoriteMeal(meal: Meal) {
        localDataSource.saveFavoriteMeal(meal)
    }

    override fun getFavoritesMeals(): LiveData<List<Meal>> {
        return localDataSource.getFavoritesMeals()
    }

    /*------------------------------ Random ------------------------------*/
    override suspend fun getRandomMeal(): MealList {
        return remoteDataSource.getRandomMeal()
    }

    /*------------------------------ Categories ------------------------------*/
    override suspend fun getCategoriesMeal(): CategoryList {
        return remoteDataSource.getCategoriesMeal()
    }

    /*------------------------------ Meal by category ------------------------------*/
    override suspend fun getMealByCategory(nameOfCategory: String): MealByCategoryList {
        return remoteDataSource.getMealByCategory(nameOfCategory)
    }

    /*------------------------------ Detail meal by category ------------------------------*/
    override suspend fun getMealDetailsById(id: String): MealList {
        return remoteDataSource.getMealDetailsById(id)
    }

    /*------------------------------ Popular meals ------------------------------*/
    override suspend fun getPopularMeals(categoryName: String): MealByCategoryList {
        return remoteDataSource.getPopularMeals(categoryName)
    }
}