package com.moonwinston.motivationaltodolist.data

import com.moonwinston.motivationaltodolist.data.TaskEntity
import java.util.*

interface TaskRepository {

    suspend fun getTasks(taskDate: Date): List<TaskEntity>

//    suspend fun getAchievementStatus(taskDate: Date): List<TaskEntity>

    suspend fun updateTask(taskEntity: TaskEntity)

    suspend fun insertTask(taskEntity: TaskEntity)

    suspend fun deleteTask(uid: Long)

}