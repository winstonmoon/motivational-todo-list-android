package com.moonwinston.motivationaltodolist.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
){
    suspend fun getAllTasks() = withContext(Dispatchers.IO) { taskDao.getAll() }
//    fun getAllTasks() = taskDao.getAll()

    //TODO temporary implement
    suspend fun getAllFutureTasks(currentTime: Date) = withContext(Dispatchers.IO) { taskDao.getAllFutureTasks(currentTime) }

    suspend fun getAllTasksByDates(taskDatesList: MutableList<Date>) = withContext(Dispatchers.IO) { taskDao.getAllByDates(taskDatesList) }

    suspend fun insertTask(taskEntity: TaskEntity) = withContext(Dispatchers.IO) { taskDao.insert(taskEntity) }

    suspend fun deleteTask(uid: Long) = withContext(Dispatchers.IO) { taskDao.delete(uid) }
}