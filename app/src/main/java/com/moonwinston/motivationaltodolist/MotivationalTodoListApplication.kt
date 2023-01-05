package com.moonwinston.motivationaltodolist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MotivationalTodoListApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}