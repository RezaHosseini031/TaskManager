package com.rezahosseini.taskmanager.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezahosseini.taskmanager.model.local.DataEntity
import com.rezahosseini.taskmanager.model.local.repository.DataRepository
import com.rezahosseini.taskmanager.vm.event.TaskEvent
import com.rezahosseini.taskmanager.vm.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelData @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    private val sortFlow = MutableStateFlow("AZ")
    private val queryFlow = MutableStateFlow("")
    val query = queryFlow.asStateFlow()
    val sort = sortFlow.asStateFlow()
    val allDataState: StateFlow<UiState<List<DataEntity>>> =
        dataRepository.getAllData()
            .map<List<DataEntity>, UiState<List<DataEntity>>> {
                UiState.Success(it)
            }
            .onStart { emit(UiState.Loading) }
            .catch { emit(UiState.Error(it.message ?: "Unknown error")) }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                UiState.Loading
            )
    val dataStateFlow = combine(sortFlow, queryFlow) { sort, query ->
        sort to query
    }.flatMapLatest { (sort, query) ->
        dataRepository.getFiltered(query, sort)
            .map<List<DataEntity>, UiState<List<DataEntity>>> { UiState.Success(it) }
            .onStart { emit(UiState.Loading) }
            .catch { emit(UiState.Error(it.message ?: "Unknown error")) }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)


    fun setSort(sort: String) { sortFlow.value = sort }
    fun setQuery(query: String) { queryFlow.value = query }

    fun insert(data: DataEntity) = viewModelScope.launch { dataRepository.insertData(data) }
    fun update(data: DataEntity) = viewModelScope.launch { dataRepository.updateData(data) }
    private suspend fun updateEvent(data: DataEntity) {
        dataRepository.updateData(data)
    }
    fun delete(data: DataEntity) = viewModelScope.launch { dataRepository.deleteData(data) }
    fun deleteDataById(id: Long) = viewModelScope.launch { dataRepository.deleteDataById(id) }
    fun deleteAll() = viewModelScope.launch { dataRepository.deleteAll() }
    fun deleteByDone(done: Boolean) = viewModelScope.launch { dataRepository.deleteByDone(done) }
    private val _selectedTask = MutableStateFlow<DataEntity?>(null)
    val selectedTask = _selectedTask.asStateFlow()
    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.OnTaskChecked -> {
                viewModelScope.launch {
                    val state = allDataState.value

                    if (state is UiState.Success) {
                        val task = state.data.find {
                            it.id == event.taskId
                        }

                        task?.let {
                            updateEvent(
                                it.copy(
                                    done = event.isCompleted
                                )
                            )
                        }
                    }
                }
            }
            is TaskEvent.OnEditTask -> {
                when (val state = allDataState.value) {
                    is UiState.Success -> {
                        _selectedTask.value = state.data.find {
                            it.id == event.taskId
                        }
                    }

                    else -> {
                        Log.w("ViewModelData", "Tasks are not loaded yet")
                    }
                }
            }

            is TaskEvent.OnDeleteTask -> {
                deleteDataById(event.taskId)
            }

            else -> Unit
        }
    }
    fun clearSelectedTask() {
        _selectedTask.value = null
    }
}

