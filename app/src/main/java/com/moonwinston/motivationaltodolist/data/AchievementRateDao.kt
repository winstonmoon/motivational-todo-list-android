package com.moonwinston.motivationaltodolist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface AchievementRateDao {

    @Query("SELECT rate FROM achievementRate WHERE date = :date")
    fun getRateByDates(date: Date): Flow<Float>

    @Query("SELECT * FROM achievementRate WHERE rate = 1")
    fun getAllComplete(): Flow<List<AchievementRateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(achievementRateEntity: AchievementRateEntity)
}