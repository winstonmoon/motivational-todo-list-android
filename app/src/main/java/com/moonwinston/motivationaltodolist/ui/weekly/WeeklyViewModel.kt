package com.moonwinston.motivationaltodolist.ui.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
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
        _selectedDate.emit(date)
//        _selectedDayTasks.value = taskRepository.getAllTasksByDate(date)
        _selectedDayTasks.value = taskRepository.getAllTasksByDate(date).stateIn(
            initialValue = emptyList(),
            started = SharingStarted.Eagerly,
            scope = viewModelScope
        )
    }

//    val selectedDayTasks = taskRepository.getAllTasksByDate(selectedDate.value).stateIn(
//        initialValue = emptyList(),
//        started = SharingStarted.Eagerly,
//        scope = viewModelScope
//    )

    val allTasks = taskRepository.getAllTasks().stateIn(
        initialValue = emptyList(),
        started = SharingStarted.Eagerly,
        scope = viewModelScope
    )

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

    fun getWeeklyRateListsFromAllTasks(allTasks: List<TaskEntity>, daysOfWeek: List<OffsetDateTime>): List<Float> {
        val monList = mutableListOf<TaskEntity>()
        val tueList = mutableListOf<TaskEntity>()
        val wedList = mutableListOf<TaskEntity>()
        val thuList = mutableListOf<TaskEntity>()
        val friList = mutableListOf<TaskEntity>()
        val satList = mutableListOf<TaskEntity>()
        val sunList = mutableListOf<TaskEntity>()
        allTasks.forEach { taskEntity ->
            when (taskEntity.taskDate.getDateExceptTime()) {
                daysOfWeek[0] -> monList.add(taskEntity)
                daysOfWeek[1] -> tueList.add(taskEntity)
                daysOfWeek[2] -> wedList.add(taskEntity)
                daysOfWeek[3] -> thuList.add(taskEntity)
                daysOfWeek[4] -> friList.add(taskEntity)
                daysOfWeek[5] -> satList.add(taskEntity)
                daysOfWeek[6] -> sunList.add(taskEntity)
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