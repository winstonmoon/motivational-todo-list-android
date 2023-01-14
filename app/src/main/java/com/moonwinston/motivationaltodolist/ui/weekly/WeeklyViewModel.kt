package com.moonwinston.motivationaltodolist.ui.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklyViewModel @Inject constructor (
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

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
}