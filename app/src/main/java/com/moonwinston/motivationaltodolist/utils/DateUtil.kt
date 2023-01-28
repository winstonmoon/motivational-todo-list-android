package com.moonwinston.motivationaltodolist.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

fun dateOfToday(): Date {
    val today = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0))
    val instant = today.atZone(ZoneId.systemDefault()).toInstant()
    return Date.from(instant)
}

fun nonExistDate(): Date {
    val nonExistDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0)
    val instant = nonExistDate.atZone(ZoneId.systemDefault()).toInstant()
    return Date.from(instant)
}

fun Date.getDateExceptTime(): Date {
    val year = this@getDateExceptTime.dateToLocalDate().year
    val month = this@getDateExceptTime.dateToLocalDate().monthValue
    val day = this@getDateExceptTime.dateToLocalDate().dayOfMonth
    return LocalDateTime.of(year, month, day, 0, 0).localDateTimeToDate()
}

fun Date.dateToLocalDateTime(): LocalDateTime = this@dateToLocalDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

fun Date.dateToLocalDate(): LocalDate = this@dateToLocalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDateTime.localDateTimeToDate(): Date = Date.from(this@localDateTimeToDate.atZone(ZoneId.systemDefault()).toInstant())

fun LocalDate.localDateToDate(): Date = Date.from(this@localDateToDate.atStartOfDay(ZoneId.systemDefault()).toInstant())