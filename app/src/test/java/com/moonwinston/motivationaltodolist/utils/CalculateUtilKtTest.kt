package com.moonwinston.motivationaltodolist.utils

import com.moonwinston.motivationaltodolist.data.TaskEntity
import org.junit.Assert.*

import org.junit.Test
import java.time.OffsetDateTime

class CalculateUtilKtTest {

    @Test
    fun calculateRate_oneTaskEntity_notCompleted() {
        //Given
        val taskEntity = listOf(TaskEntity(uid = 1, taskDate = OffsetDateTime.now(), task = "", isCompleted = false))
        //When
        val result = calculateRate(taskEntity)
        //Then
        assertEquals(result, 0F)
    }

    @Test
    fun calculateRate_twoTaskEntities_oneCompleted() {
        //Given
        val taskEntity = listOf(
            TaskEntity(uid = 1, taskDate = OffsetDateTime.now(), task = "", isCompleted = false),
            TaskEntity(uid = 2, taskDate = OffsetDateTime.now(), task = "", isCompleted = true)
        )
        //When
        val result = calculateRate(taskEntity)
        //Then
        assertEquals(result, 0.5F)
    }

    @Test
    fun calculateRate_twoTaskEntities_twoCompleted() {
        //Given
        val taskEntity = listOf(
            TaskEntity(uid = 1, taskDate = OffsetDateTime.now(), task = "", isCompleted = true),
            TaskEntity(uid = 2, taskDate = OffsetDateTime.now(), task = "", isCompleted = true)
        )
        //When
        val result = calculateRate(taskEntity)
        //Then
        assertEquals(result, 1F)
    }
}