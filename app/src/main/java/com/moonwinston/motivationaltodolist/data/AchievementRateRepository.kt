package com.moonwinston.motivationaltodolist.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRateRepository @Inject constructor(
    private val achievementRateDao: AchievementRateDao
) {

    fun getAllRate() = achievementRateDao.getAll()

    fun getAllCompleteRate() = achievementRateDao.getAllComplete()

    fun insertRate(achievementRateEntity: AchievementRateEntity) = achievementRateDao.insert(achievementRateEntity)
}