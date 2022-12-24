package com.moonwinston.motivationaltodolist.ui.rewards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.data.AchievementRateRepository
import kotlinx.coroutines.launch

class RewardsViewModel(private val achievementRateRepository: AchievementRateRepository) : ViewModel() {
    private var _rateListLiveData = MutableLiveData<List<AchievementRateEntity>>()
    val rateListLiveData: LiveData<List<AchievementRateEntity>>
        get() = _rateListLiveData

    fun getAllComplete() = viewModelScope.launch {
        val list = achievementRateRepository.getAllCompleteRate()
        val sortedList = list.sortedBy { it.date }
        _rateListLiveData.value = sortedList.map {
            AchievementRateEntity(
                date = it.date,
                rate = it.rate
            )
        }
    }
}