package com.moonwinston.motivationaltodolist.ui.rewards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.data.AchievementRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RewardsViewModel @Inject constructor(
    private val achievementRateRepository: AchievementRateRepository
) : ViewModel() {
    private var _rateListLiveData = MutableLiveData<List<AchievementRateEntity>>()
    val rateListLiveData: LiveData<List<AchievementRateEntity>>
        get() = _rateListLiveData

    fun getAllComplete() = viewModelScope.launch (Dispatchers.IO) {
        val list = achievementRateRepository.getAllCompleteRate()
        val sortedList = list.sortedBy { achievementRateEntity ->
            achievementRateEntity.date }
        withContext(Dispatchers.Main) {
            _rateListLiveData.value = sortedList.map { achievementRateEntity ->
                AchievementRateEntity(
                    date = achievementRateEntity.date,
                    rate = achievementRateEntity.rate
                )
            }
        }
    }
}