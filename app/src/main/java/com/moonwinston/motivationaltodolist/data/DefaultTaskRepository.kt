package com.moonwinston.motivationaltodolist.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class DefaultTaskRepository(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher
) : TaskRepository {

    override suspend fun getTasks(taskDate: Date): List<TaskEntity> = withContext(ioDispatcher) {
        taskDao.loadAllTasksByDate(taskDate)
    }

//    override suspend fun getAchievementStatus(taskDate: Date): List<TaskEntity> = withContext(ioDispatcher) {
//        taskDao.loadAllAchievementsByDate(taskDate)
//    }

    override suspend fun updateTask(taskEntity: TaskEntity) = withContext(ioDispatcher) {
        taskDao.updateTask(taskEntity)
    }

    override suspend fun insertTask(taskEntity: TaskEntity) = withContext(ioDispatcher) {
        taskDao.insertTask(taskEntity)
    }

    override suspend fun deleteTask(uid: Long) = withContext(ioDispatcher) {
        taskDao.deleteTask(uid)
    }
}