package com.moonwinston.motivationaltodolist.di

import com.moonwinston.motivationaltodolist.data.*
import com.moonwinston.motivationaltodolist.ui.daily.DailyViewModel
import com.moonwinston.motivationaltodolist.ui.rewards.RewardsViewModel
import com.moonwinston.motivationaltodolist.ui.settings.SettingsViewModel
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import org.koin.dsl.module
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel

internal val appModule = module {
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    single<TaskRepository> { DefaultTaskRepository(get(), get()) }
    single<AchievementRateRepository> { DefaultAchievementRateRepository(get(), get()) }

    single { provideDB(androidApplication()) }
    single { provideTaskDao(get()) }
    single { provideAchievementRateDao(get())}

    single { SharedPref(androidApplication())}

    viewModel { DailyViewModel() }
    viewModel { RewardsViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { SharedViewModel(get(), get()) }
}


