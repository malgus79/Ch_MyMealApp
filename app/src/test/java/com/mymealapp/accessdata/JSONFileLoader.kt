package com.mymealapp.accessdata

import com.google.gson.Gson
import com.mymealapp.model.data.CategoryList
import com.mymealapp.model.data.MealByCategoryList
import java.io.InputStreamReader

class JSONFileLoader {
    private var jsonStr: String? = null

    fun loadJSONString(file: String): String? {
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return jsonStr
    }

    fun loadCategoryList(file: String): MealByCategoryList? {
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return Gson().fromJson(jsonStr, MealByCategoryList::class.java)
    }
}