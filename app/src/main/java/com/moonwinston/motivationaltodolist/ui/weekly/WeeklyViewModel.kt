package com.moonwinston.motivationaltodolist.ui.weekly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import com.moonwinston.motivationaltodolist.utils.getDateExceptTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeeklyViewModel @Inject constructor (
    private val taskRepository: TaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private var _mondayRateLiveData = MutableLiveData<Float>()
    val mondayRateLiveData: LiveData<Float>
        get() = _mondayRateLiveData

    private var _tuesdayRateLiveData = MutableLiveData<Float>()
    val tuesdayRateLiveData: LiveData<Float>
        get() = _tuesdayRateLiveData

    private var _wednesdayRateLiveData = MutableLiveData<Float>()
    val wednesdayRateLiveData: LiveData<Float>
        get() = _wednesdayRateLiveData

    private var _thursdayRateLiveData = MutableLiveData<Float>()
    val thursdayRateLiveData: LiveData<Float>
        get() = _thursdayRateLiveData

    private var _fridayRateLiveData = MutableLiveData<Float>()
    val fridayRateLiveData: LiveData<Float>
        get() = _fridayRateLiveData

    private var _saturdayRateLiveData = MutableLiveData<Float>()
    val saturdayRateLiveData: LiveData<Float>
        get() = _saturdayRateLiveData

    private var _sundayRateLiveData = MutableLiveData<Float>()
    val sundayRateLiveData: LiveData<Float>
        get() = _sundayRateLiveData

    fun setRate(dayOfWeek: DayOfWeek, tasksList: List<TaskEntity>) {
        var totalTasks = 0F
        var doneTasks = 0F
        tasksList.forEach { taskEntity ->
            totalTasks += 1F
            if (taskEntity.isCompleted) doneTasks += 1F
        }
        when (dayOfWeek) {
            DayOfWeek.MONDAY -> _mondayRateLiveData.value = if (doneTasks == 0F) 0F else doneTasks / totalTasks
            DayOfWeek.TUESDAY -> _tuesdayRateLiveData.value = if (doneTasks == 0F) 0F else doneTasks / totalTasks
            DayOfWeek.WEDNESDAY -> _wednesdayRateLiveData.value = if (doneTasks == 0F) 0F else doneTasks / totalTasks
            DayOfWeek.THURSDAY -> _thursdayRateLiveData.value = if (doneTasks == 0F) 0F else doneTasks / totalTasks
            DayOfWeek.FRIDAY -> _fridayRateLiveData.value = if (doneTasks == 0F) 0F else doneTasks / totalTasks
            DayOfWeek.SATURDAY -> _saturdayRateLiveData.value = if (doneTasks == 0F) 0F else doneTasks / totalTasks
            DayOfWeek.SUNDAY-> _sundayRateLiveData.value = if (doneTasks == 0F) 0F else doneTasks / totalTasks
        }
    }

    private val _selectedDate = MutableStateFlow(Date())
    val selectedDate: StateFlow<Date> = _selectedDate

    fun setSelectedDate(date: Date) = viewModelScope.launch {
        _selectedDate.emit(date)
    }

    val selectedDayTasks = taskRepository.getAllTasks().map { taskEntities ->
        taskEntities.filter { taskEntity ->
            taskEntity.taskDate.getDateExceptTime() == selectedDate
        }.sortedBy { taskEntity ->
            taskEntity.taskDate
        }
    }.stateIn(
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

    fun calculateRate(tasksList: List<TaskEntity>): Float {
        var totalTasks = 0F
        var doneTasks = 0F
        for (task in tasksList) {
            totalTasks += 1F
            if (task.isCompleted) doneTasks += 1F
        }
        return if (doneTasks == 0F) 0F else doneTasks / totalTasks
    }

    fun createDaysOfWeek() {

    }
}