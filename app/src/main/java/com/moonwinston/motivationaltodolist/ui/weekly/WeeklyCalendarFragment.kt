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
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyCalendarBinding
import com.moonwinston.motivationaltodolist.utils.localDateToDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

const val DIFF_WEEK = "diffWeek"

@AndroidEntryPoint
class WeeklyCalendarFragment : Fragment() {
    private val weeklySharedViewModel: WeeklyViewModel by activityViewModels()
    private lateinit var binding: FragmentWeeklyCalendarBinding
    private val daysOfWeek = mutableListOf<Date>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var diffDays = 0
        arguments?.takeIf { it.containsKey(DIFF_WEEK) }?.apply {
            diffDays = getInt(DIFF_WEEK) * 7
        }
        //TODO make simple
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DATE, diffDays)
            firstDayOfWeek = Calendar.MONDAY
            val diffDateFromMonday =
                if (this@apply.get(Calendar.DAY_OF_WEEK) == 1) -6
                else 2 - this@apply.get(Calendar.DAY_OF_WEEK)
            add(Calendar.DATE, diffDateFromMonday)
        }

        (1..7).forEach { _ ->
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val date = calendar.get(Calendar.DATE)
            daysOfWeek.add(LocalDate.of(year, month + 1, date).localDateToDate())
            calendar.add(Calendar.DATE, 1)
        }
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
        binding.mondayCustomPieChart.pieChartViewDate = daysOfWeek[0]
        binding.tuesdayCustomPieChart.pieChartViewDate = daysOfWeek[1]
        binding.wednesdayCustomPieChart.pieChartViewDate = daysOfWeek[2]
        binding.thursdayCustomPieChart.pieChartViewDate = daysOfWeek[3]
        binding.fridayCustomPieChart.pieChartViewDate = daysOfWeek[4]
        binding.saturdayCustomPieChart.pieChartViewDate = daysOfWeek[5]
        binding.sundayCustomPieChart.pieChartViewDate = daysOfWeek[6]
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

        //TODO make simple
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weeklySharedViewModel.allTasks.collect {
                    val monList = mutableListOf<TaskEntity>()
                    val tueList = mutableListOf<TaskEntity>()
                    val wedList = mutableListOf<TaskEntity>()
                    val thuList = mutableListOf<TaskEntity>()
                    val friList = mutableListOf<TaskEntity>()
                    val satList = mutableListOf<TaskEntity>()
                    val sunList = mutableListOf<TaskEntity>()
                    for (taskEntity in it) {
                        when (taskEntity.taskDate) {
                            daysOfWeek[0] -> monList.add(taskEntity)
                            daysOfWeek[1] -> tueList.add(taskEntity)
                            daysOfWeek[2] -> wedList.add(taskEntity)
                            daysOfWeek[3] -> thuList.add(taskEntity)
                            daysOfWeek[4] -> friList.add(taskEntity)
                            daysOfWeek[5] -> satList.add(taskEntity)
                            daysOfWeek[6] -> sunList.add(taskEntity)
                        }
                    }
                    if (weeklySharedViewModel.calculateRate(monList) == 0F) {
                        binding.mondayCustomPieChart.alpha = 0.2F
                    } else {
                        binding.mondayCustomPieChart.alpha = 1F
                    }
                    if (weeklySharedViewModel.calculateRate(tueList) == 0F) {
                        binding.tuesdayCustomPieChart.alpha = 0.2F
                    } else {
                        binding.tuesdayCustomPieChart.alpha = 1F
                    }
                    if (weeklySharedViewModel.calculateRate(wedList) == 0F) {
                        binding.wednesdayCustomPieChart.alpha = 0.2F
                    } else {
                        binding.wednesdayCustomPieChart.alpha = 1F
                    }
                    if (weeklySharedViewModel.calculateRate(thuList) == 0F) {
                        binding.thursdayCustomPieChart.alpha = 0.2F
                    } else {
                        binding.thursdayCustomPieChart.alpha = 1F
                    }
                    if (weeklySharedViewModel.calculateRate(friList) == 0F) {
                        binding.fridayCustomPieChart.alpha = 0.2F
                    } else {
                        binding.fridayCustomPieChart.alpha = 1F
                    }
                    if (weeklySharedViewModel.calculateRate(satList) == 0F) {
                        binding.saturdayCustomPieChart.alpha = 0.2F
                    } else {
                        binding.saturdayCustomPieChart.alpha = 1F
                    }
                    if (weeklySharedViewModel.calculateRate(sunList) == 0F) {
                        binding.sundayCustomPieChart.alpha = 0.2F
                    } else {
                        binding.sundayCustomPieChart.alpha = 1F
                    }
                    val monRate = weeklySharedViewModel.calculateRate(monList)
                    val tueRate = weeklySharedViewModel.calculateRate(tueList)
                    val wedRate = weeklySharedViewModel.calculateRate(wedList)
                    val thuRate = weeklySharedViewModel.calculateRate(thuList)
                    val friRate = weeklySharedViewModel.calculateRate(friList)
                    val satRate = weeklySharedViewModel.calculateRate(satList)
                    val sunRate = weeklySharedViewModel.calculateRate(sunList)
                    binding.mondayCustomPieChart.updatePercentage(monRate)
                    binding.tuesdayCustomPieChart.updatePercentage(tueRate)
                    binding.wednesdayCustomPieChart.updatePercentage(wedRate)
                    binding.thursdayCustomPieChart.updatePercentage(thuRate)
                    binding.fridayCustomPieChart.updatePercentage(friRate)
                    binding.saturdayCustomPieChart.updatePercentage(satRate)
                    binding.sundayCustomPieChart.updatePercentage(sunRate)
                }
            }
        }
    }

    private fun createDaysOfWeek() {

    }

//    override fun onResume() {
//        super.onResume()
//        mainViewModel.getAllTasks()
//        //TODO
//    }
}