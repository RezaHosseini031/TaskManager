package com.rezahosseini.taskmanager.data.remote.repository

import com.rezahosseini.taskmanager.model.network.DataImage
import com.rezahosseini.taskmanager.model.network.repository.DataImageProviderImpl
import com.rezahosseini.taskmanager.model.network.web.DataImageService
import com.rezahosseini.taskmanager.model.network.web.mapper.WebDataImageMapper
import com.rezahosseini.taskmanager.model.network.web.model.WebDataImage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DataImageProviderImplTest {

    private lateinit var service: DataImageService
    private lateinit var mapper: WebDataImageMapper
    private lateinit var provider: DataImageProviderImpl

    private val webImages = listOf(
        WebDataImage(1L, "icons/icon1.png"),
        WebDataImage(2L, "icons/icon2.png")
    )

    private val expectedImages = listOf(
        DataImage(1L, "icons/icon1.png"),
        DataImage(2L, "icons/icon2.png")
    )

    @Before
    fun setup() {
        service = mock()
        mapper = WebDataImageMapper()

        provider = DataImageProviderImpl(
            service,
            mapper
        )
    }

    @Test
    fun getImages_returnsMappedImagesFromService() = runTest {
        whenever(service.getImages())
            .thenReturn(webImages)

        val result = provider.getImages().first()

        assertEquals(expectedImages, result)

        verify(service).getImages()
    }

    @Test
    fun getImages_returnsEmptyListWhenServiceReturnsEmptyList() = runTest {
        whenever(service.getImages())
            .thenReturn(emptyList())

        val result = provider.getImages().first()

        assertEquals(emptyList<DataImage>(), result)

        verify(service).getImages()
    }

    @Test
    fun getImages_propagatesServiceException() = runTest {
        whenever(service.getImages())
            .thenThrow(IllegalStateException("Network error"))

        val result = runCatching {
            provider.getImages().first()
        }

        assertEquals(
            "Network error",
            result.exceptionOrNull()?.message
        )
    }
}
