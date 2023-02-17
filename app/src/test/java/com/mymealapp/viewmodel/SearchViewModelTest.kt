package com.mymealapp.viewmodel

import com.mymealapp.accessdata.JSONFileLoader
import com.mymealapp.core.Constants.BASE_URL
import com.mymealapp.model.data.Meal
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

class SearchViewModelTest {

    private lateinit var api: ApiService

    companion object {
        private lateinit var retrofit: Retrofit

        @BeforeClass
        @JvmStatic
        fun setupCommon() {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    @Before
    fun setup() {
        api = retrofit.create(ApiService::class.java)
    }

    @Test
    fun `check fetch meals searched is not null test`() {
        runBlocking {
            val result = api.getMealsByName("")
            assertThat(result.meals, `is`(notNullValue()))
        }
    }

    @Test
    fun `check item meals searched for page test`() {
        runBlocking {
            val result = api.getMealsByName("")
            assertThat(result.meals.size, `is`(25))
        }
    }

    @Test
    fun `check error fetch meals test`() {
        runBlocking {
            try {
                api.getMealsByName("")
            } catch (e: Exception) {
                assertThat(e.localizedMessage, `is`("HTTP 401 "))
            }
        }
    }

    @Test
    fun `check meals searched remote with local test`() {
        runBlocking {
            val remoteResult = api.getMealsByName("")
            val localResult = JSONFileLoader().loadMealList("meal_response_success")

            assertThat(
                localResult?.meals?.size,
                `is`(remoteResult.meals.size)
            )

            assertThat(
                localResult?.meals.isNullOrEmpty(),
                `is`(remoteResult.meals.isEmpty())
            )

            assertThat(
                localResult?.meals?.contains(Meal("", "", "", "", "", "", "", "")),
                `is`(remoteResult.meals.contains(Meal("", "", "", "", "", "", "", "")))
            )

            assertThat(
                localResult?.meals?.indices,
                `is`(remoteResult.meals.indices)
            )
        }
    }
}