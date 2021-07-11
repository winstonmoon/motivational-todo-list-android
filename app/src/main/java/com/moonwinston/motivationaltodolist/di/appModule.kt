package com.moonwinston.motivationaltodolist.di

import android.content.Context
import androidx.room.Room
import com.moonwinston.motivationaltodolist.data.db.provideDB
import com.moonwinston.motivationaltodolist.data.db.provideTaskDao
import org.koin.dsl.module
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel

internal val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // Database
    single { provideDB(androidApplication()) }
    single { provideTaskDao(get()) }
}


