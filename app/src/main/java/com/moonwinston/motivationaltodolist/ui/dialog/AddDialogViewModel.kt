package com.moonwinston.motivationaltodolist.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.util.*

class AddDialogViewModel : ViewModel() {

    private val _date = MutableStateFlow(OffsetDateTime.now())
    val date: StateFlow<OffsetDateTime> = _date

    fun setDate(date: OffsetDateTime) = viewModelScope.launch {
        _date.emit(date)
    }

    private val _positiveButtonStringResource = MutableStateFlow(0)
    val positiveButtonStringResource: StateFlow<Int> = _positiveButtonStringResource

    fun setPositiveButtonStringResource(buttonStringResource: Int) = viewModelScope.launch {
        _positiveButtonStringResource.emit(buttonStringResource)
    }
}