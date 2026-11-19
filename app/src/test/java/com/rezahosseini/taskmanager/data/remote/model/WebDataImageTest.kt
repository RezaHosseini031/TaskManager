package com.rezahosseini.taskmanager.data.remote.model

import com.google.gson.Gson
import com.rezahosseini.taskmanager.model.network.web.model.WebDataImage
import org.junit.Assert.assertEquals
import org.junit.Test

class WebDataImageTest {

    private val gson = Gson()

    @Test
    fun fromJson_mapsJsonFieldsCorrectly() {
        val json = """
            {
                "id": 5,
                "uriImage": "icons/icon5.png"
            }
        """.trimIndent()

        val result = gson.fromJson(
            json,
            WebDataImage::class.java
        )

        assertEquals(5L, result.id)
        assertEquals("icons/icon5.png", result.uriImage)
    }

    @Test
    fun toJson_serializesFieldsCorrectly() {
        val image = WebDataImage(
            id = 3L,
            uriImage = "icons/icon3.png"
        )

        val result = gson.toJson(image)

        val expected = """
            {"id":3,"uriImage":"icons/icon3.png"}
        """.trimIndent()

        assertEquals(expected, result)
    }
}
