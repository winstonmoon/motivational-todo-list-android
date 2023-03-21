package com.moonwinston.motivationaltodolist.data

import androidx.room.TypeConverter
import com.moonwinston.motivationaltodolist.utils.isoOffsetDateTimeFormatter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object RoomTypeConverters {
    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return isoOffsetDateTimeFormatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(isoOffsetDateTimeFormatter)
    }
}