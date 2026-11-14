package com.rezahosseini.taskmanager.vm.event

sealed interface TaskEvent {

    data class OnTaskChecked(
        val taskId: Long,
        val isCompleted: Boolean
    ) : TaskEvent

    data class OnEditTask(
        val taskId: Long
    ) : TaskEvent

    data class OnDeleteTask(
        val taskId: Long
    ) : TaskEvent
}