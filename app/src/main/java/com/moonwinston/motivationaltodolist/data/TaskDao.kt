package com.moonwinston.motivationaltodolist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task ORDER BY taskDate")
    fun getAll(): Flow<List<TaskEntity>>
//    fun getAll(): List<TaskEntity>

    //TODO temporary implement
    @Query("SELECT * FROM task WHERE taskDate < :currentTime ORDER BY taskDate")
    fun getAllFutureTasks(currentTime: OffsetDateTime): List<TaskEntity>

    @Query("SELECT * FROM task WHERE date(taskDate) = date(:date) ORDER BY taskDate")
    fun getAllByDate(date: OffsetDateTime): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE date(taskDate) IN (date(:taskDatesList)) ORDER BY taskDate")
    fun getAllByDatesFlow(taskDatesList: List<OffsetDateTime>): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE date(taskDate) IN (date(:taskDatesList)) ORDER BY taskDate")
    fun getAllByDates(taskDatesList: List<OffsetDateTime>): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskEntity: TaskEntity)

    @Query("DELETE FROM task WHERE uid=:uid")
    fun delete(uid: Long)
}