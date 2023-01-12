package com.moonwinston.motivationaltodolist.data

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
){
    suspend fun getAllTasks() = taskDao.getAll()

    //TODO temporary implement
    suspend fun getAllFutureTasks(currentTime: Date) = taskDao.getAllFutureTasks(currentTime)

    suspend fun getAllTasksByDates(taskDatesList: MutableList<Date>) = taskDao.getAllByDates(taskDatesList)

    suspend fun insertTask(taskEntity: TaskEntity) = taskDao.insert(taskEntity)

    suspend fun deleteTask(uid: Long) = taskDao.delete(uid)
}