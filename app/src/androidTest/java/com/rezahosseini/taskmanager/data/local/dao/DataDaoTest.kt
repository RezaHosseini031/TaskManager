
package com.rezahosseini.taskmanager.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rezahosseini.taskmanager.data.local.factory.DataEntityFactory
import com.rezahosseini.taskmanager.model.local.dao.DataDao
import com.rezahosseini.taskmanager.model.local.database.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import android.content.Context

@RunWith(AndroidJUnit4::class)
class DataDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: DataDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.dataDao()
    }

    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun getAllData(){
        assertNotNull(dao.getAllData())
    }
    @Test
    fun getDataById()= runBlocking {
        val task = DataEntityFactory.create(
            id=1
        )
        dao.insertData(task)

        val result = dao.getDataById(1)
        assertNotNull(result)
    }
    @Test
    fun getDataById_isNull() {

        val result = dao.getDataById(999L)

        assertNull(result)
    }
    @Test
    fun getDataByDone() = runBlocking {
        val task = DataEntityFactory.create(
            id=1,
            done=false
        )

        dao.insertData(task)

        val result=dao.getDataByDone(false)
        assertNotNull(result)

    }
    @Test
    fun getDataByName() = runBlocking {
        val task = DataEntityFactory.create(
            name="NameTest"
        )
        dao.insertData(task)
        val result=dao.getDataByName("NameTest")
        assertNotNull(result)
    }
    @Test
    fun getDataByAbout()= runBlocking{
        val task = DataEntityFactory.create(
            about="AboutTest"
        )
        dao.insertData(task)
        val result=dao.getDataByAbout("AboutTest")
        assertNotNull(result)

    }
    @Test
    fun getDataByPriority()=runBlocking{
        val task = DataEntityFactory.create(
            priority=1
        )
        dao.insertData(task)
        val result=dao.getDataByPriority(1)
        assertNotNull(result)
    }
    private suspend fun insertFilterTestData() {

        dao.insertData(
            DataEntityFactory.create(
                id = 1L,
                name = "Charlie",
                priority = 1
            )
        )

        dao.insertData(
            DataEntityFactory.create(
                id = 2L,
                name = "Alpha",
                priority = 3
            )
        )

        dao.insertData(
            DataEntityFactory.create(
                id = 3L,
                name = "Bravo",
                priority = 2
            )
        )
    }

    @Test
    fun getFiltered_shouldSortNamesAscending() = runBlocking {

        insertFilterTestData()

        val result = dao.getFiltered("", "AZ").first()

        assertEquals(
            listOf("Alpha", "Bravo", "Charlie"),
            result.map { it.name }
        )
    }

    @Test
    fun getFiltered_shouldSortNamesDescending() = runBlocking {

        insertFilterTestData()

        val result = dao.getFiltered("", "ZA").first()

        assertEquals(
            listOf("Charlie", "Bravo", "Alpha"),
            result.map { it.name }
        )
    }

    @Test
    fun getFiltered_shouldSortPriorityDescending() = runBlocking {

        insertFilterTestData()

        val result = dao.getFiltered("", "PRIO_HIGH").first()

        assertEquals(
            listOf(3, 2, 1),
            result.map { it.priority }
        )
    }

    @Test
    fun getFiltered_shouldSortPriorityAscending() = runBlocking {

        insertFilterTestData()

        val result = dao.getFiltered("", "PRIO_LOW").first()

        assertEquals(
            listOf(1, 2, 3),
            result.map { it.priority }
        )
    }

    @Test
    fun getFiltered_shouldSortNewestFirst() = runBlocking {

        insertFilterTestData()

        val result = dao.getFiltered("", "DATE_NEW").first()

        assertEquals(
            listOf(3L, 2L, 1L),
            result.map { it.id }
        )
    }

    @Test
    fun getFiltered_shouldSortOldestFirst() = runBlocking {

        insertFilterTestData()

        val result = dao.getFiltered("", "DATE_OLD").first()

        assertEquals(
            listOf(1L, 2L, 3L),
            result.map { it.id }
        )
    }

    @Test
    fun getFiltered_shouldFindPartialName() = runBlocking {

        dao.insertData(
            DataEntityFactory.create(
                id = 1L,
                name = "Android Development"
            )
        )

        dao.insertData(
            DataEntityFactory.create(
                id = 2L,
                name = "Kotlin Programming"
            )
        )

        val result = dao.getFiltered("Android", "AZ").first()

        assertEquals(1, result.size)
        assertEquals("Android Development", result.first().name)
    }

    @Test
    fun getFiltered_shouldReturnEmptyListWhenNoMatch() = runBlocking {

        dao.insertData(
            DataEntityFactory.create(
                id = 1L,
                name = "Android"
            )
        )

        val result = dao.getFiltered("Flutter", "AZ").first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun getFiltered_shouldReturnAllTasksWhenQueryIsEmpty() = runBlocking {

        insertFilterTestData()

        val result = dao.getFiltered("", "AZ").first()

        assertEquals(3, result.size)
    }

    @Test
    fun getFiltered_shouldSearchAndSortTogether() = runBlocking {

        dao.insertData(
            DataEntityFactory.create(
                id = 1L,
                name = "Android Studio"
            )
        )

        dao.insertData(
            DataEntityFactory.create(
                id = 2L,
                name = "Android Development"
            )
        )

        dao.insertData(
            DataEntityFactory.create(
                id = 3L,
                name = "Kotlin"
            )
        )

        val result = dao.getFiltered("Android", "AZ").first()

        assertEquals(
            listOf(
                "Android Development",
                "Android Studio"
            ),
            result.map { it.name }
        )
    }

    @Test
    fun getAllData_shouldEmitUpdatedDataAfterInsert() = runBlocking {

        val initialResult = dao.getAllData().first()

        assertTrue(initialResult.isEmpty())

        dao.insertData(
            DataEntityFactory.create(
                id = 1L,
                name = "New Task"
            )
        )

        val updatedResult = dao.getAllData().first()

        assertEquals(1, updatedResult.size)
        assertEquals("New Task", updatedResult.first().name)
    }


    @Test
    fun insertData_shouldSaveTask() = runBlocking {

        val task = DataEntityFactory.create(
            name = "Android Testing"
        )

        dao.insertData(task)

        val result = dao.getAllData().first()

        assertEquals(1, result.size)
        assertEquals("Android Testing", result.first().name)
    }

    @Test
    fun updateData_shouldUpdateExistingTask() = runBlocking {

        val task = DataEntityFactory.create(
            id = 1L,
            name = "Old Name",
            priority = 1
        )

        dao.insertData(task)

        val updatedTask = task.copy(
            name = "New Name",
            priority = 3
        )

        dao.updateData(updatedTask)

        val result = dao.getDataById(1L)

        assertNotNull(result)
        assertEquals("New Name", result?.name)
        assertEquals(3, result?.priority)
    }

    @Test
    fun deleteData_shouldRemoveTask() = runBlocking {

        val task = DataEntityFactory.create(
            id = 1L,
            name = "Delete Task"
        )

        dao.insertData(task)

        dao.deleteData(task)

        val result = dao.getAllData().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun deleteDataById_shouldRemoveCorrectTask() = runBlocking {

        val firstTask = DataEntityFactory.create(
            id = 1L,
            name = "First Task"
        )

        val secondTask = DataEntityFactory.create(
            id = 2L,
            name = "Second Task"
        )

        dao.insertData(firstTask)
        dao.insertData(secondTask)

        dao.deleteDataById(1L)

        val result = dao.getAllData().first()

        assertEquals(1, result.size)
        assertEquals(2L, result.first().id)
    }

    @Test
    fun deleteAll_shouldRemoveAllTasks() = runBlocking {

        dao.insertData(
            DataEntityFactory.create(id = 1L)
        )

        dao.insertData(
            DataEntityFactory.create(id = 2L)
        )

        dao.insertData(
            DataEntityFactory.create(id = 3L)
        )

        dao.deleteAll()

        val result = dao.getAllData().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun deleteByDone_shouldRemoveOnlyCompletedTasks() = runBlocking {

        dao.insertData(
            DataEntityFactory.create(
                id = 1L,
                name = "Completed Task",
                done = true
            )
        )

        dao.insertData(
            DataEntityFactory.create(
                id = 2L,
                name = "Uncompleted Task",
                done = false
            )
        )

        dao.deleteByDone(1)

        val result = dao.getAllData().first()

        assertEquals(1, result.size)
        assertEquals("Uncompleted Task", result.first().name)
    }



}
