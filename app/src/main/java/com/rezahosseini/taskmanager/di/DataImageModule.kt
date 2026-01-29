package com.rezahosseini.taskmanager.di

import com.rezahosseini.taskmanager.model.network.repository.DataImageProviderImpl
import com.rezahosseini.taskmanager.model.network.web.DataImageService
import com.rezahosseini.taskmanager.model.network.web.WebService
import com.rezahosseini.taskmanager.model.network.web.mapper.WebDataImageMapper
import com.rezahosseini.taskmanager.vm.ViewModelDataImage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataImageModule {
    @Provides
    fun provideDataImageService(webService: WebService):DataImageService{
        return webService.getDataImageService()
    }
    @Singleton
    @Provides
    fun provideDataImageProviderImpl(dataImageService: DataImageService,webDataImageMapper: WebDataImageMapper):DataImageProviderImpl{
        return DataImageProviderImpl(dataImageService,webDataImageMapper)
    }
    @Singleton
    @Provides
    fun provideWebDataImageMapper():WebDataImageMapper{
        return WebDataImageMapper()
    }
    @Singleton
    @Provides
    fun dataImageViewModel(dataImageProviderImpl: DataImageProviderImpl):ViewModelDataImage{
        return ViewModelDataImage(dataImageProviderImpl)
    }


}