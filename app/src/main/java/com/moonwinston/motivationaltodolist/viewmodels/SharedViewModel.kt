package com.moonwinston.motivationaltodolist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                isCompleted = it.isCompleted
            )
        }
    }

    fun insert(taskEntity: TaskEntity) = viewModelScope.launch {
        taskRepository.insertTask(taskEntity)
    }

    fun deleteTasks(uid: Long) = viewModelScope.launch {
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

    //TODO fix
    suspend fun getRate(date: Date) =
        withContext(viewModelScope.coroutineContext) {
            var allNumber: Int = 0
            var doneNumber: Int = 0
            val list = taskRepository.getTasks(date)
            for (a in list) {
                allNumber += 1
                if (a.isCompleted) {
                    doneNumber += 1
                }
            }
            allNumber / doneNumber
        }

    private var _selectedTaskListLiveData = MutableLiveData<List<TaskEntity>>()
    val selectedTaskListLiveData: LiveData<List<TaskEntity>>
        get() = _selectedTaskListLiveData

    fun updateTasks(taskEntity: TaskEntity) = viewModelScope.launch {
        val list = taskRepository.updateTask(taskEntity)
    }

    private var _deleteTaskListLiveData = MutableLiveData<List<TaskEntity>>()
    val deleteTaskListLiveData: LiveData<List<TaskEntity>>
        get() = _deleteTaskListLiveData
}