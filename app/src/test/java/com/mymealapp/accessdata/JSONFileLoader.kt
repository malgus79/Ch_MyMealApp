package com.mymealapp.accessdata

import com.google.gson.Gson
import com.mymealapp.model.data.AreaList
import com.mymealapp.model.data.CategoryList
import com.mymealapp.model.data.MealByCategoryList
import com.mymealapp.model.data.MealList
import java.io.InputStreamReader

class JSONFileLoader {
    private var jsonStr: String? = null

    fun loadJSONString(file: String): String? {
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return jsonStr
    }

    fun loadMealByCategoryList(file: String): MealByCategoryList? {
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return Gson().fromJson(jsonStr, MealByCategoryList::class.java)
    }

    fun loadMealList(file: String): MealList? {
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return Gson().fromJson(jsonStr, MealList::class.java)
    }

    fun loadCategoryList(file: String): CategoryList? {
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return Gson().fromJson(jsonStr, CategoryList::class.java)
    }

    fun loadAreaList(file: String): AreaList? {
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return Gson().fromJson(jsonStr, AreaList::class.java)
    }

}