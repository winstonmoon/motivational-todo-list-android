package com.moonwinston.motivationaltodolist.ui.daily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moonwinston.motivationaltodolist.data.TaskEntity

class DailyViewModel : ViewModel() {
    private var _rateLiveData = MutableLiveData<Float>()
    val rateLiveData: LiveData<Float>
        get() = _rateLiveData

    fun setRate(tasksList: List<TaskEntity>) {
        var totalTasks = 0F
        var doneTasks = 0F
        for (task in tasksList) {
            totalTasks += 1F
            if (task.isCompleted) doneTasks += 1F
        }
        _rateLiveData.value = if (doneTasks == 0F) 0F else doneTasks / totalTasks
    }
}