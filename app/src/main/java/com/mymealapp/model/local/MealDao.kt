package com.mymealapp.model.local

import androidx.room.*

@Dao
interface MealDao {

    @Query("SELECT * FROM meal_entity")
    suspend fun getAllMeals(): List<MealEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMeal(meal: MealEntity)

    @Query("SELECT * FROM meal_entity WHERE strMeal LIKE '%' || :mealName || '%'") // This Like operator is needed due that the API returns blank spaces in the name
    suspend fun getMeal(mealName: String): List<MealEntity>

    @Query("SELECT * FROM favorites_entity WHERE idMeal = :mealId")
    suspend fun getMealById(mealId: String): FavoritesEntity?

    @Delete
    suspend fun deleteFavoriteMeal(favorites: FavoritesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavoriteMeal(meal: FavoritesEntity)
}