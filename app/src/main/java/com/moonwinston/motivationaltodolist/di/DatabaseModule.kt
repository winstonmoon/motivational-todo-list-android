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

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Book ADD COLUMN pub_year INTEGER")
            database.execSQL("ALTER TABLE Book ADD COLUMN pub_year INTEGER")
            database.execSQL("ALTER TABLE Book ADD COLUMN pub_year INTEGER")
        }
    }

    @Singleton
    @Provides
    fun provideTodoDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase = Room.databaseBuilder(context, TodoDatabase::class.java, TodoDatabase.DB_NAME).build()
//    ): TodoDatabase = Room.databaseBuilder(context, TodoDatabase::class.java, TodoDatabase.DB_NAME).addMigrations(MIGRATION_2_3).build()

    @Singleton
    @Provides
    fun provideTaskDao(todoDatabase: TodoDatabase): TaskDao = todoDatabase.taskDao()

    @Singleton
    @Provides
    fun provideAchievementRateDao(todoDatabase: TodoDatabase): AchievementRateDao = todoDatabase.achievementRateDao()
}