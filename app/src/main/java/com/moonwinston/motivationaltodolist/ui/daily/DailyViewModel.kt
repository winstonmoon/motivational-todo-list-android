package com.moonwinston.motivationaltodolist.ui.daily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*

class DailyViewModel(private val taskRepository: TaskRepository) : BaseViewModel() {

    fun insert(taskEntity: TaskEntity) = viewModelScope.launch {
        taskRepository.insertTask(taskEntity)
    }

    fun delete(uid: Long) = viewModelScope.launch {
        taskRepository.deleteTask(uid)
    }

    fun getRate(tasksList: List<TaskEntity>): Float {
        var totalTasks: Float = 0F
        var doneTasks: Float = 0F
        for (task in tasksList) {
            totalTasks += 1
            if (task.isCompleted) doneTasks += 1
        }
        return if (doneTasks == 0F) 0F else doneTasks / totalTasks
    }
}