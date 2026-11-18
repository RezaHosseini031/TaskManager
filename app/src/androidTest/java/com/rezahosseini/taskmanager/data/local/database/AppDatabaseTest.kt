package com.rezahosseini.taskmanager.data.local.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rezahosseini.taskmanager.model.local.DataEntity
import com.rezahosseini.taskmanager.model.local.database.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import android.content.Context

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var database: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun database_shouldCreateSuccessfully() {
        assertNotNull(database)
        assertNotNull(database.dataDao())
    }

    @Test
    fun database_shouldInsertAndReadData() = runBlocking {

        val task = DataEntity(
            name = "Database Test",
            about = "Testing Room Database",
            done = false,
            priority = 1
        )

        database.dataDao().insertData(task)

        val result = database.dataDao()
            .getAllData()
            .first()

        assertEquals(1, result.size)
        assertEquals("Database Test", result.first().name)
    }
}
