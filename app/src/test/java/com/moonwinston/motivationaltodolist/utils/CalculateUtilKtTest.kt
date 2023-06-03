package com.moonwinston.motivationaltodolist.utils

import com.moonwinston.motivationaltodolist.data.TaskEntity
import org.junit.Assert.*

import org.junit.Test
import java.time.OffsetDateTime

class CalculateUtilKtTest {

    @Test
    fun calculateRate_oneTaskEntity_notCompleted() {
        val taskEntity = listOf(
            TaskEntity(
                uid = 1,
                taskDate = OffsetDateTime.now(),
                task = "",
                isCompleted = false
            )
        )
        val result = calculateRate(taskEntity)
        assertEquals(result, 0F)
    }

    @Test
    fun calculateRate_twoTaskEntity_oneCompleted() {
        val taskEntity = listOf(
            TaskEntity(uid = 1, taskDate = OffsetDateTime.now(), task = "", isCompleted = false),
            TaskEntity(uid = 2, taskDate = OffsetDateTime.now(), task = "", isCompleted = true)
        )
        val result = calculateRate(taskEntity)
        assertEquals(result, 0.5F)
    }
}