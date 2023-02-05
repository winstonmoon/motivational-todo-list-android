package com.moonwinston.motivationaltodolist.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.OffsetDateTime

@Parcelize
@Entity(tableName = "task")
data class TaskEntity(
        @PrimaryKey(autoGenerate = true) val uid: Long = 0,
        @ColumnInfo(name = "taskDate") var taskDate: OffsetDateTime,
        @ColumnInfo(name = "task") var task: String,
        @ColumnInfo(name = "isCompleted") var isCompleted: Boolean
): Parcelable