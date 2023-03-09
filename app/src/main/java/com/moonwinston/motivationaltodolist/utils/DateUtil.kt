package com.moonwinston.motivationaltodolist.utils

import java.time.*

fun dateOfToday(): OffsetDateTime = OffsetDateTime.of(LocalDate.now(), LocalTime.of(0,0), ZoneOffset.UTC)

fun nonExistDate(): OffsetDateTime = OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.of(0, 0), ZoneOffset.UTC )

fun OffsetDateTime.getDateExceptTime(): OffsetDateTime {
    val year = this@getDateExceptTime.year
    val month = this@getDateExceptTime.monthValue
    val day = this@getDateExceptTime.dayOfMonth
    return OffsetDateTime.of(LocalDate.of(year, month, day), LocalTime.of(0, 0),ZoneOffset.UTC)
}

fun OffsetDateTime.getEpoch(): Long {
    return this@getEpoch.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun OffsetDateTime.getEpochTest(): Long {
    return this@getEpochTest.toInstant().toEpochMilli()
}