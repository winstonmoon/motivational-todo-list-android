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

    private var _monthlyTitleLiveData = MutableLiveData<String>()
    val monthlyTitleLiveData: LiveData<String>
        get() = _monthlyTitleLiveData

    fun setMonthlyTitle(month: Int, year: Int) = viewModelScope.launch {
        val parsedMonth = MonthEnum.values()[month].name
        _monthlyTitleLiveData.value = "$parsedMonth $year"
    }

    private var _isMondaySelectedLiveData = MutableLiveData<Boolean>()
    val isMondaySelectedLiveData: LiveData<Boolean>
        get() = _isMondaySelectedLiveData

    fun selectMonday() = viewModelScope.launch {
        _isMondaySelectedLiveData.value = true
    }

    private var _isTuesdaySelectedLiveData = MutableLiveData<Boolean>()
    val isTuesdaySelectedLiveData: LiveData<Boolean>
        get() = _isTuesdaySelectedLiveData

    fun selectTuesday() = viewModelScope.launch {
        _isTuesdaySelectedLiveData.value = true
    }

    private var _isWednesdaySelectedLiveData = MutableLiveData<Boolean>()
    val isWednesdaySelectedLiveData: LiveData<Boolean>
        get() = _isWednesdaySelectedLiveData

    fun selectWednesday() = viewModelScope.launch {
        _isWednesdaySelectedLiveData.value = true
    }

    private var _isThursdaySelectedLiveData = MutableLiveData<Boolean>()
    val isThursdaySelectedLiveData: LiveData<Boolean>
        get() = _isThursdaySelectedLiveData

    fun selectThursday() = viewModelScope.launch {
        _isThursdaySelectedLiveData.value = true
    }

    private var _isFridaySelectedLiveData = MutableLiveData<Boolean>()
    val isFridaySelectedLiveData: LiveData<Boolean>
        get() = _isFridaySelectedLiveData

    fun selectFriday() = viewModelScope.launch {
        _isFridaySelectedLiveData.value = true
    }

    private var _isSaturdaySelectedLiveData = MutableLiveData<Boolean>()
    val isSaturdaySelectedLiveData: LiveData<Boolean>
        get() = _isSaturdaySelectedLiveData

    fun selectSaturday() = viewModelScope.launch {
        _isSaturdaySelectedLiveData.value = true
    }

    private var _isSundaySelectedLiveData = MutableLiveData<Boolean>()
    val isSundaySelectedLiveData: LiveData<Boolean>
        get() = _isSundaySelectedLiveData

    fun selectSunday() = viewModelScope.launch {
        _isSundaySelectedLiveData.value = true
    }
}