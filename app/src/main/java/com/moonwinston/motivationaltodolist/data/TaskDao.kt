package com.moonwinston.motivationaltodolist.data

import androidx.room.*
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task WHERE taskDate=:taskDate")
    suspend fun loadAllTasksByDate(taskDate: Date): List<TaskEntity>

//    @Query("SELECT isGoalSet, isCompleted FROM task WHERE taskDate IN (:taskDate)")
//    suspend fun loadAllAchievementsByDate(taskDate: Date): List<TaskEntity>

    @Update()
    suspend fun updateTask(taskEntity: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(taskEntity: TaskEntity)

    @Query("DELETE FROM task WHERE uid=:uid")
    suspend fun deleteTask(uid: Long)
}