package com.moonwinston.motivationaltodolist.data

interface AchievementRateRepository {

    suspend fun getAllRate(): List<AchievementRateEntity>

    suspend fun getAllCompleteRate(): List<AchievementRateEntity>

    suspend fun insertRate(achievementRateEntity: AchievementRateEntity)
}