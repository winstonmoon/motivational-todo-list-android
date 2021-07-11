package com.moonwinston.motivationaltodolist.data.repository

import com.moonwinston.motivationaltodolist.data.entity.TaskEntity
import java.util.*

interface TaskRepository {

    suspend fun loadAllTasksByDate(taskDate: Date): List<TaskEntity>

    suspend fun loadAllAchievementsByDate(taskDate: Date): List<TaskEntity>

    suspend fun updateTask(taskEntity: TaskEntity)

    suspend fun insertTask(taskEntity: TaskEntity)

    suspend fun deleteTask(taskDate: Date, taskTime: Date)

}