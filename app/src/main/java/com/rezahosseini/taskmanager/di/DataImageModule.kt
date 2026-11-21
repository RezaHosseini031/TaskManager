
package com.rezahosseini.taskmanager.di

import com.rezahosseini.taskmanager.model.network.repository.DataImageProvider
import com.rezahosseini.taskmanager.model.network.repository.DataImageProviderImpl
import com.rezahosseini.taskmanager.model.network.web.DataImageService
import com.rezahosseini.taskmanager.model.network.web.WebService
import com.rezahosseini.taskmanager.model.network.web.mapper.WebDataImageMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataImageModule {

    @Binds
    @Singleton
    abstract fun bindDataImageProvider(
        implementation: DataImageProviderImpl
    ): DataImageProvider

    companion object {

        @Provides
        fun provideDataImageService(
            webService: WebService
        ): DataImageService {
            return webService.getDataImageService()
        }

        @Provides
        @Singleton
        fun provideWebDataImageMapper(): WebDataImageMapper {
            return WebDataImageMapper()
        }
    }
}
