package com.moonwinston.motivationaltodolist.utilities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.moonwinston.motivationaltodolist.data.CalendarDate
import java.text.SimpleDateFormat
import java.util.*

class RoomTypeConverters {
    var listType: TypeToken<MutableList<CalendarDate>> =
        object : TypeToken<MutableList<CalendarDate>>() {}

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time?.toLong()

    @TypeConverter
    fun listToJson(value: MutableList<CalendarDate>?): String?
    = Gson().toJson(value, listType.type)


    @TypeConverter
    fun jsonToList(value: String?): MutableList<CalendarDate>?
    = Gson().fromJson(value, listType.type)
}