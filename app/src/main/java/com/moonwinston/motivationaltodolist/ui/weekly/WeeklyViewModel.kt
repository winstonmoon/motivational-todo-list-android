package com.moonwinston.motivationaltodolist.ui.weekly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeeklyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is weekly Fragment"
    }
    val text: LiveData<String> = _text
}