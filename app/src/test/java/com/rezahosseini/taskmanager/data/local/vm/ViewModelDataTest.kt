package com.rezahosseini.taskmanager.data.local.vm

import com.rezahosseini.taskmanager.testutil.MainDispatcherRule
import com.rezahosseini.taskmanager.model.local.DataEntity
import com.rezahosseini.taskmanager.model.local.repository.DataRepository
import com.rezahosseini.taskmanager.vm.ViewModelData
import com.rezahosseini.taskmanager.vm.event.TaskEvent
import com.rezahosseini.taskmanager.vm.state.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelDataTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: DataRepository
    private lateinit var viewModel: ViewModelData

    private val task = DataEntity(
        id = 1L,
        name = "Test Task",
        about = "Test Description",
        done = false,
        priority = 2
    )

    private val secondTask = DataEntity(
        id = 2L,
        name = "Another Task",
        about = "Another Description",
        done = true,
        priority = 1
    )

    @Before
    fun setup() {
        repository = mock()

        whenever(repository.getAllData())
            .thenReturn(flowOf(listOf(task, secondTask)))

        whenever(repository.getFiltered(any(), any()))
            .thenReturn(flowOf(listOf(task, secondTask)))

        viewModel = ViewModelData(repository)
    }

    // --------------------------------------------------
    // ALL DATA STATE
    // --------------------------------------------------

    @Test
    fun allDataState_emitsSuccessWithRepositoryData() = runTest {
        advanceUntilIdle()

        val result = viewModel.allDataState.value

        assertTrue(result is UiState.Success)

        result as UiState.Success

        assertEquals(
            listOf(task, secondTask),
            result.data
        )

        verify(repository).getAllData()
    }

    @Test
    fun allDataState_emitsErrorWhenRepositoryFails() = runTest {
        val errorMessage = "Database error"

        whenever(repository.getAllData())
            .thenReturn(
                flow {
                    throw IllegalStateException(errorMessage)
                }
            )

        viewModel = ViewModelData(repository)

        advanceUntilIdle()

        val result = viewModel.allDataState.value

        assertTrue(result is UiState.Error)

        result as UiState.Error

        assertEquals(errorMessage, result.message)
    }

    // --------------------------------------------------
    // FILTERED DATA STATE
    // --------------------------------------------------


    @Test
    fun dataStateFlow_emitsSuccessWithFilteredData() = runTest {
        val result = viewModel.dataStateFlow.first {
            it is UiState.Success
        }

        assertTrue(result is UiState.Success)

        result as UiState.Success

        assertEquals(
            listOf(task, secondTask),
            result.data
        )

        verify(repository).getFiltered("", "AZ")
    }

    @Test
    fun setQuery_updatesRepositoryFilter() = runTest {
        viewModel.setQuery("Test")

        val result = viewModel.dataStateFlow.first {
            it is UiState.Success
        }

        assertTrue(result is UiState.Success)
        assertEquals("Test", viewModel.query.value)

        verify(repository).getFiltered("Test", "AZ")
    }

    @Test
    fun setSort_updatesRepositorySort() = runTest {
        viewModel.setSort("ZA")

        val result = viewModel.dataStateFlow.first {
            it is UiState.Success
        }

        assertTrue(result is UiState.Success)
        assertEquals("ZA", viewModel.sort.value)

        verify(repository).getFiltered("", "ZA")
    }


    // --------------------------------------------------
    // INSERT
    // --------------------------------------------------

    @Test
    fun insert_callsRepositoryInsert() = runTest {
        viewModel.insert(task)

        advanceUntilIdle()

        verify(repository).insertData(task)
    }

    // --------------------------------------------------
    // UPDATE
    // --------------------------------------------------

    @Test
    fun update_callsRepositoryUpdate() = runTest {
        viewModel.update(task)

        advanceUntilIdle()

        verify(repository).updateData(task)
    }

    // --------------------------------------------------
    // DELETE
    // --------------------------------------------------

    @Test
    fun delete_callsRepositoryDelete() = runTest {
        viewModel.delete(task)

        advanceUntilIdle()

        verify(repository).deleteData(task)
    }

    @Test
    fun deleteDataById_callsRepositoryWithCorrectId() = runTest {
        viewModel.deleteDataById(10L)

        advanceUntilIdle()

        verify(repository).deleteDataById(10L)
    }

    @Test
    fun deleteAll_callsRepositoryDeleteAll() = runTest {
        viewModel.deleteAll()

        advanceUntilIdle()

        verify(repository).deleteAll()
    }

    @Test
    fun deleteByDone_callsRepositoryWithCorrectValue() = runTest {
        viewModel.deleteByDone(true)

        advanceUntilIdle()

        verify(repository).deleteByDone(true)
    }

    // --------------------------------------------------
    // SELECTED TASK
    // --------------------------------------------------

    @Test
    fun selectedTask_isNullInitially() = runTest {
        assertNull(viewModel.selectedTask.value)
    }

    @Test
    fun onEditTask_selectsCorrectTask() = runTest {
        advanceUntilIdle()

        viewModel.onEvent(
            TaskEvent.OnEditTask(task.id)
        )

        assertEquals(
            task,
            viewModel.selectedTask.value
        )
    }

    @Test
    fun onEditTask_doesNotSelectUnknownTask() = runTest {
        advanceUntilIdle()

        viewModel.onEvent(
            TaskEvent.OnEditTask(999L)
        )

        assertNull(viewModel.selectedTask.value)
    }

    @Test
    fun clearSelectedTask_clearsSelection() = runTest {
        advanceUntilIdle()

        viewModel.onEvent(
            TaskEvent.OnEditTask(task.id)
        )

        viewModel.clearSelectedTask()

        assertNull(viewModel.selectedTask.value)
    }

    // --------------------------------------------------
    // TASK EVENTS
    // --------------------------------------------------

    @Test
    fun onTaskChecked_updatesTaskDoneStatus() = runTest {
        advanceUntilIdle()

        viewModel.onEvent(
            TaskEvent.OnTaskChecked(
                taskId = task.id,
                isCompleted = true
            )
        )

        advanceUntilIdle()

        verify(repository).updateData(
            task.copy(done = true)
        )
    }

    @Test
    fun onDeleteTask_deletesTaskById() = runTest {
        viewModel.onEvent(
            TaskEvent.OnDeleteTask(task.id)
        )

        advanceUntilIdle()

        verify(repository).deleteDataById(task.id)
    }
}
