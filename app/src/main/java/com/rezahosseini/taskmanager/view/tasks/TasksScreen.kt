package com.rezahosseini.taskmanager.view.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rezahosseini.taskmanager.model.local.DataEntity
import com.rezahosseini.taskmanager.R
import com.rezahosseini.taskmanager.vm.ViewModelData
import com.rezahosseini.taskmanager.vm.event.TaskEvent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rezahosseini.taskmanager.vm.state.UiState

@Composable
fun TasksScreen(
    viewModel: ViewModelData= hiltViewModel()
) {
    val onEvent: (TaskEvent) -> Unit = viewModel::onEvent

    val dataStateFlow by viewModel.dataStateFlow.collectAsStateWithLifecycle()
    val selectedTask by viewModel.selectedTask.collectAsStateWithLifecycle()
    var showAddDialog by remember {
        mutableStateOf(false)
    }
    val query by viewModel.query.collectAsStateWithLifecycle()
    val sort by viewModel.sort.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TasksTopBar(
                query = query,
                onQueryChange = viewModel::setQuery,
                onSortChange = viewModel::setSort,

                onDeleteAll = {
                    viewModel.deleteAll()
                },

                onDeleteDone = {
                    viewModel.deleteByDone(1)
                }
            )
        },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showAddDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task"
                    )
                }
            }
            ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
                when (val state = dataStateFlow) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        val listUnDone = state.data.filter { it.done == false }
                        val listDone = state.data.filter { it.done == true }
                        item {
                            Text(
                                text = stringResource(R.string.works),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        items(
                            items = listUnDone,
                            key = { it.id }
                        ) { task ->

                            TaskItem(
                                title = task.name,
                                description = task.about,
                                isCompleted = task.done,
                                priority = task.priority,
                                onCheckedChange = { checked ->
                                    onEvent(
                                        TaskEvent.OnTaskChecked(
                                            taskId = task.id,
                                            isCompleted = checked
                                        )
                                    )
                                },

                                onEditClick = {
                                    onEvent(TaskEvent.OnEditTask(task.id))
                                },

                                onDeleteClick = {
                                    onEvent(TaskEvent.OnDeleteTask(task.id))
                                }
                            )
                        }

                        item {
                            Text(
                                text = "Completed Tasks",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        items(
                            items = listDone,
                            key = { it.id }
                        ) { task ->

                            TaskItem(
                                title = task.name,
                                description = task.about,
                                isCompleted = task.done,
                                priority = task.priority,
                                onCheckedChange = { checked ->
                                    onEvent(
                                        TaskEvent.OnTaskChecked(
                                            taskId = task.id,
                                            isCompleted = checked
                                        )
                                    )
                                },

                                onEditClick = {
                                    onEvent(TaskEvent.OnEditTask(task.id))
                                },

                                onDeleteClick = {
                                    onEvent(TaskEvent.OnDeleteTask(task.id))
                                }
                            )
                        }
                    }
                    is UiState.Error -> {

                    }
                }


        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = {
                showAddDialog = false
            },
            onConfirm = { title, description,priority ->

                val task = DataEntity(
                    name = title,
                    about = description,
                    done = false,
                    priority = priority
                )

                viewModel.insert(task)

                showAddDialog = false
            }
        )
    }
    selectedTask?.let { task ->

        AddTaskDialog(
            initialTitle = task.name,
            initialDescription = task.about,
            initialPriority = task.priority,

            onDismiss = {
                viewModel.clearSelectedTask()
            },

            onConfirm = { title, description, priority ->

                viewModel.update(
                    task.copy(
                        name = title,
                        about = description,
                        priority = priority
                    )
                )

                viewModel.clearSelectedTask()
            }
        )
    }
}