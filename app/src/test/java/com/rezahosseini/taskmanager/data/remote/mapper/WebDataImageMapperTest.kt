package com.rezahosseini.taskmanager.data.remote.mapper

import com.rezahosseini.taskmanager.model.network.DataImage
import com.rezahosseini.taskmanager.model.network.web.mapper.WebDataImageMapper
import com.rezahosseini.taskmanager.model.network.web.model.WebDataImage
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WebDataImageMapperTest {

    private lateinit var mapper: WebDataImageMapper

    @Before
    fun setup() {
        mapper = WebDataImageMapper()
    }

    @Test
    fun toDataImage_mapsWebDataImageCorrectly() {
        val webDataImage = WebDataImage(
            id = 1L,
            uriImage = "icons/icon1.png"
        )

        val result = mapper.toDataImage(webDataImage)

        assertEquals(
            DataImage(
                id = 1L,
                uriImage = "icons/icon1.png"
            ),
            result
        )
    }

    @Test
    fun fromDataImage_mapsDataImageCorrectly() {
        val dataImage = DataImage(
            id = 2L,
            uriImage = "icons/icon2.png"
        )

        val result = mapper.fromDataImage(dataImage)

        assertEquals(
            WebDataImage(
                id = 2L,
                uriImage = "icons/icon2.png"
            ),
            result
        )
    }
}
