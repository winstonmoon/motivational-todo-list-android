package com.moonwinston.motivationaltodolist.ui.monthly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MonthlyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is monthly Fragment"
    }
    val text: LiveData<String> = _text
}