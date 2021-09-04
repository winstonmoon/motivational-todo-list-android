package com.moonwinston.motivationaltodolist.data

import java.text.SimpleDateFormat
import java.util.*

data class CalendarDate(
    var calendarDate: Date,
) {
    constructor() : this(SimpleDateFormat("yyyy-MM-dd").parse("0000-00-00"))
}