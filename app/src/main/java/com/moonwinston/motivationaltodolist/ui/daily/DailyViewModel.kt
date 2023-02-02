package com.moonwinston.motivationaltodolist.ui.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.AchievementRateRepository
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import com.moonwinston.motivationaltodolist.utils.dateOfToday
import com.moonwinston.motivationaltodolist.utils.getDateExceptTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val achievementRateRepository: AchievementRateRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val todayTasks = taskRepository.getAllTasks().map { taskEntities ->
        taskEntities.filter { taskEntity ->
            taskEntity.taskDate.getDateExceptTime() == dateOfToday()
        }.sortedBy { taskEntity ->
            taskEntity.taskDate
        }
    }.stateIn(
        initialValue = emptyList(),
        started = SharingStarted.Eagerly,
        scope = viewModelScope
    )

    val todayAchievementRate =
        achievementRateRepository.getRateByDates(dateOfToday()).filterNotNull().stateIn(
            initialValue = 0.0F,
            started = SharingStarted.Eagerly,
            scope = viewModelScope
        )

    val isCoachDailyDismissed =
        userPreferencesRepository.fetchDailyCoachMarkDismissedStatusFlow.stateIn(
            initialValue = false,
            started = SharingStarted.Eagerly,
            scope = viewModelScope
        )

    fun setCoachDailyAsDismissed(dismissDailyCoachMark: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateDailyCoachMarkDismissedStatusFlow(dismissDailyCoachMark)
        }
    }

    fun calculateRate(tasksList: List<TaskEntity>): Float {
        var totalTasks = 0F
        var doneTasks = 0F
        for (task in tasksList) {
            totalTasks += 1F
            if (task.isCompleted) doneTasks += 1F
        }
        return if (doneTasks == 0F) 0F else doneTasks / totalTasks
    }
}