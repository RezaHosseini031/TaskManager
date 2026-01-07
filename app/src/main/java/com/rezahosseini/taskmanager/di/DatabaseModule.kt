package com.rezahosseini.taskmanager.di

import android.content.Context
import com.rezahosseini.taskmanager.model.local.repository.DataRepository
import com.rezahosseini.taskmanager.vm.ViewModelData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context:Context):Context{
        return context
    }
    @Singleton
    @Provides
    fun provideDataRepository(@ApplicationContext context: Context):DataRepository{
        return DataRepository(context)
    }
    @Singleton
    @Provides
    fun provideViewModelData(dataRepository: DataRepository):ViewModelData{
        return ViewModelData(dataRepository)
    }
}