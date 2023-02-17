package com.mymealapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mymealapp.accessdata.JSONFileLoader
import com.mymealapp.core.Constants
import com.mymealapp.model.data.Category
import com.mymealapp.model.data.MealByCategory
import com.mymealapp.model.remote.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModelTest {

    @get:Rule
    val instantExecutionerRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutinesRule = MainDispatcherRule()

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
    fun `check fetch meals random is not null test`() {
        runBlocking {
            val result = api.getRandomMeal()
            assertThat(result, `is`(notNullValue()))
        }
    }

    @Test
    fun `check fetch meals popular is not null test`() {
        runBlocking {
            val result = api.getPopularMeals("Beef")
            assertThat(result, `is`(notNullValue()))
        }
    }

    @Test
    fun `check fetch categories is not null test`() {
        runBlocking {
            val result = api.getCategoriesMeal()
            assertThat(result, `is`(notNullValue()))
        }
    }

    @Test
    fun `check items of meals random test`() {
        runBlocking {
            val result = api.getRandomMeal()
            assertThat(result.meals.size, `is`(1))
        }
    }

    @Test
    fun `check items of meals in categories beef test`() {
        runBlocking {
            val result = api.getPopularMeals("Beef")
            assertThat(result.meals.size, `is`(42))
        }
    }

    @Test
    fun `check number of categories test`() {
        runBlocking {
            val result = api.getCategoriesMeal()
            assertThat(result.categories.size, `is`(14))
        }
    }

    @Test
    fun `check first item meal test`() {
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
    fun `check first item categories test`() {
        runBlocking {
            val result = api.getCategoriesMeal()
            assertThat(
                result.categories.first(), `is`(
                    Category(
                        "1",
                        "Beef",
                        "Beef is the culinary name for meat from cattle, particularly skeletal muscle. Humans have been eating beef since prehistoric times.[1] Beef is a source of high-quality protein and essential nutrients.[2]",
                        "https://www.themealdb.com/images/category/beef.png"
                    )
                )
            )
        }
    }

    @Test
    fun `check meals remote with local test`() {
        runBlocking {
            val remoteResult = api.getPopularMeals("Beef")
            val localResult = JSONFileLoader().loadMealByCategoryList("meal_by_category_response_success")

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