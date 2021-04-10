package com.moonwinston.motivationaltodolist.ui.reward

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RewardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is reward Fragment"
    }
    val text: LiveData<String> = _text
}