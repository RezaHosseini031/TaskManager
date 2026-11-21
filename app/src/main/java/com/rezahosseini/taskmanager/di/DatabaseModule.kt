
package com.rezahosseini.taskmanager.di

import android.content.Context
import androidx.room.Room
import com.rezahosseini.taskmanager.model.local.dao.DataDao
import com.rezahosseini.taskmanager.model.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getInstance(context
        )
    }

    @Provides
    fun provideDataDao(
        database: AppDatabase
    ): DataDao {
        return database.dataDao()
    }
}
