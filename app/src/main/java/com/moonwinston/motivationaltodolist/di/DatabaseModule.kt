package com.moonwinston.motivationaltodolist.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moonwinston.motivationaltodolist.data.AchievementRateDao
import com.moonwinston.motivationaltodolist.data.TaskDao
import com.moonwinston.motivationaltodolist.data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    //TODO
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, `name` TEXT, " +
                    "PRIMARY KEY(`id`))")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Book ADD COLUMN pub_year INTEGER")
        }
    }

    @Singleton
    @Provides
    fun provideTodoDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase = Room
        .databaseBuilder(context, TodoDatabase::class.java, TodoDatabase.DB_NAME)
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
        .build()

    @Singleton
    @Provides
    fun provideTaskDao(todoDatabase: TodoDatabase): TaskDao = todoDatabase.taskDao()

    @Singleton
    @Provides
    fun provideAchievementRateDao(todoDatabase: TodoDatabase): AchievementRateDao = todoDatabase.achievementRateDao()
}