package com.moonwinston.motivationaltodolist.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

data class CalendarDate (
////    var month: Int,
//    var dayOfMonth: Int
////    var dayOfWeek: Int
//
//)
//{
////    constructor(): this(0, 0, 0)
//    constructor(): this(0)
//}

var calendarDate: Date
)
{
    constructor(): this(SimpleDateFormat("yyyy-MM-dd").parse("0000-00-00"))
}