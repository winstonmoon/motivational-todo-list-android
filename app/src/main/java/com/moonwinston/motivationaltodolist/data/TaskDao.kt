package com.moonwinston.motivationaltodolist.data

import androidx.room.*
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAll(): List<TaskEntity>

    //TODO temporary implement
    @Query("SELECT * FROM task WHERE taskDate < :currentTime AND taskTime < :currentTime")
    fun getAllFutureTasks(currentTime: Date): List<TaskEntity>

    @Query("SELECT * FROM task WHERE taskDate IN(:taskDatesList)")
    fun getAllByDates(taskDatesList: MutableList<Date>): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskEntity: TaskEntity)

    @Query("DELETE FROM task WHERE uid=:uid")
    fun delete(uid: Long)
}