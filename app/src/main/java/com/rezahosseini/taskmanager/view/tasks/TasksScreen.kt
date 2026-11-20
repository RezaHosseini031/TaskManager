package com.rezahosseini.taskmanager.view.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import coil3.compose.AsyncImage
import com.rezahosseini.taskmanager.vm.ViewModelDataImage
import com.rezahosseini.taskmanager.vm.state.UiState
import kotlin.random.Random

@Composable
fun TasksScreen(
    viewModel: ViewModelData= hiltViewModel(),
    viewModelImage: ViewModelDataImage = hiltViewModel()
) {
    val onEvent: (TaskEvent) -> Unit = viewModel::onEvent

    val dataStateFlow by viewModel.dataStateFlow.collectAsStateWithLifecycle()
    val selectedTask by viewModel.selectedTask.collectAsStateWithLifecycle()
    val imageFlatState by viewModelImage.getImages.collectAsStateWithLifecycle()
    var showAddDialog by remember {
        mutableStateOf(false)
    }
    val query by viewModel.query.collectAsStateWithLifecycle()
    val sort by viewModel.sort.collectAsStateWithLifecycle()
    var diceRoll by remember { mutableLongStateOf(Random.nextLong(1, 11)) }
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
                        diceRoll = Random.nextLong(1, 11)
                    }
                ) {
                    when(val state = imageFlatState){
                        is UiState.Success ->{
                            val currentIcon = state.data.find { it.id == diceRoll }

                            if (currentIcon != null) {
                                AsyncImage(
                                    model = "https://raw.githubusercontent.com/RezaHosseini031/TaskManager/refs/heads/main/app/src/main/assets/${currentIcon.uriImage}",
                                    contentDescription = "Random Icon",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        else -> {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Task"
                            )
                        }
                    }
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
                        item{
                            CircularProgressIndicator()
                        }
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
                        item{
                            Column {
                                Text(state.message)
                            }
                        }

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