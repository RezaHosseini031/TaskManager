package com.rezahosseini.taskmanager.data.local.repository

import com.rezahosseini.taskmanager.model.local.DataEntity
import com.rezahosseini.taskmanager.model.local.dao.DataDao
import com.rezahosseini.taskmanager.model.local.repository.DataRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DataRepositoryTest {

    private lateinit var dao: DataDao
    private lateinit var repository: DataRepository

    private val testData = DataEntity(
        id = 1L,
        name = "Test Task",
        about = "Test Description",
        done = false,
        priority = 1
    )

    @Before
    fun setup() {
        dao = mock()
        repository = DataRepository(dao)
    }

    // --------------------------------------------------
    // GET
    // --------------------------------------------------

    @Test
    fun getAllData_returnsDataFromDao() = runTest {
        val expected = listOf(testData)

        whenever(dao.getAllData())
            .thenReturn(flowOf(expected))

        val result = repository.getAllData().first()

        assertEquals(expected, result)
        verify(dao).getAllData()
    }

    @Test
    fun getFiltered_returnsFilteredDataFromDao() = runTest {
        val query = "Test"
        val sort = "AZ"
        val expected = listOf(testData)

        whenever(dao.getFiltered(query, sort))
            .thenReturn(flowOf(expected))

        val result = repository.getFiltered(query, sort).first()

        assertEquals(expected, result)
        verify(dao).getFiltered(query, sort)
    }

    // --------------------------------------------------
    // INSERT
    // --------------------------------------------------

    @Test
    fun insertData_delegatesDataToDao() = runTest {
        repository.insertData(testData)

        verify(dao).insertData(testData)
    }

    // --------------------------------------------------
    // UPDATE
    // --------------------------------------------------

    @Test
    fun updateData_delegatesDataToDao() = runTest {
        repository.updateData(testData)

        verify(dao).updateData(testData)
    }

    // --------------------------------------------------
    // DELETE
    // --------------------------------------------------

    @Test
    fun deleteData_delegatesDataToDao() = runTest {
        repository.deleteData(testData)

        verify(dao).deleteData(testData)
    }

    @Test
    fun deleteDataById_delegatesIdToDao() = runTest {
        val id = 10L

        repository.deleteDataById(id)

        verify(dao).deleteDataById(id)
    }

    @Test
    fun deleteAll_delegatesToDao() = runTest {
        repository.deleteAll()

        verify(dao).deleteAll()
    }

    @Test
    fun deleteByDone_delegatesDoneValueToDao() = runTest {
        val done = true

        repository.deleteByDone(done)

        verify(dao).deleteByDone(done)
    }
}
