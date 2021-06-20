package com.moonwinston.motivationaltodolist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "task")
data class Task(
//        val time: String,
//        var task: String,
//        var isGoalSet: Boolean
//){constructor():this("12:00","test",true)}
        @PrimaryKey(autoGenerate = true) val uid: Int,
        @ColumnInfo(name = "taskDate")var taskDate: Date,
        @ColumnInfo(name = "taskTime")var taskTime: Date,
        @ColumnInfo(name = "task")var task: String,
        @ColumnInfo(name = "isGoalSet")var isGoalSet: Boolean,
        @ColumnInfo(name = "isCompleted") var isCompleted: Boolean
)