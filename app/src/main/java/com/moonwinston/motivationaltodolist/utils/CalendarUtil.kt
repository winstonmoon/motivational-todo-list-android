package com.moonwinston.motivationaltodolist.utils

import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {

    fun getTodayDate(): Date {
        //TODO
        val formatDate = SimpleDateFormat("yyyy-MM-dd")
        val todayString = formatDate.format(Calendar.getInstance().time)
        return formatDate.parse(todayString)
//        val cal  = Calendar.getInstance()
//        cal.set(Calendar.HOUR, 0)
//        cal.set(Calendar.MINUTE, 0)
//        cal.set(Calendar.SECOND, 0)
//        return cal.time
    }

    //TODO
    fun getNonExistDate(): Date = SimpleDateFormat("yyyy-MM-dd").parse("0000-00-00")
//    fun getNonExistDate(): Date {
//        val cal = Calendar.getInstance()
//        cal.set(Calendar.YEAR, 0)
//        cal.set(Calendar.MONTH, 0)
//        cal.set(Calendar.DAY_OF_MONTH, 0)
//        return cal.time
//    }
}