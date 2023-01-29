package com.moonwinston.motivationaltodolist.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRateRepository @Inject constructor(
    private val achievementRateDao: AchievementRateDao
) {

    fun getRateByDates(date: Date) = achievementRateDao.getRateByDates(date)

    fun getAllCompleteRate() = achievementRateDao.getAllComplete()

    suspend fun insertRate(achievementRateEntity: AchievementRateEntity) = withContext(Dispatchers.IO) { achievementRateDao.insert(achievementRateEntity) }
}