package com.moonwinston.motivationaltodolist.ui.daily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DailyViewModel @Inject constructor (
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private var _rateLiveData = MutableLiveData<Float>()
    val rateLiveData: LiveData<Float>
        get() = _rateLiveData

    fun setRate(tasksList: List<TaskEntity>) {
        var totalTasks = 0F
        var doneTasks = 0F
        for (task in tasksList) {
            totalTasks += 1F
            if (task.isCompleted) doneTasks += 1F
        }
        _rateLiveData.value = if (doneTasks == 0F) 0F else doneTasks / totalTasks
    }

    private val _isCoachDailyDismissed = MutableStateFlow(false)
    val isCoachDailyDismissed = _isCoachDailyDismissed.asStateFlow()

    fun getCoachDailyDismissed() {
        viewModelScope.launch {
            userPreferencesRepository.fetchDailyCoachMarkDismissedStatusFlow.collect {
                _isCoachDailyDismissed.value = it
            }
        }
    }

    fun setCoachDailyAsDismissed(dismissDailyCoachMark: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateDailyCoachMarkDismissedStatusFlow(dismissDailyCoachMark)
        }
    }
}