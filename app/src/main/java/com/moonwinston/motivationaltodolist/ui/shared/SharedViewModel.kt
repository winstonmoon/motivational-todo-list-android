package com.moonwinston.motivationaltodolist.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val achievementRateRepository: AchievementRateRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private var _tasksListLiveData = MutableLiveData<List<TaskEntity>>()
    val tasksListLiveData: LiveData<List<TaskEntity>>
        get() = _tasksListLiveData

    fun getAllTasks() = viewModelScope.launch (Dispatchers.IO) {
        val list = taskRepository.getAllTasks()
        val sortedList = list.sortedBy { it.taskDate }
        withContext(Dispatchers.Main) {
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
    }

    private var _multipleDaysTasksList = MutableLiveData<List<TaskEntity>>()
    val multipleDaysTasksList: LiveData<List<TaskEntity>>
        get() = _multipleDaysTasksList

    fun getAllTasksByDates(taskDatesList: MutableList<Date>) = viewModelScope.launch (Dispatchers.IO) {
        val list = taskRepository.getAllTasksByDates(taskDatesList)
        val sortedList = list.sortedBy { it.taskDate }
        withContext(Dispatchers.Main) {
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
    }

    fun insertTask(taskEntity: TaskEntity) = viewModelScope.launch (Dispatchers.IO) {
        taskRepository.insertTask(taskEntity)
        //TODO
        getAllTasks()
    }

    fun deleteTask(uid: Long) = viewModelScope.launch (Dispatchers.IO) {
        taskRepository.deleteTask(uid)
        //TODO
        getAllTasks()
    }

    fun insertAchievementRate(achievementRateEntity: AchievementRateEntity) = viewModelScope.launch (Dispatchers.IO) {
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

    private val _languageIndex = MutableStateFlow(0)
    val languageIndex = _languageIndex.asStateFlow()

    fun getLanguage() {
        viewModelScope.launch {
            userPreferencesRepository.fetchLanguageSettingFlow.collect {
                _languageIndex.value = it
            }
        }
    }

    fun setLanguage(languageIndex: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateLanguageSettingFlow(languageIndex)
        }
    }

    private val _themeIndex = MutableStateFlow(0)
    val themeIndex = _themeIndex.asStateFlow()

    fun getTheme() {
        viewModelScope.launch {
            userPreferencesRepository.fetchThemeSettingFlow.collect {
                _themeIndex.value = it
            }
        }
    }

    fun setTheme(themeIndex: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateThemeSettingFlow(themeIndex)
        }
    }

    private val _notifyIndex = MutableStateFlow(0)
    val notifyIndex = _notifyIndex.asStateFlow()

    fun getNotify() {
        viewModelScope.launch {
            userPreferencesRepository.fetchNotifySettingFlow.collect {
                _notifyIndex.value = it
            }
        }
    }

    fun setNotify(notifyIndex: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateNotifySettingFlow(notifyIndex)
        }
    }
}