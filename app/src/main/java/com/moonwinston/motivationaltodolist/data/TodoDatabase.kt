package com.moonwinston.motivationaltodolist.data

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec

@Database(
    version = 3,
    entities = [TaskEntity::class, AchievementRateEntity::class],
    autoMigrations = [
        AutoMigration (
//            from = 1,
//            to = 2,
            from = 2,
            to = 3,
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