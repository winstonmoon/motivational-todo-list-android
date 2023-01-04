package com.moonwinston.motivationaltodolist.data

import com.moonwinston.motivationaltodolist.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TaskRepository {

    override suspend fun getAllTasks(): List<TaskEntity> = withContext(ioDispatcher) {
        taskDao.getAll()
    }

    //TODO temporary implement
    override suspend fun getAllFutureTasks(currentTime: Date): List<TaskEntity> = withContext(ioDispatcher) {
        taskDao.getAllFutureTasks(currentTime)
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