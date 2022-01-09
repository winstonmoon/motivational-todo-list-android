package com.moonwinston.motivationaltodolist.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.data.AchievementRateRepository
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import kotlinx.coroutines.launch
import java.util.*

class SharedViewModel(
    private val taskRepository: TaskRepository,
    private val achievementRateRepository: AchievementRateRepository
) : ViewModel() {
    private var _tasksListLiveData = MutableLiveData<List<TaskEntity>>()
    val tasksListLiveData: LiveData<List<TaskEntity>>
        get() = _tasksListLiveData

    fun getAllTasks() = viewModelScope.launch {
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

    fun getAllTasksByDates(taskDatesList: MutableList<Date>) = viewModelScope.launch {
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

    private var _rateListLiveData = MutableLiveData<List<AchievementRateEntity>>()
    val rateListLiveData: LiveData<List<AchievementRateEntity>>
        get() = _rateListLiveData

    fun getAllComplete() = viewModelScope.launch {
        val list = achievementRateRepository.getAllCompleteRate()
        val sortedList = list.sortedBy { it.date }
        _rateListLiveData.value = sortedList.map {
            AchievementRateEntity(
                date = it.date,
                rate = it.rate
            )
        }
    }

    fun insertTask(taskEntity: TaskEntity) = viewModelScope.launch {
        taskRepository.insertTask(taskEntity)
        //TODO
        getAllTasks()
    }

    fun deleteTask(uid: Long) = viewModelScope.launch {
        taskRepository.deleteTask(uid)
        //TODO
        getAllTasks()
    }

    fun insertAchievementRate(achievementRateEntity: AchievementRateEntity) = viewModelScope.launch {
        achievementRateRepository.insertRate(achievementRateEntity)
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

    private var _rateLiveData = MutableLiveData<Float>()
    val rateLiveData: LiveData<Float>
    get() = _rateLiveData

    fun setRate(tasksList: List<TaskEntity>) {
        var totalTasks = 0F
        var doneTasks = 0F
        for (task in tasksList) {
            totalTasks += 1F
            if (task.isCompleted) doneTasks += 1F
        }
        _rateLiveData.value = if (doneTasks == 0F) 0F else doneTasks / totalTasks
    }

    fun getRate(tasksList: List<TaskEntity>): Float {
        var totalTasks = 0F
        var doneTasks = 0F
        for (task in tasksList) {
            totalTasks += 1F
            if (task.isCompleted) doneTasks += 1F
        }
        return if (doneTasks == 0F) 0F else doneTasks / totalTasks
    }
}