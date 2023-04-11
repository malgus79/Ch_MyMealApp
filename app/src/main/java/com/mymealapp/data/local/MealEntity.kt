package com.mymealapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_entity")
data class MealEntity(
    @PrimaryKey val idMeal: String,
    @ColumnInfo(name = "strMeal") val name: String? = "",
    @ColumnInfo(name = "strCategory") val category: String? = "",
    @ColumnInfo(name = "strArea") val area: String? = "",
    @ColumnInfo(name = "strInstructions") val instructions: String? = "",
    @ColumnInfo(name = "strMealThumb") val image: String? = "",
    @ColumnInfo(name = "strTags") val tags: String? = "",
    @ColumnInfo(name = "strYoutube") val youtube: String? = ""
)

@Entity(tableName = "favorites_entity")
data class FavoritesEntity(
    @PrimaryKey val idMeal: String,
    @ColumnInfo(name = "strMeal") val name: String? = "",
    @ColumnInfo(name = "strCategory") val category: String? = "",
    @ColumnInfo(name = "strArea") val area: String? = "",
    @ColumnInfo(name = "strInstructions") val instructions: String? = "",
    @ColumnInfo(name = "strMealThumb") val image: String? = "",
    @ColumnInfo(name = "strTags") val tags: String? = "",
    @ColumnInfo(name = "strYoutube") val youtube: String? = ""
)