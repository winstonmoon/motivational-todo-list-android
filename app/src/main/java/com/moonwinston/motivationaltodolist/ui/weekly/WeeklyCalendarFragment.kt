package com.moonwinston.motivationaltodolist.ui.weekly

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyCalendarBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

const val DIFF_WEEK = "diffWeek"

@AndroidEntryPoint
class WeeklyCalendarFragment: BaseFragment<FragmentWeeklyCalendarBinding, WeeklyViewModel>() {
    override fun getViewBinding() = FragmentWeeklyCalendarBinding.inflate(layoutInflater)
    override val viewModel: WeeklyViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var diffDays = 0
        arguments?.takeIf { it.containsKey(DIFF_WEEK) }?.apply {
            diffDays = getInt(DIFF_WEEK) * 7
        }
        val daysOfWeek = viewModel.createDaysOfWeek(diffDays)
        viewModel.setDaysOfWeek(daysOfWeek)
    }

    override fun initViews() {
    }
    override fun initListeners() {
        binding.mondayCustomPieChart.setOnClickListener {
            viewModel.setSelectedDate(binding.mondayCustomPieChart.pieChartViewDate)
        }
        binding.tuesdayCustomPieChart.setOnClickListener {
            viewModel.setSelectedDate(binding.tuesdayCustomPieChart.pieChartViewDate)
        }
        binding.wednesdayCustomPieChart.setOnClickListener {
            viewModel.setSelectedDate(binding.wednesdayCustomPieChart.pieChartViewDate)
        }
        binding.thursdayCustomPieChart.setOnClickListener {
            viewModel.setSelectedDate(binding.thursdayCustomPieChart.pieChartViewDate)
        }
        binding.fridayCustomPieChart.setOnClickListener {
            viewModel.setSelectedDate(binding.fridayCustomPieChart.pieChartViewDate)
        }
        binding.saturdayCustomPieChart.setOnClickListener {
            viewModel.setSelectedDate(binding.saturdayCustomPieChart.pieChartViewDate)
        }
        binding.sundayCustomPieChart.setOnClickListener {
            viewModel.setSelectedDate(binding.sundayCustomPieChart.pieChartViewDate)
        }
    }
    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.daysOfWeek.onEach { daysOfWeek ->
                        binding.mondayCustomPieChart.pieChartViewDate = daysOfWeek[0]
                        binding.tuesdayCustomPieChart.pieChartViewDate = daysOfWeek[1]
                        binding.wednesdayCustomPieChart.pieChartViewDate = daysOfWeek[2]
                        binding.thursdayCustomPieChart.pieChartViewDate = daysOfWeek[3]
                        binding.fridayCustomPieChart.pieChartViewDate = daysOfWeek[4]
                        binding.saturdayCustomPieChart.pieChartViewDate = daysOfWeek[5]
                        binding.sundayCustomPieChart.pieChartViewDate = daysOfWeek[6]
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
                launch {
                    viewModel.weeklyTasks.onEach { taskEntities ->
                        val weeklyRates = viewModel.getWeeklyRateListsFromAllTasks(taskEntities)
                        binding.mondayCustomPieChart.alpha = if (weeklyRates[0] == 0F) 0.2F else 1F
                        binding.tuesdayCustomPieChart.alpha = if (weeklyRates[1] == 0F) 0.2F else 1F
                        binding.wednesdayCustomPieChart.alpha = if (weeklyRates[2] == 0F) 0.2F else 1F
                        binding.thursdayCustomPieChart.alpha = if (weeklyRates[3] == 0F) 0.2F else 1F
                        binding.fridayCustomPieChart.alpha = if (weeklyRates[4] == 0F) 0.2F else 1F
                        binding.saturdayCustomPieChart.alpha = if (weeklyRates[5] == 0F) 0.2F else 1F
                        binding.sundayCustomPieChart.alpha = if (weeklyRates[6] == 0F) 0.2F else 1F
                        binding.mondayCustomPieChart.updatePercentage(weeklyRates[0])
                        binding.tuesdayCustomPieChart.updatePercentage(weeklyRates[1])
                        binding.wednesdayCustomPieChart.updatePercentage(weeklyRates[2])
                        binding.thursdayCustomPieChart.updatePercentage(weeklyRates[3])
                        binding.fridayCustomPieChart.updatePercentage(weeklyRates[4])
                        binding.saturdayCustomPieChart.updatePercentage(weeklyRates[5])
                        binding.sundayCustomPieChart.updatePercentage(weeklyRates[6])
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
            }
        }
    }
}