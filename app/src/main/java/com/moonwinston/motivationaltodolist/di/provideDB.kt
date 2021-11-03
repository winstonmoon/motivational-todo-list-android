package com.moonwinston.motivationaltodolist.di

import android.content.Context
import androidx.room.Room
import com.moonwinston.motivationaltodolist.data.TodoDatabase

internal fun provideDB(context: Context): TodoDatabase =
    Room.databaseBuilder(context, TodoDatabase::class.java, TodoDatabase.DB_NAME).build()

internal fun provideTaskDao(database: TodoDatabase) = database.taskDao()

internal fun provideAchievementRateDao(database: TodoDatabase) = database.achievementRateDao()
