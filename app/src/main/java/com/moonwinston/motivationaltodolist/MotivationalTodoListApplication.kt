package com.moonwinston.motivationaltodolist

import android.app.Application
import com.moonwinston.motivationaltodolist.di.appModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@HiltAndroidApp
class MotivationalTodoListApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        startKoin {
//            androidLogger(Level.ERROR)
//            androidContext(this@MotivationalTodoListApplication)
//            modules(appModule)
//        }
    }
}