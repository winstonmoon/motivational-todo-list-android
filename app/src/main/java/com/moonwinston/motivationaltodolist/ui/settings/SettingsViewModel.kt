package com.moonwinston.motivationaltodolist.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject internal constructor(
    private val taskRepository: TaskRepository
    ) : ViewModel() {

    private var _tasksListLiveData = MutableLiveData<List<TaskEntity>>()
    val tasksListLiveData: LiveData<List<TaskEntity>>
        get() = _tasksListLiveData

//    fun getAllFutureTasks() = viewModelScope.launch {
//        val list = taskRepository.getAllFutureTasks(System.currentTimeMillis())
//        val sortedList = list.sortedBy { it.taskDate }
//        _tasksListLiveData.value = sortedList.map {
//            TaskEntity(
//                uid = it.uid,
//                taskDate = it.taskDate,
//                taskTime = it.taskTime,
//                task = it.task,
//                isCompleted = it.isCompleted
//            )
//        }
//    }
}