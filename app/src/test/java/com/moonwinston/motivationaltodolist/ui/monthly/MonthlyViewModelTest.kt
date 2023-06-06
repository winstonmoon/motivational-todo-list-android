package com.moonwinston.motivationaltodolist.ui.monthly

import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before

import org.junit.Test
import java.time.OffsetDateTime

class MonthlyViewModelTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var viewModel: MonthlyViewModel

    @Before
    fun setUp() {
        taskRepository = mockk()
        userPreferencesRepository = mockk()
        viewModel = MonthlyViewModel(taskRepository, userPreferencesRepository)
    }

    @Test
    fun createDaysOfMonth() {
        //TODO
        //GIVEN

        //WHEN
        val result = viewModel.createDaysOfMonth(2)
        //THEN
        Assert.assertEquals(result, listOf(OffsetDateTime.now()))
    }
}