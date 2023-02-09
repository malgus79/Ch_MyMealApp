package com.mymealapp.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meal(
    @SerializedName("idMeal") val idMeal: String? = "",
    @SerializedName("strMeal") val strMeal: String? = "",
    @SerializedName("strCategory") val strCategory: String? = "",
    @SerializedName("strArea") val strArea: String? = "",
    @SerializedName("strInstructions") val strInstructions: String? = "",
    @SerializedName("strMealThumb") val strMealThumb: String? = "",
    @SerializedName("strTags") val strTags: String? = "",
    @SerializedName("strYoutube") val strYoutube: String? = ""
) : Parcelable