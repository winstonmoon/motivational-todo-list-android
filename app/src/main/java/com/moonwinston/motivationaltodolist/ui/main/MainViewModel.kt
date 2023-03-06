package com.moonwinston.motivationaltodolist.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.*
import com.moonwinston.motivationaltodolist.utils.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val achievementRateRepository: AchievementRateRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    fun insertTask(taskEntity: TaskEntity) = viewModelScope.launch {
        taskRepository.insertTask(taskEntity)
    }

    fun deleteTask(uid: Long) = viewModelScope.launch {
        taskRepository.deleteTask(uid)
    }

    fun insertAchievementRate(achievementRateEntity: AchievementRateEntity) = viewModelScope.launch {
        achievementRateRepository.insertRate(achievementRateEntity)
    }

    val languageIndex = userPreferencesRepository.fetchLanguageSettingFlow.stateIn(
        initialValue = 0,
        started = SharingStarted.Eagerly,
        scope = viewModelScope
    )

    fun setLanguage(languageIndex: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateLanguageSettingFlow(languageIndex)
        }
    }

    val themeIndex = userPreferencesRepository.fetchThemeSettingFlow.stateIn(
        initialValue = 0,
        started = SharingStarted.Eagerly,
        scope = viewModelScope
    )

    fun setTheme(themeIndex: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateThemeSettingFlow(themeIndex)
        }
    }

    val notifyIndex = userPreferencesRepository.fetchNotifySettingFlow.stateIn(
        initialValue = 0,
        started = SharingStarted.Eagerly,
        scope = viewModelScope
    )

    fun setNotify(notifyIndex: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateNotifySettingFlow(notifyIndex)
        }
    }

    private val _futureTasks = MutableStateFlow(listOf<TaskEntity>())
    val futureTasks: StateFlow<List<TaskEntity>> = _futureTasks

    fun getFutureTasks(index: Int) {
        val notificationTime = Notification.values()[index].time
        viewModelScope.launch {
            taskRepository.getAllFutureTasks(OffsetDateTime.now().plusMinutes(notificationTime)).collect { taskEntities ->
                _futureTasks.emit(taskEntities)
            }
        }
    }
}