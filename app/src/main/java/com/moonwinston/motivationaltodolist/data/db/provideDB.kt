package com.moonwinston.motivationaltodolist.data.db

import android.content.Context
import androidx.room.Room

internal fun provideDB(context: Context): TodoDatabase =
    Room.databaseBuilder(context, TodoDatabase::class.java, TodoDatabase.DB_NAME).build()

internal fun provideTaskDao(database: TodoDatabase) = database.taskDao()
