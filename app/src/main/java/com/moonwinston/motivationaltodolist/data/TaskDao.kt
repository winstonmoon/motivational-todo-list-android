package com.moonwinston.motivationaltodolist.data

import androidx.room.*
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    suspend fun getAll(): List<TaskEntity>

    @Query("SELECT * FROM task WHERE taskDate IN(:taskDatesList)")
    suspend fun getAllByDates(taskDatesList: MutableList<Date>): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskEntity: TaskEntity)

    @Query("DELETE FROM task WHERE uid=:uid")
    suspend fun delete(uid: Long)
}