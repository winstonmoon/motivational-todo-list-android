package com.moonwinston.motivationaltodolist.utilities

import androidx.room.TypeConverter
import java.util.*

class RoomTypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}