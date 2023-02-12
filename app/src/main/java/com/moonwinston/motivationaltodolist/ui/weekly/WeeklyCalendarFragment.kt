package com.moonwinston.motivationaltodolist.ui.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyCalendarBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val DIFF_WEEK = "diffWeek"

@AndroidEntryPoint
class WeeklyCalendarFragment : Fragment() {
    private val weeklySharedViewModel: WeeklyViewModel by activityViewModels()
    private lateinit var binding: FragmentWeeklyCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var diffDays = 0
        arguments?.takeIf { it.containsKey(DIFF_WEEK) }?.apply {
            diffDays = getInt(DIFF_WEEK) * 7
        }
        val daysOfWeek = weeklySharedViewModel.createDaysOfWeek(diffDays)
        weeklySharedViewModel.setDaysOfWeek(daysOfWeek)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeeklyCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mondayCustomPieChart.setOnClickListener {
            weeklySharedViewModel.setSelectedDate(binding.mondayCustomPieChart.pieChartViewDate)
        }
        binding.tuesdayCustomPieChart.setOnClickListener {
            weeklySharedViewModel.setSelectedDate(binding.tuesdayCustomPieChart.pieChartViewDate)
        }
        binding.wednesdayCustomPieChart.setOnClickListener {
            weeklySharedViewModel.setSelectedDate(binding.wednesdayCustomPieChart.pieChartViewDate)
        }
        binding.thursdayCustomPieChart.setOnClickListener {
            weeklySharedViewModel.setSelectedDate(binding.thursdayCustomPieChart.pieChartViewDate)
        }
        binding.fridayCustomPieChart.setOnClickListener {
            weeklySharedViewModel.setSelectedDate(binding.fridayCustomPieChart.pieChartViewDate)
        }
        binding.saturdayCustomPieChart.setOnClickListener {
            weeklySharedViewModel.setSelectedDate(binding.saturdayCustomPieChart.pieChartViewDate)
        }
        binding.sundayCustomPieChart.setOnClickListener {
            weeklySharedViewModel.setSelectedDate(binding.sundayCustomPieChart.pieChartViewDate)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weeklySharedViewModel.daysOfWeek.collect { daysOfWeek ->
                    binding.mondayCustomPieChart.pieChartViewDate = daysOfWeek[0]
                    binding.tuesdayCustomPieChart.pieChartViewDate = daysOfWeek[1]
                    binding.wednesdayCustomPieChart.pieChartViewDate = daysOfWeek[2]
                    binding.thursdayCustomPieChart.pieChartViewDate = daysOfWeek[3]
                    binding.fridayCustomPieChart.pieChartViewDate = daysOfWeek[4]
                    binding.saturdayCustomPieChart.pieChartViewDate = daysOfWeek[5]
                    binding.sundayCustomPieChart.pieChartViewDate = daysOfWeek[6]
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weeklySharedViewModel.weeklyTasks.collect { taskEntities ->
                    val weeklyRates = weeklySharedViewModel.getWeeklyRateListsFromAllTasks(taskEntities)
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
                }
            }
        }
    }
}