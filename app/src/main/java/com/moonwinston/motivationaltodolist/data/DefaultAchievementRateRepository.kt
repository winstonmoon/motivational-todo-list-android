package com.moonwinston.motivationaltodolist.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultAchievementRateRepository(
    private val achievementRateDao: AchievementRateDao,
    private val ioDispatcher: CoroutineDispatcher
) : AchievementRateRepository {

    override suspend fun getAllRate(): List<AchievementRateEntity> = withContext(ioDispatcher) {
        achievementRateDao.getAll()
    }

    override suspend fun getAllCompleteRate(): List<AchievementRateEntity> =
        withContext(ioDispatcher) {
            achievementRateDao.getAllComplete()
        }

    override suspend fun insertRate(achievementRateEntity: AchievementRateEntity) = withContext(ioDispatcher) {
        achievementRateDao.insert(achievementRateEntity)
    }
}