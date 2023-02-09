package com.mymealapp.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao {

    @Query("SELECT * FROM meal_entity")
    suspend fun getAllMeals(): List<MealEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMeal(meal: MealEntity)

    @Query("SELECT * FROM meal_entity WHERE strMeal LIKE '%' || :mealName || '%'") // This Like operator is needed due that the API returns blank spaces in the name
    suspend fun getMeala(mealName: String): List<MealEntity>
}