package com.moonwinston.motivationaltodolist.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRateRepository @Inject constructor(
    private val achievementRateDao: AchievementRateDao
) {

    suspend fun getAllRate() = withContext(Dispatchers.IO) { achievementRateDao.getAll() }

    suspend fun getAllCompleteRate() = withContext(Dispatchers.IO) { achievementRateDao.getAllComplete() }

    suspend fun insertRate(achievementRateEntity: AchievementRateEntity) = withContext(Dispatchers.IO) { achievementRateDao.insert(achievementRateEntity) }
}