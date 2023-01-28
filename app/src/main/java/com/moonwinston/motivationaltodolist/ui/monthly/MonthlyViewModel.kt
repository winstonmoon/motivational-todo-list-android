package com.moonwinston.motivationaltodolist.ui.monthly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private var _monthlyTitleLiveData = MutableLiveData<Pair<Int, Int>>()
    val monthlyTitleLiveData: LiveData<Pair<Int, Int>>
        get() = _monthlyTitleLiveData

    fun setMonthlyTitle(year: Int, month: Int) {
        _monthlyTitleLiveData.value = Pair(year, month)
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
}