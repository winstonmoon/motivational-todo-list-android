package com.moonwinston.motivationaltodolist.ui.monthly

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import com.moonwinston.motivationaltodolist.utils.Language
import com.moonwinston.motivationaltodolist.utils.nonExistDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MonthlyViewModel @Inject constructor (
    application: Application,
    private val taskRepository: TaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : AndroidViewModel(application) {
    private val _yearAndMonth = MutableStateFlow(Pair(1970,1))
    val yearAndMonth: StateFlow<Pair<Int, Int>> = _yearAndMonth

    private fun setYearAndMonth(yearAndMonth: Pair<Int, Int>) = viewModelScope.launch {
        _yearAndMonth.value = yearAndMonth
    }

    private val _monthTasks = MutableStateFlow(listOf<TaskEntity>())
    val monthTasks: StateFlow<List<TaskEntity>> = _monthTasks

    fun getAllTasksByDates(daysOfMonth: List<OffsetDateTime>) = viewModelScope.launch {
        _monthTasks.value = taskRepository.getAllTasksByStartEndDate(daysOfMonth.first(), daysOfMonth.last())
    }

    val isCoachMonthlyDismissed = userPreferencesRepository.fetchMonthlyCoachMarkDismissedStatusFlow.stateIn(
        initialValue = false,
        started = SharingStarted.Eagerly,
        scope = viewModelScope
    )

    fun setCoachMonthlyAsDismissed(dismissMonthlyCoachMark: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateMonthlyCoachMarkDismissedStatusFlow(dismissMonthlyCoachMark)
        }
    }

    fun createDaysOfMonth(diffMonth: Int): List<OffsetDateTime> {
        val calendar = Calendar.getInstance()
        calendar.apply {
            add(Calendar.MONTH, diffMonth)
            set(Calendar.DAY_OF_MONTH, 1)
            firstDayOfWeek = Calendar.MONDAY
        }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfWeek =
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1) 6
            else calendar.get(Calendar.DAY_OF_WEEK) - 2
        val daysOfMonth = MutableList(
            dayOfWeek,
            init = { nonExistDate() })
        val endOfMonth = calendar.getActualMaximum(Calendar.DATE)
        (1..endOfMonth).forEach { day ->
            daysOfMonth.add(OffsetDateTime.of(LocalDate.of(year, month + 1, day), LocalTime.of(0,0), ZoneOffset.UTC))
        }
        return daysOfMonth
    }

    fun setYearAndMonthByLastDayOfMonth(daysOfMonth: List<OffsetDateTime>) {
        val year = daysOfMonth.last().year
        val month = daysOfMonth.last().monthValue
        setYearAndMonth(Pair(year, month))
    }

    fun createMonthlyTitle(year: Int, month: Int, selectedLanguage: Int): String {
        val wordYear = getApplication<Application>().resources.getString(R.string.label_year)
        val month = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.getDefault())
        return when (Language.values()[selectedLanguage]) {
            Language.ENGLISH -> "$month, $year"
            else -> "$year$wordYear $month"
        }
    }
}