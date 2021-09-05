package com.moonwinston.motivationaltodolist.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class DefaultTaskRepository(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher
) : TaskRepository {

    override suspend fun getAllTasksByDate(taskDate: Date): List<TaskEntity> = withContext(ioDispatcher) {
        taskDao.getAllByDate(taskDate)
    }

//    override suspend fun getAllTasksByDates(taskDatesList: MutableList<CalendarDate>): List<TaskEntity> = withContext(ioDispatcher) {
        override suspend fun getAllTasksByDates(taskDatesList: MutableList<Date>): List<TaskEntity> = withContext(ioDispatcher) {
        taskDao.getAllByDates(taskDatesList)
    }

    override suspend fun insertTask(taskEntity: TaskEntity) = withContext(ioDispatcher) {
        taskDao.insert(taskEntity)
    }

    override suspend fun deleteTask(uid: Long) = withContext(ioDispatcher) {
        taskDao.delete(uid)
    }
}