package com.mymealapp.viewmodel

import com.mymealapp.accessdata.JSONFileLoader
import com.mymealapp.app.Constants
import com.mymealapp.data.model.Area
import com.mymealapp.data.model.Meal
import com.mymealapp.data.remote.ApiService
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AreaViewModelTest {

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
    fun `check fetch area list is not null test`() {
        runBlocking {
            val result = api.getAllAreaList("American")
            assertThat(result.meals, `is`(notNullValue()))
        }
    }

    @Test
    fun `check fetch meal by area is not null test`() {
        runBlocking {
            val result = api.getMealByArea("American")
            assertThat(result.meals, `is`(notNullValue()))
        }
    }

    @Test
    fun `check item area list for page test`() {
        runBlocking {
            val result = api.getAllAreaList("American")
            assertThat(result.meals.size, `is`(27))
        }
    }

    @Test
    fun `check item meal by area for page test`() {
        runBlocking {
            val result = api.getMealByArea("American")
            assertThat(result.meals.size, `is`(32))
        }
    }

    @Test
    fun `check error fetch area list test`() {
        runBlocking {
            try {
                api.getAllAreaList("American")
            } catch (e: Exception) {
                assertThat(e.localizedMessage, `is`(""))
            }
        }
    }

    @Test
    fun `check error fetch meal by area test`() {
        runBlocking {
            try {
                api.getMealByArea("American")
            } catch (e: Exception) {
                assertThat(e.localizedMessage, `is`(""))
            }
        }
    }

    @Test
    fun `check first item area list test`() {
        runBlocking {
            val result = api.getAllAreaList("American")
            assertThat(
                result.meals.first(), `is`(
                    Area(
                        "American",
                    )
                )
            )
        }
    }

    @Test
    fun `check first item meal by area test`() {
        runBlocking {
            val result = api.getMealByArea("American")
            assertThat(
                result.meals.first(), `is`(
                    Meal(
                        "52855",
                        "Banana Pancakes",
                        null,
                        null,
                        null,
                        "https://www.themealdb.com/images/media/meals/sywswr1511383814.jpg",
                        null,
                        null
                    )
                )
            )
        }
    }

    @Test
    fun `check area list remote with local test`() {
        runBlocking {
            val remoteResult = api.getAllAreaList("American")
            val localResult = JSONFileLoader().loadAreaList("area_list_response_success")

            assertThat(
                localResult?.meals?.size,
                `is`(remoteResult.meals.size)
            )

            assertThat(
                localResult?.meals.isNullOrEmpty(),
                `is`(remoteResult.meals.isEmpty())
            )

            assertThat(
                localResult?.meals?.contains(Area()),
                `is`(remoteResult.meals.contains(Area()))
            )

            assertThat(
                localResult?.meals?.indices,
                `is`(remoteResult.meals.indices)
            )
        }
    }

    @Test
    fun `check meal by area remote with local test`() {
        runBlocking {
            val remoteResult = api.getMealByArea("American")
            val localResult = JSONFileLoader().loadAreaList("meal_by_area_response_success")

            assertThat(
                localResult?.meals?.size,
                `is`(remoteResult.meals.size)
            )

            assertThat(
                localResult?.meals.isNullOrEmpty(),
                `is`(remoteResult.meals.isEmpty())
            )

            assertThat(
                localResult?.meals?.contains(Area("American")),
                `is`(remoteResult.meals.contains(Meal("", "", "", "", "", "", "", "")))
            )

            assertThat(
                localResult?.meals?.indices,
                `is`(remoteResult.meals.indices)
            )
        }
    }
}