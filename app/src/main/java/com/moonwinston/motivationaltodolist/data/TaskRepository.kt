package com.moonwinston.motivationaltodolist.data

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
){
    fun getAllTasks() = taskDao.getAll()

    //TODO temporary implement
    fun getAllFutureTasks(currentTime: Date) = taskDao.getAllFutureTasks(currentTime)

    fun getAllTasksByDates(taskDatesList: MutableList<Date>) = taskDao.getAllByDates(taskDatesList)

    fun insertTask(taskEntity: TaskEntity) = taskDao.insert(taskEntity)

    fun deleteTask(uid: Long) = taskDao.delete(uid)
}