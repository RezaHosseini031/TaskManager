package com.rezahosseini.taskmanager.model.network.web.mapper

import com.rezahosseini.taskmanager.model.network.DataImage
import com.rezahosseini.taskmanager.model.network.web.model.WebDataImage

class WebDataImageMapper {
    fun toDataImage(webDataImage: WebDataImage):DataImage{
        return DataImage(webDataImage.id,webDataImage.uriImage)
    }
    fun fromDataImage(dataImage: DataImage):WebDataImage{
        return WebDataImage(dataImage.id,dataImage.uriImage)
    }
}