package com.mymealapp.model.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MealEntity::class, FavoritesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MealDataBase : RoomDatabase() {
    abstract fun mealDao(): MealDao
}