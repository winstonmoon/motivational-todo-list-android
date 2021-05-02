package com.moonwinston.motivationaltodolist.data

import java.sql.Time
import java.util.*

data class Task(
        //val date: Date,
        //val time: Time,
        val time: String,
        val task: String,
        val isGoalSet: Boolean,
        //val isCompleted: Boolean
)