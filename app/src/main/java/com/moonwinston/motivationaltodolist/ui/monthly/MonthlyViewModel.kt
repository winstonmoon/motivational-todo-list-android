package com.moonwinston.motivationaltodolist.ui.monthly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MonthlyViewModel : ViewModel() {
    private var _monthlyTitleLiveData = MutableLiveData<Pair<Int, Int>>()
    val monthlyTitleLiveData: LiveData<Pair<Int, Int>>
        get() = _monthlyTitleLiveData

    fun setMonthlyTitle(month: Int, year: Int) {
        _monthlyTitleLiveData.value = Pair(month, year)
    }
}