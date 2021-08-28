package com.moonwinston.motivationaltodolist.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "task")
data class TaskEntity(
        @PrimaryKey(autoGenerate = true) val uid: Long = 0,
        @ColumnInfo(name = "taskDate") var taskDate: Date,
        @ColumnInfo(name = "taskTime") var taskTime: Date,
        @ColumnInfo(name = "task") var task: String,
        @ColumnInfo(name = "isCompleted") var isCompleted: Boolean
): Parcelable