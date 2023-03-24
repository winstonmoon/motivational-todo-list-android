package com.moonwinston.motivationaltodolist.utils

import com.moonwinston.motivationaltodolist.data.TaskEntity
import org.junit.jupiter.api.Assertions.*
import java.time.OffsetDateTime

internal class CalculateUtilKtTest {

    @org.junit.jupiter.api.Test
    fun calculateRate_oneTaskEntity_notCompleted() {
        val taskEntity = listOf(TaskEntity(uid = 1, taskDate = OffsetDateTime.now(), task = "", isCompleted = false))
        val result = calculateRate(taskEntity)
        assertEquals(result, 0F)
    }

    @org.junit.jupiter.api.Test
    fun calculateRate_twoTaskEntity_oneCompleted() {
        val taskEntity = listOf(
            TaskEntity(uid = 1, taskDate = OffsetDateTime.now(), task = "", isCompleted = false),
            TaskEntity(uid = 2, taskDate = OffsetDateTime.now(), task = "", isCompleted = true)
        )
        val result = calculateRate(taskEntity)
        assertEquals(result, 0.5F)
    }
}