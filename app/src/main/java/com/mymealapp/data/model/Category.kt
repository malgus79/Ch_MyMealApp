package com.mymealapp.data.model

import com.google.gson.annotations.SerializedName

data class CategoryList(
    @SerializedName("categories") val categories: List<Category> = listOf()
)

data class Category(
    @SerializedName("idCategory") val idCategory: String? = "",
    @SerializedName("strCategory") val nameCategory: String? = "",
    @SerializedName("strCategoryDescription") val descriptionCategory: String? = "",
    @SerializedName("strCategoryThumb") val imageCategory: String? = ""
)