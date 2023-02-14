package com.moonwinston.motivationaltodolist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task WHERE datetime(:currentTime) < datetime(taskDate) ORDER BY taskDate")
    fun getAllFutureTasks(currentTime: OffsetDateTime): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE date(taskDate) = date(:date) ORDER BY taskDate")
    fun getAllByDate(date: OffsetDateTime): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE date(:startDate) <= date(taskDate) <= date(:endDate) ORDER BY taskDate")
    fun getAllByStartEndDateFlow(startDate: OffsetDateTime, endDate: OffsetDateTime): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE date(:startDate) <= date(taskDate) <= date(:endDate) ORDER BY taskDate")
    fun getAllByStartEndDate(startDate: OffsetDateTime, endDate: OffsetDateTime): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskEntity: TaskEntity)

    @Query("DELETE FROM task WHERE uid=:uid")
    fun delete(uid: Long)
}