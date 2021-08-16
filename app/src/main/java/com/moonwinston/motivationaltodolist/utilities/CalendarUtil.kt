package com.moonwinston.motivationaltodolist.utilities

import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {

    fun getToday(): Date {
        val formatDate = SimpleDateFormat("yyyy-MM-dd")
        val todayString = formatDate.format(Calendar.getInstance().time)
        return formatDate.parse(todayString)
    }

    fun getNonExistDate(): Date = SimpleDateFormat("yyyy-MM-dd").parse("0000-00-00")
}