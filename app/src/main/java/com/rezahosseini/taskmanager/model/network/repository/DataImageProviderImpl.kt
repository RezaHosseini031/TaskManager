package com.rezahosseini.taskmanager.model.network.repository

import com.rezahosseini.taskmanager.model.network.DataImage
import com.rezahosseini.taskmanager.model.network.web.DataImageService
import com.rezahosseini.taskmanager.model.network.web.mapper.WebDataImageMapper
import com.rezahosseini.taskmanager.model.network.web.model.WebDataImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataImageProviderImpl : DataImageProvider {
    private val dataImageService:DataImageService
    private val webDataImageMapper:WebDataImageMapper
    @Inject
    constructor(dataImageService: DataImageService,webDataImageMapper: WebDataImageMapper){
        this.dataImageService=dataImageService
        this.webDataImageMapper=webDataImageMapper
    }
    override fun getImages(): Flow<List<DataImage>> {
        return dataImageService.getImages().map { webDataImage: List<WebDataImage> ->
            webDataImage.map { webDataImageMapper.toDataImage(it) } }
    }
}