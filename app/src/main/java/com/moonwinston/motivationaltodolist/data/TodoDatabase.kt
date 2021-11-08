package com.moonwinston.motivationaltodolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moonwinston.motivationaltodolist.utils.RoomTypeConverters

@Database(
    entities = [TaskEntity::class, AchievementRateEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
abstract class TodoDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "ToDoDataBase.db"
    }

    abstract fun taskDao(): TaskDao

    abstract fun achievementRateDao(): AchievementRateDao
}