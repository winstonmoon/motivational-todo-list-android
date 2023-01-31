package com.moonwinston.motivationaltodolist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAll(): Flow<List<TaskEntity>>

    //TODO temporary implement
    @Query("SELECT * FROM task WHERE taskDate < :currentTime")
    fun getAllFutureTasks(currentTime: Date): List<TaskEntity>

    @Query("SELECT * FROM task WHERE date(taskDate) IN(:taskDatesList)")
    fun getAllByDates(taskDatesList: MutableList<Date>): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskEntity: TaskEntity)

    @Query("DELETE FROM task WHERE uid=:uid")
    fun delete(uid: Long)
}