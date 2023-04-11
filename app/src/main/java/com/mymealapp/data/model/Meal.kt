package com.mymealapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MealList(
    @SerializedName("meals") val meals: List<Meal> = listOf()
)

@Parcelize
data class Meal(
    @SerializedName("idMeal") val idMeal: String,
    @SerializedName("strMeal") val name: String? = "",
    @SerializedName("strCategory") val category: String? = "",
    @SerializedName("strArea") val area: String? = "",
    @SerializedName("strInstructions") val instructions: String? = "",
    @SerializedName("strMealThumb") val image: String? = "",
    @SerializedName("strTags") val tags: String? = "",
    @SerializedName("strYoutube") val youtube: String? = ""
) : Parcelable