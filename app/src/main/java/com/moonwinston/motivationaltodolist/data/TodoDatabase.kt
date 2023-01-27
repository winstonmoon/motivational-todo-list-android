package com.moonwinston.motivationaltodolist.data

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.moonwinston.motivationaltodolist.utils.RoomTypeConverters

@Database(
    version = 2,
    entities = [TaskEntity::class, AchievementRateEntity::class],
    autoMigrations = [
        AutoMigration (
            from = 1,
            to = 2,
            spec = TodoDatabase.TodoAutoMigration::class
        )
    ]
)
@TypeConverters(RoomTypeConverters::class)
abstract class TodoDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "ToDoDataBase.db"
    }

    abstract fun taskDao(): TaskDao

    abstract fun achievementRateDao(): AchievementRateDao

    @DeleteColumn(tableName = "task", columnName = "taskTime")
    class TodoAutoMigration : AutoMigrationSpec
}