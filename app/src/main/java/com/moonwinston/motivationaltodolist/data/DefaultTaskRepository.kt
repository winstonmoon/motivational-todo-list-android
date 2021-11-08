package com.moonwinston.motivationaltodolist.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class DefaultTaskRepository(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher
) : TaskRepository {

    override suspend fun getAllTasks(): List<TaskEntity> = withContext(ioDispatcher) {
        taskDao.getAll()
    }

    override suspend fun getAllTasksByDates(taskDatesList: MutableList<Date>): List<TaskEntity> =
        withContext(ioDispatcher) {
            taskDao.getAllByDates(taskDatesList)
        }

    override suspend fun insertTask(taskEntity: TaskEntity) = withContext(ioDispatcher) {
        taskDao.insert(taskEntity)
    }

    override suspend fun deleteTask(uid: Long) = withContext(ioDispatcher) {
        taskDao.delete(uid)
    }
}