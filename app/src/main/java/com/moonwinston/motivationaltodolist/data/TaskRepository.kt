package com.moonwinston.motivationaltodolist.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
){
//    suspend fun getAllTasks() = withContext(Dispatchers.IO) { taskDao.getAll() }
    fun getAllTasks() = taskDao.getAll()

    //TODO temporary implement
    suspend fun getAllFutureTasks(currentTime: OffsetDateTime) = withContext(Dispatchers.IO) { taskDao.getAllFutureTasks(currentTime) }

    fun getAllTasksByDate(date: OffsetDateTime) = taskDao.getAllByDate(date)

    fun getAllTasksByDatesFlow(taskDatesList: List<OffsetDateTime>) = taskDao.getAllByDates(taskDatesList)

    suspend fun getAllTasksByDates(taskDatesList: List<OffsetDateTime>) = withContext(Dispatchers.IO) { taskDao.getAllByDates(taskDatesList) }

    suspend fun insertTask(taskEntity: TaskEntity) = withContext(Dispatchers.IO) { taskDao.insert(taskEntity) }

    suspend fun deleteTask(uid: Long) = withContext(Dispatchers.IO) { taskDao.delete(uid) }
}