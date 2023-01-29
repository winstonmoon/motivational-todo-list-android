package com.moonwinston.motivationaltodolist.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class AddDialogViewModel : ViewModel() {

    private val _date = MutableStateFlow(Date())
    val date: StateFlow<Date> = _date

    fun setDate(date: Date) = viewModelScope.launch {
        _date.emit(date)
    }

    private val _positiveButtonStringResource = MutableStateFlow(0)
    val positiveButtonStringResource: StateFlow<Int> = _positiveButtonStringResource

    fun setPositiveButtonStringResource(buttonStringResource: Int) = viewModelScope.launch {
        _positiveButtonStringResource.emit(buttonStringResource)
    }
}