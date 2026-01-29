package com.rezahosseini.taskmanager.di

import com.google.gson.Gson
import com.rezahosseini.taskmanager.model.network.web.mapper.WebDataImageMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://github.com/RezaHosseini031/TaskManager/tree/main/app/src/main/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}