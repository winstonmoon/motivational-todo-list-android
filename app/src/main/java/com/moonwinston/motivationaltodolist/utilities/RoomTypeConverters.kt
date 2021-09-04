package com.moonwinston.motivationaltodolist.utilities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.moonwinston.motivationaltodolist.data.CalendarDate
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

    @TypeConverter
    fun listToJson(value: List<CalendarDate>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<CalendarDate>::class.java).toList()
}