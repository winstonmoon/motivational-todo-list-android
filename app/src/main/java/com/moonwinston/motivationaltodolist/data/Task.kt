package com.moonwinston.motivationaltodolist.data

import java.sql.Time
import java.util.*

data class Task(
//        var month: Int,
//        var dayOfMonth: Int,
//        var dayOfWeek: Int,
        //val time: Time,
        var time: String,
        var task: String,
        var isGoalSet: Boolean,
//        var isCompleted: Boolean
)