package com.moonwinston.motivationaltodolist.data

import com.moonwinston.motivationaltodolist.data.TaskEntity
import java.util.*

interface TaskRepository {

    suspend fun getAllTasksByDate(taskDate: Date): List<TaskEntity>

//    suspend fun getAllTasksByDates(taskDatesList: MutableList<CalendarDate>): List<TaskEntity>
suspend fun getAllTasksByDates(taskDatesList: MutableList<Date>): List<TaskEntity>

    suspend fun insertTask(taskEntity: TaskEntity)

    suspend fun deleteTask(uid: Long)

}