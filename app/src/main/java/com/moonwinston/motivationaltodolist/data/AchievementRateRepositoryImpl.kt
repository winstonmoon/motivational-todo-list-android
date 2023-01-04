package com.moonwinston.motivationaltodolist.data

import com.moonwinston.motivationaltodolist.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AchievementRateRepositoryImpl @Inject constructor(
    private val achievementRateDao: AchievementRateDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
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