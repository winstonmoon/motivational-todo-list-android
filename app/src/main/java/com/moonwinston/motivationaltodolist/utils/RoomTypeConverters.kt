package com.moonwinston.motivationaltodolist.utils

import androidx.room.TypeConverter
import java.util.*

class RoomTypeConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time?.toLong()
}