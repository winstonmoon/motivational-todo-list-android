package com.moonwinston.motivationaltodolist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "task")
data class TaskEntity(
        @PrimaryKey(autoGenerate = true) val uid: Long = 0,
        @ColumnInfo(name = "taskDate")var taskDate: Date,
        @ColumnInfo(name = "taskTime")var taskTime: Date,
        @ColumnInfo(name = "task")var task: String,
        @ColumnInfo(name = "isGoalSet")var isGoalSet: Boolean,
        @ColumnInfo(name = "isCompleted") var isCompleted: Boolean
)