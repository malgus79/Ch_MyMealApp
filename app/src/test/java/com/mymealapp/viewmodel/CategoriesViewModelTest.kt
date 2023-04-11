package com.mymealapp.viewmodel

import com.mymealapp.accessdata.JSONFileLoader
import com.mymealapp.app.Constants
import com.mymealapp.data.model.Category
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

class CategoriesViewModelTest {

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
    fun `check fetch categories is not null test`() {
        runBlocking {
            val result = api.getCategoriesMeal()
            assertThat(result.categories, `is`(notNullValue()))
        }
    }

    @Test
    fun `check item categories for page test`() {
        runBlocking {
            val result = api.getCategoriesMeal()
            assertThat(result.categories.size, `is`(14))
        }
    }

    @Test
    fun `check error fetch categories test`() {
        runBlocking {
            try {
                api.getCategoriesMeal()
            } catch (e: Exception) {
                assertThat(e.localizedMessage, `is`("HTTP 401 "))
            }
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
    fun `check categories remote with local test`() {
        runBlocking {
            val remoteResult = api.getCategoriesMeal()
            val localResult = JSONFileLoader().loadCategoryList("category_response_success")

            assertThat(
                localResult?.categories?.size,
                `is`(remoteResult.categories.size)
            )

            assertThat(
                localResult?.categories.isNullOrEmpty(),
                `is`(remoteResult.categories.isEmpty())
            )

            assertThat(
                localResult?.categories?.contains(Category()),
                `is`(remoteResult.categories.contains(Category()))
            )

            assertThat(
                localResult?.categories?.indices,
                `is`(remoteResult.categories.indices)
            )
        }
    }
}