package com.mymealapp.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mymealapp.model.data.Meal
import com.mymealapp.model.data.MealList

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

fun List<MealEntity>.toMealList(): MealList {
    val resultList = mutableListOf<Meal>()
    this.forEach { mealEntity ->
        resultList.add(mealEntity.toMeal())
    }
    return MealList(resultList)
}

private fun MealEntity.toMeal(): Meal = Meal(
    this.idMeal,
    this.name,
    this.category,
    this.area,
    this.instructions,
    this.image,
    this.tags,
    this.youtube
)


fun Meal.toMealEntity(): MealEntity = MealEntity(
    this.idMeal,
    this.name,
    this.category,
    this.area,
    this.instructions,
    this.image,
    this.tags,
    this.youtube
)

fun Meal.asFavoriteEntity(): FavoritesEntity = FavoritesEntity(
    this.idMeal,
    this.name,
    this.category,
    this.area,
    this.instructions,
    this.image,
    this.tags,
    this.youtube
)

fun List<FavoritesEntity>.asMealList(): List<Meal> = this.map {
    Meal(
        it.idMeal,
        it.name,
        it.category,
        it.area,
        it.instructions,
        it.image,
        it.tags,
        it.youtube
    )
}