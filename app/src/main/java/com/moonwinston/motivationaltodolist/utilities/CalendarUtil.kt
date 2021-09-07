package com.moonwinston.motivationaltodolist.utilities

import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {

    fun getToday(): Date = Calendar.getInstance().time

    fun getNonExistDate(): Date = SimpleDateFormat("yyyy-MM-dd").parse("0000-00-00")
}