package com.moonwinston.motivationaltodolist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import kotlinx.coroutines.launch
import java.util.*

class SharedViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private var _todayTaskListLiveData = MutableLiveData<List<TaskEntity>>()
    val todayTaskListLiveData: LiveData<List<TaskEntity>>
        get() = _todayTaskListLiveData

    fun getTasks(taskDate: Date) = viewModelScope.launch {
        val list = taskRepository.getTasks(taskDate)
        val sortedList = list.sortedBy { it.taskTime }
        _todayTaskListLiveData.value = sortedList.map {
            TaskEntity(
                uid = it.uid,
                taskDate = it.taskDate,
                taskTime = it.taskTime,
                task = it.task,
                isGoalSet = it.isGoalSet,
                isCompleted = it.isCompleted
            )
        }
    }

    fun insert(taskEntity: TaskEntity) = viewModelScope.launch {
        taskRepository.insertTask(taskEntity)
    }

    private var _monthlyTitle = MutableLiveData<String>()
    val monthlyTitle: LiveData<String>
        get() = _monthlyTitle

    fun setMonthlyTitle(month: Int, year: Int) = viewModelScope.launch {
        val parsedMonth = MonthEnum.values()[month].name
        _monthlyTitle.value = "$parsedMonth $year"
    }
}