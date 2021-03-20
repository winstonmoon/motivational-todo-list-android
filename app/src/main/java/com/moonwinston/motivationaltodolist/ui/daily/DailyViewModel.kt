package com.moonwinston.motivationaltodolist.ui.daily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DailyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is daily Fragment"
    }
    val text: LiveData<String> = _text
}