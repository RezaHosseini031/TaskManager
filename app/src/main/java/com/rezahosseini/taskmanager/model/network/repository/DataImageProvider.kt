package com.rezahosseini.taskmanager.model.network.repository

import com.rezahosseini.taskmanager.model.network.DataImage
import com.rezahosseini.taskmanager.model.network.web.model.WebDataImage
import kotlinx.coroutines.flow.Flow

interface DataImageProvider {
    fun getImages(): Flow<List<DataImage>>
}