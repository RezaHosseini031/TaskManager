package com.rezahosseini.taskmanager.model.network.web

import android.service.wallpaper.WallpaperService
import retrofit2.Retrofit
import javax.inject.Inject


class WebService {
    var retrofit: Retrofit

    private val dataImageService: DataImageService
    @Inject
    constructor(retrofit: Retrofit){
        this.retrofit=retrofit
        dataImageService=retrofit.create(DataImageService::class.java)
    }
    fun getDataImageService():DataImageService{
        return dataImageService
    }
}