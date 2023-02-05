package com.moonwinston.motivationaltodolist.data

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec

@Database(
    version = 3,
    entities = [TaskEntity::class, AchievementRateEntity::class],
    autoMigrations = [
        AutoMigration (
            from = 2,
            to = 3
        )
    ]
)
@TypeConverters(TiviTypeConverters::class)
abstract class TodoDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "ToDoDataBase.db"
    }

    abstract fun taskDao(): TaskDao

    abstract fun achievementRateDao(): AchievementRateDao

    @DeleteColumn(tableName = "task", columnName = "taskTime")
    class TodoAutoMigration : AutoMigrationSpec
}