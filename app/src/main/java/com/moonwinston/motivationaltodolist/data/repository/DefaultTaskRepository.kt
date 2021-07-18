package com.moonwinston.motivationaltodolist.data.repository

import com.moonwinston.motivationaltodolist.data.db.dao.TaskDao
import com.moonwinston.motivationaltodolist.data.entity.TaskEntity
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

    override suspend fun deleteTask(taskDate: Date, taskTime: Date) = withContext(ioDispatcher) {
        taskDao.deleteTask(taskDate, taskTime)
    }
}