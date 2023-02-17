package com.mymealapp.model.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MealDao {

    @Query("SELECT * FROM meal_entity")
    suspend fun getAllMeals(): List<MealEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMeal(meal: MealEntity)

    @Query("SELECT * FROM meal_entity WHERE strMeal LIKE '%' || :mealName || '%'") // This Like operator is needed due that the API returns blank spaces in the name
    suspend fun getMeals(mealName: String?): List<MealEntity>

    @Query("SELECT * FROM favorites_entity WHERE idMeal = :mealId")
    suspend fun getMealById(mealId: String): FavoritesEntity?

    @Delete
    suspend fun deleteFavoriteMeal(favorites: FavoritesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavoriteMeal(meal: FavoritesEntity)

    @Query("DELETE FROM meal_entity")
    suspend fun deleteCachedMeal()

    @Query("SELECT * FROM favorites_entity")
    fun getAllFavoritesMealsWithChanges(): LiveData<List<FavoritesEntity>>

    //for androidTest in MealDaoTest
    @Query("SELECT * FROM favorites_entity")
    suspend fun getAllFavoriteMeals(): List<FavoritesEntity>
}