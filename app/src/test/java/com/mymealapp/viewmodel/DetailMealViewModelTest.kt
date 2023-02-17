package com.mymealapp.viewmodel

import com.mymealapp.accessdata.JSONFileLoader
import com.mymealapp.core.Constants
import com.mymealapp.model.data.Meal
import com.mymealapp.model.data.MealByCategory
import com.mymealapp.model.remote.ApiService
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailMealViewModelTest {

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
    fun `check fetch meals by id (id = 52977) is not null test`() {
        runBlocking {
            val result = api.getMealDetailsById("52977")
            assertThat(result.meals, `is`(notNullValue()))
        }
    }

    @Test
    fun `check item meals by id (id = 52977) for page test`() {
        runBlocking {
            val result = api.getMealDetailsById("52977")
            assertThat(result.meals.size, `is`(1))
        }
    }

    @Test
    fun `check error fetch meals by id (id = 52977) test`() {
        runBlocking {
            try {
                api.getMealDetailsById("52977")
            } catch (e: Exception) {
                assertThat(e.localizedMessage, `is`("HTTP 401 "))
            }
        }
    }

    @Test
    fun `check meals by id (id = 52977) remote with local test`() {
        runBlocking {
            val remoteResult = api.getMealDetailsById("52977")
            val localResult =
                JSONFileLoader().loadMealList("meal_by_id_response_success")

            assertThat(
                localResult?.meals?.size,
                `is`(remoteResult.meals.size)
            )

            assertThat(
                localResult?.meals.isNullOrEmpty(),
                `is`(remoteResult.meals.isEmpty())
            )

            assertThat(
                localResult?.meals?.contains(Meal("","","","","","","","")),
                `is`(remoteResult.meals.contains(Meal("","","","","","","","")))
            )

            assertThat(
                localResult?.meals?.indices,
                `is`(remoteResult.meals.indices)
            )
        }
    }
}