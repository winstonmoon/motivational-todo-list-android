package com.moonwinston.motivationaltodolist.ui.settings

import android.app.PendingIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.utils.Notification
import com.moonwinston.motivationaltodolist.utils.nonExistDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val taskRepository: TaskRepository
    ) : ViewModel() {

    private val _notificationTime = MutableStateFlow(0L)
    val notificationTime: StateFlow<Long> = _notificationTime

    private val _futureTasks = MutableStateFlow(listOf<TaskEntity>())
    val futureTasks: StateFlow<List<TaskEntity>> = _futureTasks

    fun setNotification(index: Int) {
        val notificationTime =
            when (Notification.values()[index]) {
                Notification.OFF -> 0L
                Notification.FIVE_MIN -> 5L
                Notification.TEN_MIN -> 10L
                Notification.FIFTEEN_MIN -> 15L
                Notification.THIRTY_MIN -> 30L
                Notification.ONE_HOUR -> 60L
            }
        viewModelScope.launch {
            _notificationTime.emit(notificationTime)
            taskRepository.getAllFutureTasks(OffsetDateTime.now().plusMinutes(notificationTime)).collect { taskEntities ->
                _futureTasks.emit(taskEntities)
            }
        }
    }

    private val _alarmIntents = MutableStateFlow(listOf<PendingIntent>())
    val alarmIntents: StateFlow<List<PendingIntent>> = _alarmIntents

    fun setAlarmIntents(alarmIntents: List<PendingIntent>) {
        viewModelScope.launch {
            _alarmIntents.emit(alarmIntents)
        }
    }
}