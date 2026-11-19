package com.rezahosseini.taskmanager.data.remote.service


import com.rezahosseini.taskmanager.model.network.web.WebService
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WebServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var webService: WebService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

        webService = WebService(retrofit)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getDataImageService_returnsServiceInstance() {
        val result = webService.getDataImageService()

        assertNotNull(result)
    }

    @Test
    fun getDataImageService_returnsSameInstance() {
        val first = webService.getDataImageService()
        val second = webService.getDataImageService()

        assert(first === second)
    }
}
