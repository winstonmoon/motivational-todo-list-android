package com.moonwinston.motivationaltodolist.ui.monthly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MonthlyViewModel @Inject constructor (
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _isCoachMonthlyDismissed = MutableStateFlow(false)
    val isCoachMonthlyDismissed = _isCoachMonthlyDismissed.asStateFlow()

    fun getCoachMonthlyDismissed() {
        viewModelScope.launch {
            userPreferencesRepository.fetchMonthlyCoachMarkDismissedStatusFlow.collect {
                _isCoachMonthlyDismissed.value = it
            }
        }
    }

    fun setCoachMonthlyAsDismissed(dismissMonthlyCoachMark: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateMonthlyCoachMarkDismissedStatusFlow(dismissMonthlyCoachMark)
        }
    }
}