package com.moonwinston.motivationaltodolist.ui.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.AchievementRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RewardsViewModel @Inject constructor(
    private val achievementRateRepository: AchievementRateRepository
) : ViewModel() {

    val completedTask = achievementRateRepository.getAllCompleteRate().stateIn(
        initialValue = emptyList(),
        started = SharingStarted.Eagerly,
        scope = viewModelScope
    )
}