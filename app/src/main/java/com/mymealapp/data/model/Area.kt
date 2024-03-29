package com.mymealapp.data.model

import com.google.gson.annotations.SerializedName

data class AreaList(
    @SerializedName("meals") val meals: List<Area> = listOf()
)

data class Area(
    @SerializedName("strArea") val area: String? = ""
)