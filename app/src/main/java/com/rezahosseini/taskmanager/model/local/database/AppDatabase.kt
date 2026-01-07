package com.rezahosseini.taskmanager.model.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rezahosseini.taskmanager.model.local.DataEntity
import com.rezahosseini.taskmanager.model.local.dao.DataDao

@Database(
    entities = [DataEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(), DataDao {

    abstract fun dataDao(): DataDao

    companion object {
        private const val DATABASE_NAME = "databaseTaskManager"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build().also { INSTANCE = it }
            }
    }
}
