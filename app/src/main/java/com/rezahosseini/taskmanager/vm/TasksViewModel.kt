package com.rezahosseini.taskmanager.vm

import androidx.lifecycle.ViewModel
import com.rezahosseini.taskmanager.vm.event.TaskEvent

class TasksViewModel : ViewModel(){
    fun onEvent(event: TaskEvent) {
        when (event) {

            is TaskEvent.OnTaskChecked -> {
                // تغییر وضعیت Task
            }

            is TaskEvent.OnEditTask -> {
                // شروع فرآیند ویرایش
            }

            is TaskEvent.OnDeleteTask -> {
                // حذف Task
            }
        }
    }
}