package com.moonwinston.motivationaltodolist.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

fun Date.getYearMonthDateTriple(): Triple<Int, Int, Int> {
    val cal = Calendar.getInstance().apply {
        time = this@getYearMonthDateTriple
    }
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val date = cal.get(Calendar.DAY_OF_MONTH)
    return Triple(year, month, date)
}

fun Date.getHourMinutePair(): Pair<Int, Int> {
    val cal = Calendar.getInstance().apply {
        time = this@getHourMinutePair
    }
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)
    return Pair(hour, minute)
}


fun test() {
    val date: LocalDate
    val time: LocalTime
    val dateTime: LocalDateTime

}