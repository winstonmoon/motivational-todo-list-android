package com.moonwinston.motivationaltodolist.di

import com.moonwinston.motivationaltodolist.MainViewModel
import com.moonwinston.motivationaltodolist.di.provideDB
import com.moonwinston.motivationaltodolist.di.provideTaskDao
import com.moonwinston.motivationaltodolist.data.DefaultTaskRepository
import com.moonwinston.motivationaltodolist.data.SharedPref
import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.ui.daily.DailyViewModel
import com.moonwinston.motivationaltodolist.ui.monthly.MonthlyViewModel
import com.moonwinston.motivationaltodolist.ui.rewards.RewardsViewModel
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import com.moonwinston.motivationaltodolist.ui.weekly.WeeklyViewModel
import org.koin.dsl.module
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel

internal val appModule = module {
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    single<TaskRepository> { DefaultTaskRepository(get(), get()) }

    single { provideDB(androidApplication()) }
    single { provideTaskDao(get()) }
    single { SharedPref(androidApplication())}

    viewModel { MainViewModel() }
    viewModel { DailyViewModel() }
    viewModel { MonthlyViewModel() }
    viewModel { RewardsViewModel() }
    viewModel { WeeklyViewModel() }
    viewModel { SharedViewModel(get()) }
}


