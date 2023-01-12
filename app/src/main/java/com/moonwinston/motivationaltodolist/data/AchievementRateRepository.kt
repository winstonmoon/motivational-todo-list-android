package com.moonwinston.motivationaltodolist.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRateRepository @Inject constructor(
    private val achievementRateDao: AchievementRateDao
) {

    suspend fun getAllRate() = achievementRateDao.getAll()

    suspend fun getAllCompleteRate() = achievementRateDao.getAllComplete()

    suspend fun insertRate(achievementRateEntity: AchievementRateEntity) = achievementRateDao.insert(achievementRateEntity)
}