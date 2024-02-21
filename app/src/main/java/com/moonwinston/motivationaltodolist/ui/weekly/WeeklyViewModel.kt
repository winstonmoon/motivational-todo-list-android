package com.moonwinston.motivationaltodolist.ui.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import com.moonwinston.motivationaltodolist.utils.calculateRate
import com.moonwinston.motivationaltodolist.utils.getDateExceptTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeeklyViewModel @Inject constructor (
    private val taskRepository: TaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _selectedDate = MutableStateFlow(OffsetDateTime.now())
    val selectedDate: StateFlow<OffsetDateTime> = _selectedDate

    private val _selectedDayTasks = MutableStateFlow(listOf<TaskEntity>())
    val selectedDayTasks: StateFlow<List<TaskEntity>> = _selectedDayTasks

    fun setSelectedDate(date: OffsetDateTime) = viewModelScope.launch {
        _selectedDate.value = date
        taskRepository.getAllTasksByDate(date).collect { taskEntities ->
            _selectedDayTasks.value = taskEntities
        }
    }

    private val _daysOfWeek = MutableStateFlow(listOf<OffsetDateTime>())
    val daysOfWeek: StateFlow<List<OffsetDateTime>> = _daysOfWeek

    private val _weeklyTasks = MutableStateFlow(listOf<TaskEntity>())
    val weeklyTasks: StateFlow<List<TaskEntity>> = _weeklyTasks

    fun setDaysOfWeek(daysOfWeek: List<OffsetDateTime>) = viewModelScope.launch {
        _daysOfWeek.value = daysOfWeek
        taskRepository.getAllTasksByStartEndDateFlow(daysOfWeek.first(), daysOfWeek.last()).collect { taskEntities ->
            _weeklyTasks.value = taskEntities
        }
    }

    val isCoachWeeklyDismissed = userPreferencesRepository.fetchWeeklyCoachMarkDismissedStatusFlow.stateIn(
        initialValue = false,
        started = SharingStarted.Eagerly,
        scope = viewModelScope
    )

    fun setCoachWeeklyAsDismissed(dismissWeeklyCoachMark: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateWeeklyCoachMarkDismissedStatusFlow(dismissWeeklyCoachMark)
        }
    }

    fun createDaysOfWeek(diffDays: Int): List<OffsetDateTime> {
        val daysOfWeek = mutableListOf<OffsetDateTime>()
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DATE, diffDays)
            firstDayOfWeek = Calendar.MONDAY
            val diffDateFromMonday =
                if (this@apply.get(Calendar.DAY_OF_WEEK) == 1) -6
                else 2 - this@apply.get(Calendar.DAY_OF_WEEK)
            add(Calendar.DATE, diffDateFromMonday)
        }

        (1..7).forEach { _ ->
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val date = calendar.get(Calendar.DATE)
            daysOfWeek.add(OffsetDateTime.of(LocalDate.of(year, month + 1, date), LocalTime.of(0,0), ZoneOffset.UTC))
            calendar.add(Calendar.DATE, 1)
        }
        return daysOfWeek
    }

    fun getWeeklyRateListsFromAllTasks(tasks: List<TaskEntity>): List<Float> {
        val monList = mutableListOf<TaskEntity>()
        val tueList = mutableListOf<TaskEntity>()
        val wedList = mutableListOf<TaskEntity>()
        val thuList = mutableListOf<TaskEntity>()
        val friList = mutableListOf<TaskEntity>()
        val satList = mutableListOf<TaskEntity>()
        val sunList = mutableListOf<TaskEntity>()
        tasks.forEach { taskEntity ->
            when (taskEntity.taskDate.getDateExceptTime()) {
                daysOfWeek.value[0] -> monList.add(taskEntity)
                daysOfWeek.value[1] -> tueList.add(taskEntity)
                daysOfWeek.value[2] -> wedList.add(taskEntity)
                daysOfWeek.value[3] -> thuList.add(taskEntity)
                daysOfWeek.value[4] -> friList.add(taskEntity)
                daysOfWeek.value[5] -> satList.add(taskEntity)
                daysOfWeek.value[6] -> sunList.add(taskEntity)
            }
        }
        val weeklyRates = mutableListOf<Float>()
        weeklyRates.add(calculateRate(monList))
        weeklyRates.add(calculateRate(tueList))
        weeklyRates.add(calculateRate(wedList))
        weeklyRates.add(calculateRate(thuList))
        weeklyRates.add(calculateRate(friList))
        weeklyRates.add(calculateRate(satList))
        weeklyRates.add(calculateRate(sunList))
        return weeklyRates
    }
}