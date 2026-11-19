package com.rezahosseini.taskmanager.data.remote.service

import com.rezahosseini.taskmanager.model.network.web.DataImageService
import com.rezahosseini.taskmanager.model.network.web.model.WebDataImage
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue

class DataImageServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: DataImageService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(DataImageService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getImages_returnsImagesFromJson() = runTest {
        val json = """
            [
              {
                "id": 1,
                "uriImage": "icons/icon1.png"
              },
              {
                "id": 2,
                "uriImage": "icons/icon2.png"
              }
            ]
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(json)
        )

        val result = service.getImages()

        assertEquals(2, result.size)

        assertEquals(
            WebDataImage(1L, "icons/icon1.png"),
            result[0]
        )

        assertEquals(
            WebDataImage(2L, "icons/icon2.png"),
            result[1]
        )

        val request = mockWebServer.takeRequest()

        assertEquals("GET", request.method)
        assertEquals("/icons.json", request.path)
    }

    @Test
    fun getImages_returnsEmptyListForEmptyJsonArray() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("[]")
        )

        val result = service.getImages()

        assertEquals(emptyList<WebDataImage>(), result)
    }

    @Test
    fun getImages_throwsExceptionForServerError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        )

        val result = runCatching {
            service.getImages()
        }

        assertTrue(result.isFailure)
    }
}
