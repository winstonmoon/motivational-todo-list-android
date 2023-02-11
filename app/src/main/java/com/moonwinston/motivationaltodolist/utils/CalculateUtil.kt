package com.moonwinston.motivationaltodolist.utils

import com.moonwinston.motivationaltodolist.data.TaskEntity

fun calculateRate(tasksList: List<TaskEntity>): Float {
    var totalTasks = 0F
    var doneTasks = 0F
    tasksList.forEach {  taskEntity ->
        totalTasks += 1F
        if (taskEntity.isCompleted) doneTasks += 1F
    }
    return if (doneTasks == 0F) 0F else doneTasks / totalTasks
}