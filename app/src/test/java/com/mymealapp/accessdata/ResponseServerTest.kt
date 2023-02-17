package com.mymealapp.accessdata

import com.google.gson.Gson
import com.mymealapp.model.data.MealByCategoryList
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class ResponseServerTest {
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `read json file success`() {
        val reader = JSONFileLoader().loadJSONString("meal_by_category_response_success")
        assertThat(reader, `is`(notNullValue()))
        assertThat(reader, containsString("Beef and Mustard Pie"))
    }

    @Test
    fun `get meals and check title exist`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                JSONFileLoader().loadJSONString("meal_by_category_response_success")
                    ?: ""
            )
        mockWebServer.enqueue(response)

        assertThat(response.getBody()?.readUtf8(), containsString("\"strMeal\""))
    }

    @Test
    fun `get meals and check fail response`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                JSONFileLoader().loadJSONString("meal_by_category_response_fail")
                    ?: ""
            )
        mockWebServer.enqueue(response)

        assertThat(
            response.getBody()?.readUtf8(),
            containsString("{\"meals\":null}")
        )
    }

    @Test
    fun `get meals and check contains strMeal list no empty`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(
                JSONFileLoader().loadJSONString("meal_by_category_response_success")
                    ?: ""
            )
        mockWebServer.enqueue(response)

        assertThat(response.getBody()?.readUtf8(), containsString("strMeal"))

        val json =
            Gson().fromJson(response.getBody()?.readUtf8() ?: "", MealByCategoryList::class.java)
        assertThat(json.meals.isEmpty(), `is`(false))
    }
}