package com.moonwinston.motivationaltodolist.di

import com.moonwinston.motivationaltodolist.data.db.provideDB
import com.moonwinston.motivationaltodolist.data.db.provideTaskDao
import com.moonwinston.motivationaltodolist.data.DefaultTaskRepository
import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import org.koin.dsl.module
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel

internal val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    single<TaskRepository> { DefaultTaskRepository(get(), get()) }

    single { provideDB(androidApplication()) }
    single { provideTaskDao(get()) }

    viewModel { SharedViewModel(get()) }
}


