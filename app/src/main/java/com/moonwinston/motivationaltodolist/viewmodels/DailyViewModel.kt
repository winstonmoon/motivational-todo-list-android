package com.moonwinston.motivationaltodolist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import kotlinx.coroutines.launch
import java.util.*

class DailyViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private var _todayTaskListLiveData = MutableLiveData<List<TaskEntity>>()
    val todayTaskListLiveData: LiveData<List<TaskEntity>>
        get() = _todayTaskListLiveData

    fun getAllByDate(taskDate: Date) = viewModelScope.launch {
        val list = taskRepository.getAllTasksByDate(taskDate)
        val sortedList = list.sortedBy { it.taskTime }
        _todayTaskListLiveData.value = sortedList.map {
            TaskEntity(
                uid = it.uid,
                taskDate = it.taskDate,
                taskTime = it.taskTime,
                task = it.task,
                isCompleted = it.isCompleted
            )
        }
    }

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