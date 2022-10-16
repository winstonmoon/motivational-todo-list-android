package com.moonwinston.motivationaltodolist.ui.settings

import androidx.lifecycle.ViewModel
import com.moonwinston.motivationaltodolist.data.AchievementRateRepository
import com.moonwinston.motivationaltodolist.data.TaskRepository

class SettingsViewModel(
    private val taskRepository: TaskRepository,
    private val achievementRateRepository: AchievementRateRepository
) : ViewModel() {

}