package com.moonwinston.motivationaltodolist.ui.weekly

import com.moonwinston.motivationaltodolist.data.TaskRepository
import com.moonwinston.motivationaltodolist.data.UserPreferencesRepository
import com.moonwinston.motivationaltodolist.ui.monthly.MonthlyViewModel
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime

class WeeklyViewModelTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var viewModel: WeeklyViewModel

    @Before
    fun setUp() {
        taskRepository = mockk()
        userPreferencesRepository = mockk()
        viewModel = WeeklyViewModel(taskRepository, userPreferencesRepository)
    }

    @Test
    fun createDaysOfWeek() {
        //TODO
        //GIVEN

        //WHEN
        val result = viewModel.createDaysOfWeek(2)
        //THEN
        Assert.assertEquals(result, listOf(OffsetDateTime.now()))
    }

    @Test
    fun getWeeklyRateListsFromAllTasks() {
    }
}