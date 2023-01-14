package com.moonwinston.motivationaltodolist.ui.daily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
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

    val isCoachDailyDismissed = userPreferencesRepository.fetchDailyCoachMarkDismissedStatusFlow.stateIn(
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