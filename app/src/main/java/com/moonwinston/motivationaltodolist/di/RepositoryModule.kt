package com.moonwinston.motivationaltodolist.di

import com.moonwinston.motivationaltodolist.data.AchievementRateRepository
import com.moonwinston.motivationaltodolist.data.AchievementRateRepositoryImpl
import com.moonwinston.motivationaltodolist.data.TaskRepositoryImpl
import com.moonwinston.motivationaltodolist.data.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAchievementRateRepository(): AchievementRateRepository {
        return AchievementRateRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideTaskRepository(): TaskRepository {
        return TaskRepositoryImpl()
    }
}