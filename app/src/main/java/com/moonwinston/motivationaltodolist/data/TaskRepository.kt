package com.moonwinston.motivationaltodolist.data

import java.util.*

interface TaskRepository {

    suspend fun getAllTasks(): List<TaskEntity>

    suspend fun getAllTasksByDates(taskDatesList: MutableList<Date>): List<TaskEntity>

    suspend fun insertTask(taskEntity: TaskEntity)

    suspend fun deleteTask(uid: Long)

}