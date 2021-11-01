package com.moonwinston.motivationaltodolist.ui.shared

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
    private var _tasksListLiveData = MutableLiveData<List<TaskEntity>>()
    val tasksListLiveData: LiveData<List<TaskEntity>>
        get() = _tasksListLiveData

    fun getAll() = viewModelScope.launch {
        val list = taskRepository.getAllTasks()
        val sortedList = list.sortedBy { it.taskDate }
        _tasksListLiveData.value = sortedList.map {
            TaskEntity(
                uid = it.uid,
                taskDate = it.taskDate,
                taskTime = it.taskTime,
                task = it.task,
                isCompleted = it.isCompleted
            )
        }
    }

    private var _multipleDaysTasksList = MutableLiveData<List<TaskEntity>>()
    val multipleDaysTasksList: LiveData<List<TaskEntity>>
        get() = _multipleDaysTasksList

    fun getAllByDates(taskDatesList: MutableList<Date>) = viewModelScope.launch {
        val list = taskRepository.getAllTasksByDates(taskDatesList)
        val sortedList = list.sortedBy { it.taskDate }
        _multipleDaysTasksList.value = sortedList.map {
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
        //TODO
        getAll()
    }

    fun delete(uid: Long) = viewModelScope.launch {
        taskRepository.deleteTask(uid)
        //TODO
        getAll()
    }

    private var _monthlyTitleLiveData = MutableLiveData<List<Int>>()
    val monthlyTitleLiveData: LiveData<List<Int>>
        get() = _monthlyTitleLiveData

    fun setMonthlyTitle(month: Int, year: Int) {
        _monthlyTitleLiveData.value = listOf(year, month)
    }

    private var _selectedDateLiveData = MutableLiveData<Date>()
    val selectedDateLiveData: LiveData<Date>
        get() = _selectedDateLiveData

    fun setSelectedDate(selectedDate: Date) {
        _selectedDateLiveData.value = selectedDate
    }

    fun getRate(tasksList: List<TaskEntity>): Float {
        var totalTasks: Float = 0F
        var doneTasks: Float = 0F
        for (task in tasksList) {
            totalTasks += 1F
            if (task.isCompleted) doneTasks += 1F
        }
        return if (doneTasks == 0F) 0F else doneTasks / totalTasks
    }
}