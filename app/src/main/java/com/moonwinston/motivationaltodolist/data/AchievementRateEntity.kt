package com.moonwinston.motivationaltodolist.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.OffsetDateTime

@Parcelize
@Entity(tableName = "achievementRate")
data class AchievementRateEntity(
    @PrimaryKey var date: OffsetDateTime,
    @ColumnInfo(name = "rate") var rate: Float,
): Parcelable
