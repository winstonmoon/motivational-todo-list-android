package com.moonwinston.motivationaltodolist.utils

import java.time.*
import java.util.*

//fun dateOfToday(): Date {
//    val today = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0))
//    return today.localDateTimeToDate()
//}

fun dateOfToday(): OffsetDateTime = OffsetDateTime.of(LocalDate.now(), LocalTime.of(0,0), ZoneOffset.UTC)


//fun nonExistDate(): Date {
//    val nonExistDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0)
//    return nonExistDate.localDateTimeToDate()
//}

fun nonExistDate(): OffsetDateTime = OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.of(0, 0), ZoneOffset.UTC )

//fun Date.getDateExceptTime(): Date {
//    val year = this@getDateExceptTime.dateToLocalDate().year
//    val month = this@getDateExceptTime.dateToLocalDate().monthValue
//    val day = this@getDateExceptTime.dateToLocalDate().dayOfMonth
//    return LocalDateTime.of(year, month, day, 0, 0).localDateTimeToDate()
//}

fun OffsetDateTime.getDateExceptTime(): OffsetDateTime {
    val year = this@getDateExceptTime.year
    val month = this@getDateExceptTime.monthValue
    val day = this@getDateExceptTime.dayOfMonth
    return OffsetDateTime.of(LocalDate.of(year, month, day), LocalTime.of(0, 0),ZoneOffset.UTC)
}

fun Date.dateToLocalDateTime(): LocalDateTime = this@dateToLocalDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

fun Date.dateToLocalDate(): LocalDate = this@dateToLocalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDateTime.localDateTimeToDate(): Date = Date.from(this@localDateTimeToDate.atZone(ZoneId.systemDefault()).toInstant())

fun LocalDate.localDateToDate(): Date = Date.from(this@localDateToDate.atStartOfDay(ZoneId.systemDefault()).toInstant())