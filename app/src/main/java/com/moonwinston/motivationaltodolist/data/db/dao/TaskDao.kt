package com.moonwinston.motivationaltodolist.data.db.dao

import androidx.room.*
import com.moonwinston.motivationaltodolist.data.entity.TaskEntity
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task WHERE taskDate IN (:taskDate)")
    fun loadAllTasksByDate(taskDate: Date): List<TaskEntity>

//    @Query("SELECT isGoalSet, isCompleted FROM task WHERE taskDate IN (:taskDate)")
//    fun loadAllAchievementsByDate(taskDate: Date): List<TaskEntity>

    @Update()
    fun updateTask(taskEntity: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(taskEntity: TaskEntity)

    @Query("DELETE FROM task WHERE taskDate=:taskDate AND taskTime=:taskTime")
    fun deleteTask(taskDate: Date, taskTime: Date)
}