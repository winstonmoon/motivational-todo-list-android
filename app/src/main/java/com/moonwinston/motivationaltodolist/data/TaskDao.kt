package com.moonwinston.motivationaltodolist.data

import androidx.room.*
import java.util.*

@Dao
interface TaskDao {
//    @Query("SELECT * FROM task WHERE taskDate IN (:taskDate)")
//    fun loadAllTasksByDate(taskDate: Date): List<Task>
//
//    @Query("SELECT isGoalSet, isCompleted FROM task WHERE taskDate IN (:taskDate)")
//    fun loadAllAchievementsByDate(taskDate: Date): List<Task>

    @Update()
    fun updateTask(task: Task)

    @Insert()
    fun insertTask(task: Task)

    @Delete()
    fun deleteTask(task: Task)
}