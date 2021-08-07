package com.moonwinston.motivationaltodolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import kotlinx.coroutines.launch
import java.util.*

class SharedViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    fun insert(taskEntity: TaskEntity) = viewModelScope.launch {
        taskRepository.insertTask(taskEntity)
    }

    fun getTasks(taskDate: Date) = viewModelScope.launch {
        taskRepository.getTasks(taskDate)
    }
}