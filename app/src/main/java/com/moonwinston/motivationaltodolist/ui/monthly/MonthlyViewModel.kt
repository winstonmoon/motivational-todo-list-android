package com.moonwinston.motivationaltodolist.ui.monthly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import com.moonwinston.motivationaltodolist.utils.dateToLocalDate
import com.moonwinston.motivationaltodolist.utils.getDateExceptTime
import com.moonwinston.motivationaltodolist.utils.localDateToDate
import com.moonwinston.motivationaltodolist.utils.nonExistDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@HiltViewModel
class MonthlyViewModel @Inject constructor (
    private val taskRepository: TaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _yearAndMonth = MutableStateFlow(Pair(1970,1))
    val yearAndMonth: StateFlow<Pair<Int, Int>> = _yearAndMonth

    private fun setYearAndMonth(yearAndMonth: Pair<Int, Int>) = viewModelScope.launch {
        _yearAndMonth.emit(yearAndMonth)
    }

    private val _monthTasks = MutableStateFlow(listOf<TaskEntity>())
    val monthTasks: StateFlow<List<TaskEntity>> = _monthTasks

    fun getAllTasksByDates(daysOfMonth: List<OffsetDateTime>) = viewModelScope.launch {
        _monthTasks.emit(
            taskRepository.getAllTasksByDates(daysOfMonth).sortedBy { taskEntity ->
                taskEntity.taskDate
            }
        )
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

//    fun createDaysOfMonth(diffMonth: Int): List<Date> {
//        val calendar = Calendar.getInstance()
//        calendar.apply {
//            add(Calendar.MONTH, diffMonth)
//            set(Calendar.DAY_OF_MONTH, 1)
//            firstDayOfWeek = Calendar.MONDAY
//        }
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val dayOfWeek =
//            if (calendar.get(Calendar.DAY_OF_WEEK) == 1) 6
//            else calendar.get(Calendar.DAY_OF_WEEK) - 2
//        val daysOfMonth = MutableList(
//            dayOfWeek,
//            init = { nonExistDate() })
//        val endOfMonth = calendar.getActualMaximum(Calendar.DATE)
//        (1..endOfMonth).forEach { day ->
//            daysOfMonth.add(LocalDate.of(year, month + 1, day).localDateToDate())
//        }
//        return daysOfMonth
//    }

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
            daysOfMonth.add(OffsetDateTime.of(LocalDate.of(year, month + 1, day), LocalTime.of(0,0),
                ZoneOffset.UTC))
        }
        return daysOfMonth
    }

//    fun setYearAndMonthByLastDayOfMonth(daysOfMonth: List<Date>) {
//        val year = daysOfMonth.last().dateToLocalDate().year
//        val month = daysOfMonth.last().dateToLocalDate().monthValue
//        setYearAndMonth(Pair(year, month))
//    }

    fun setYearAndMonthByLastDayOfMonth(daysOfMonth: List<OffsetDateTime>) {
        val year = daysOfMonth.last().year
        val month = daysOfMonth.last().monthValue
        setYearAndMonth(Pair(year, month))
    }
}