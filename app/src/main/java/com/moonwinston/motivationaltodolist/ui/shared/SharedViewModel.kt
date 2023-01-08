package com.moonwinston.motivationaltodolist.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject internal constructor(
    private val taskRepository: TaskRepository,
    private val achievementRateRepository: AchievementRateRepository,
    private val userPreferencesRepository: UserPreferencesRepository
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

    private var _monthlyTitleLiveData = MutableLiveData<Pair<Int, Int>>()
    val monthlyTitleLiveData: LiveData<Pair<Int, Int>>
        get() = _monthlyTitleLiveData

    fun setMonthlyTitle(year: Int, month: Int) {
        _monthlyTitleLiveData.value = Pair(year, month)
    }

    private var _selectedDateLiveData = MutableLiveData<Date>()
    val selectedDateLiveData: LiveData<Date>
        get() = _selectedDateLiveData

    fun setSelectedDate(selectedDate: Date) {
        _selectedDateLiveData.value = selectedDate
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


    private val _isCoachWeeklyDismissed = MutableStateFlow(false)
    val isCoachWeeklyDismissed = _isCoachWeeklyDismissed.asStateFlow()

    fun getCoachWeeklyDismissed() {
        viewModelScope.launch {
            userPreferencesRepository.fetchDailyCoachMarkDismissedStatusFlow.collect {
                _isCoachWeeklyDismissed.value = it
            }
        }
    }


    fun setCoachWeeklyAsDismissed(dismissWeeklyCoachMark: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateDailyCoachMarkDismissedStatusFlow(dismissWeeklyCoachMark)
        }
    }
}