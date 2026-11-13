package com.rezahosseini.taskmanager.di

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
            .baseUrl("https://raw.githubusercontent.com/RezaHosseini031/TaskManager/refs/heads/main/app/src/main/assets/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}