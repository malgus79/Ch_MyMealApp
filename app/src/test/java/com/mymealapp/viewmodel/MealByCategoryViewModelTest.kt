package com.mymealapp.viewmodel

import com.mymealapp.accessdata.JSONFileLoader
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import com.mymealapp.core.Constants
import com.mymealapp.model.data.MealByCategory
import com.mymealapp.model.remote.ApiService
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MealByCategoryViewModelTest {

    private lateinit var api: ApiService

    companion object {
        private lateinit var retrofit: Retrofit

        @BeforeClass
        @JvmStatic
        fun setupCommon() {
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    @Before
    fun setup() {
        api = retrofit.create(ApiService::class.java)
    }

    @Test
    fun `check fetch meals by category beef is not null test`() {
        runBlocking {
            val result = api.getMealByCategory("Beef")
            assertThat(result.meals, `is`(notNullValue()))
        }
    }

    @Test
    fun `check item meals by category beef for page test`() {
        runBlocking {
            val result = api.getMealByCategory("Beef")
            assertThat(result.meals.size, `is`(42))
        }
    }

    @Test
    fun `check error fetch meals by category beef test`() {
        runBlocking {
            try {
                api.getMealByCategory("Beef")
            } catch (e: Exception) {
                assertThat(e.localizedMessage, `is`("HTTP 401 "))
            }
        }
    }

    @Test
    fun `check first item meal by category beef test`() {
        runBlocking {
            val result = api.getPopularMeals("Beef")
            assertThat(
                result.meals.first(), `is`(
                    MealByCategory(
                        "52874",
                        "Beef and Mustard Pie",
                        "https://www.themealdb.com/images/media/meals/sytuqu1511553755.jpg"
                    )
                )
            )
        }
    }

    @Test
    fun `check meals by category beef remote with local test`() {
        runBlocking {
            val remoteResult = api.getPopularMeals("Beef")
            val localResult =
                JSONFileLoader().loadMealByCategoryList("meal_by_category_response_success")

            assertThat(
                localResult?.meals?.size,
                `is`(remoteResult.meals.size)
            )

            assertThat(
                localResult?.meals.isNullOrEmpty(),
                `is`(remoteResult.meals.isEmpty())
            )

            assertThat(
                localResult?.meals?.contains(MealByCategory()),
                `is`(remoteResult.meals.contains(MealByCategory()))
            )

            assertThat(
                localResult?.meals?.indices,
                `is`(remoteResult.meals.indices)
            )
        }
    }
}