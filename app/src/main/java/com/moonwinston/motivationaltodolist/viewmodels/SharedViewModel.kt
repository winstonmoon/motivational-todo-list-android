package com.moonwinston.motivationaltodolist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.data.CalendarDate
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import kotlinx.coroutines.launch
import java.util.*

class SharedViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private var _singleDayTasksListLiveData = MutableLiveData<List<TaskEntity>>()
    val singleDayTasksListLiveData: LiveData<List<TaskEntity>>
        get() = _singleDayTasksListLiveData

    fun getAllByDate(taskDate: Date) = viewModelScope.launch {
        val list = taskRepository.getAllTasksByDate(taskDate)
        val sortedList = list.sortedBy { it.taskTime }
        _singleDayTasksListLiveData.value = sortedList.map {
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

//    fun getAllByDates(taskDatesList: MutableList<CalendarDate>) = viewModelScope.launch {
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
    }

    fun delete(uid: Long) = viewModelScope.launch {
        taskRepository.deleteTask(uid)
    }

    private var _monthlyTitleLiveData = MutableLiveData<String>()
    val monthlyTitleLiveData: LiveData<String>
        get() = _monthlyTitleLiveData

    fun setMonthlyTitle(month: Int, year: Int) = viewModelScope.launch {
        val parsedMonth = MonthEnum.values()[month].name
        _monthlyTitleLiveData.value = "$parsedMonth $year"
    }

    private var _isMondaySelectedLiveData = MutableLiveData<Date>()
    val isMondaySelectedLiveData: LiveData<Date>
        get() = _isMondaySelectedLiveData

    fun selectMonday(weeklyTitle: Date) = viewModelScope.launch {
        _isMondaySelectedLiveData.value = weeklyTitle
    }

    private var _isTuesdaySelectedLiveData = MutableLiveData<Date>()
    val isTuesdaySelectedLiveData: LiveData<Date>
        get() = _isTuesdaySelectedLiveData

    fun selectTuesday(weeklyTitle: Date) = viewModelScope.launch {
        _isTuesdaySelectedLiveData.value = weeklyTitle
    }

    private var _isWednesdaySelectedLiveData = MutableLiveData<Date>()
    val isWednesdaySelectedLiveData: LiveData<Date>
        get() = _isWednesdaySelectedLiveData

    fun selectWednesday(weeklyTitle: Date) = viewModelScope.launch {
        _isWednesdaySelectedLiveData.value = weeklyTitle
    }

    private var _isThursdaySelectedLiveData = MutableLiveData<Date>()
    val isThursdaySelectedLiveData: LiveData<Date>
        get() = _isThursdaySelectedLiveData

    fun selectThursday(weeklyTitle: Date) = viewModelScope.launch {
        _isThursdaySelectedLiveData.value = weeklyTitle
    }

    private var _isFridaySelectedLiveData = MutableLiveData<Date>()
    val isFridaySelectedLiveData: LiveData<Date>
        get() = _isFridaySelectedLiveData

    fun selectFriday(weeklyTitle: Date) = viewModelScope.launch {
        _isFridaySelectedLiveData.value = weeklyTitle
    }

    private var _isSaturdaySelectedLiveData = MutableLiveData<Date>()
    val isSaturdaySelectedLiveData: LiveData<Date>
        get() = _isSaturdaySelectedLiveData

    fun selectSaturday(weeklyTitle: Date) = viewModelScope.launch {
        _isSaturdaySelectedLiveData.value = weeklyTitle
    }

    private var _isSundaySelectedLiveData = MutableLiveData<Date>()
    val isSundaySelectedLiveData: LiveData<Date>
        get() = _isSundaySelectedLiveData

    fun selectSunday(weeklyTitle: Date) = viewModelScope.launch {
        _isSundaySelectedLiveData.value = weeklyTitle
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