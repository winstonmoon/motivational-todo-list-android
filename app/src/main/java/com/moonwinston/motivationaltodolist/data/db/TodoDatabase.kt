package com.moonwinston.motivationaltodolist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moonwinston.motivationaltodolist.data.entity.TaskEntity
import com.moonwinston.motivationaltodolist.data.db.dao.TaskDao
import com.moonwinston.motivationaltodolist.util.Converters

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "ToDoDataBase.db"
    }
    abstract fun taskDao(): TaskDao
}