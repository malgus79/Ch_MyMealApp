package com.mymealapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MealByCategoryList(
    @SerializedName("meals") val meals: List<MealByCategory> = listOf()
)

@Parcelize
data class MealByCategory(
    @SerializedName("idMeal") val idMeal: String? = "",
    @SerializedName("strMeal") val name: String? = "",
    @SerializedName("strMealThumb") val image: String? = ""
) : Parcelable