package com.moonwinston.motivationaltodolist.ui.daily

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.MotivationalTodoListApplication
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.AchievementRateRepository
import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import com.moonwinston.motivationaltodolist.utils.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(
    application: Application,
    private val taskRepository: TaskRepository,
    private val achievementRateRepository: AchievementRateRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : AndroidViewModel(application) {
    val todayTasks = taskRepository.getAllTasksByDate(OffsetDateTime.now()).filterNotNull().stateIn(
        initialValue = emptyList(),
        started = SharingStarted.Eagerly,
        scope = viewModelScope
    )

    val todayAchievementRate =
        achievementRateRepository.getRateByDates(OffsetDateTime.now()).filterNotNull().stateIn(
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

    fun createDailyTitle(language: Int): String {
        val today = getApplication<Application>().resources.getString(R.string.text_today)
        val wordYear = getApplication<Application>().resources.getString(R.string.label_year)
        val wordDay = getApplication<Application>().resources.getString(R.string.label_day)
        val year = LocalDate.now().year
        val month = LocalDate.now().month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val day = LocalDate.now().dayOfMonth
        return when (Language.values()[language]) {
            Language.ENGLISH -> "$today, $month $day, $year"
            else -> "$year$wordYear $month $day$wordDay $today"
        }
    }
}