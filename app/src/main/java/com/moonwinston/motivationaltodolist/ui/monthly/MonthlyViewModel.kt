package com.moonwinston.motivationaltodolist.ui.monthly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonthlyViewModel @Inject constructor (
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

//    private val _isCoachMonthlyDismissed = MutableStateFlow(false)
//    val isCoachMonthlyDismissed = _isCoachMonthlyDismissed.asStateFlow()
//
//    fun getCoachMonthlyDismissed() {
//        viewModelScope.launch {
//            userPreferencesRepository.fetchMonthlyCoachMarkDismissedStatusFlow.collect {
//                _isCoachMonthlyDismissed.value = it
//            }
//        }
//    }

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
}