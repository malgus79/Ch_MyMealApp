package com.mymealapp.data.model

import com.mymealapp.data.local.FavoritesEntity
import com.mymealapp.data.local.MealEntity

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

@JvmName("asMealListMealEntity")
fun List<MealEntity>.asMealList(): List<Meal> = this.map {
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

fun Meal.asMealEntity(): MealEntity = MealEntity(
    this.idMeal,
    this.name,
    this.category,
    this.area,
    this.instructions,
    this.image,
    this.tags,
    this.youtube
)