package com.moonwinston.motivationaltodolist.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
){
    suspend fun getAllFutureTasks(currentTime: OffsetDateTime) = withContext(Dispatchers.IO) { taskDao.getAllFutureTasks(currentTime = currentTime) }

    fun getAllTasksByDate(date: OffsetDateTime) = taskDao.getAllByDate(date)

    fun getAllTasksByStartEndDateFlow(startDate: OffsetDateTime, endDate: OffsetDateTime) = taskDao.getAllByStartEndDateFlow(startDate = startDate, endDate = endDate)

    suspend fun getAllTasksByStartEndDate(startDate: OffsetDateTime, endDate: OffsetDateTime) = withContext(Dispatchers.IO) {
        taskDao.getAllByStartEndDate(startDate = startDate, endDate = endDate)
    }

    suspend fun insertTask(taskEntity: TaskEntity) = withContext(Dispatchers.IO) { taskDao.insert(taskEntity) }

    suspend fun deleteTask(uid: Long) = withContext(Dispatchers.IO) { taskDao.delete(uid) }
}