package com.moonwinston.motivationaltodolist.utils

import android.icu.util.TimeZone
import java.time.*

val zoneId: ZoneId = ZoneId.of(TimeZone.getDefault().id)

fun dateOfToday(): OffsetDateTime = OffsetDateTime.of(LocalDate.now(), LocalTime.of(0,0), ZoneOffset.UTC)

fun nonExistDate(): OffsetDateTime = OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.of(0, 0), ZoneOffset.UTC )

fun OffsetDateTime.getDateExceptTime(): OffsetDateTime {
    val year = this@getDateExceptTime.year
    val month = this@getDateExceptTime.monthValue
    val day = this@getDateExceptTime.dayOfMonth
    return OffsetDateTime.of(LocalDate.of(year, month, day), LocalTime.of(0, 0),ZoneOffset.UTC)
}

fun OffsetDateTime.getEpochMilli(): Long {
    return this@getEpochMilli.toInstant().toEpochMilli()
}

fun OffsetDateTime.getZonedEpochMilliFromOffset(): Long {
    return this@getZonedEpochMilliFromOffset.atZoneSimilarLocal(zoneId).toInstant().toEpochMilli()
}