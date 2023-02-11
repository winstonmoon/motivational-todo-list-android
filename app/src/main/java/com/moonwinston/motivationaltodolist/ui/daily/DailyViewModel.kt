package com.moonwinston.motivationaltodolist.ui.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.AchievementRateRepository
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import com.moonwinston.motivationaltodolist.utils.dateOfToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val achievementRateRepository: AchievementRateRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val todayTasks = taskRepository.getAllTasksByDate(OffsetDateTime.now()).filterNotNull().stateIn(
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
}