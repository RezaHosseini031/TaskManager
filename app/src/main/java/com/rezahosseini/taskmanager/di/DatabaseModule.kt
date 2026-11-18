package com.rezahosseini.taskmanager.di

import android.content.Context
import androidx.room.Room
import com.rezahosseini.taskmanager.model.local.dao.DataDao
import com.rezahosseini.taskmanager.model.local.database.AppDatabase
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
    fun provideDataRepository( daoData: DataDao):DataRepository{
        return DataRepository(daoData)
    }
    @Singleton
    @Provides
    fun provideViewModelData(dataRepository: DataRepository):ViewModelData{
        return ViewModelData(dataRepository)
    }
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "task_database"
        ).build()
    }

    @Provides
    fun provideDataDao(
        database: AppDatabase
    ): DataDao {
        return database.dataDao()
    }
}