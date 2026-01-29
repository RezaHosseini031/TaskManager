package com.rezahosseini.taskmanager.model.network.web

import com.rezahosseini.taskmanager.model.network.DataImage
import com.rezahosseini.taskmanager.model.network.web.model.WebDataImage
import retrofit2.http.GET
import kotlinx.coroutines.flow.Flow

interface DataImageService {
    @GET("assets/icons.json")
    fun getImages(): Flow<List<WebDataImage>>
}