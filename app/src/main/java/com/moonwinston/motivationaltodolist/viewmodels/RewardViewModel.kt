package com.moonwinston.motivationaltodolist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moonwinston.motivationaltodolist.data.Task

class RewardViewModel : ViewModel() {

    val tasks: List<Task> = arrayListOf(
            Task(isGoalSet = false, task = "test1", time = "8:00"),
            Task(isGoalSet = true, task = "test2", time = "9:00"),
            Task(isGoalSet = false, task = "test3", time = "10:00"),
            Task(isGoalSet = true, task = "test3", time = "11:00"),
            Task(isGoalSet = false, task = "test3", time = "12:00"),
    )
}