package com.moonwinston.motivationaltodolist.ui.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeeklyViewModel @Inject constructor (
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _isCoachWeeklyDismissed = MutableStateFlow(false)
    val isCoachWeeklyDismissed = _isCoachWeeklyDismissed.asStateFlow()

    fun getCoachWeeklyDismissed() {
        viewModelScope.launch {
            userPreferencesRepository.fetchWeeklyCoachMarkDismissedStatusFlow.collect {
                _isCoachWeeklyDismissed.value = it
            }
        }
    }

    fun setCoachWeeklyAsDismissed(dismissWeeklyCoachMark: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateWeeklyCoachMarkDismissedStatusFlow(dismissWeeklyCoachMark)
        }
    }
}