package com.moonwinston.motivationaltodolist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AchievementRateDao {

    @Query("SELECT * FROM achievementRate")
    fun getAll(): List<AchievementRateEntity>

    @Query("SELECT * FROM achievementRate WHERE rate = 1")
    fun getAllComplete(): List<AchievementRateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(achievementRateEntity: AchievementRateEntity)
}