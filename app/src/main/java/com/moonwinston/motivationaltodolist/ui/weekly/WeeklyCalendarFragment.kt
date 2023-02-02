package com.moonwinston.motivationaltodolist.ui.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyCalendarBinding
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
import com.moonwinston.motivationaltodolist.utils.getDateExceptTime
import com.moonwinston.motivationaltodolist.utils.localDateToDate
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

const val DIFF_WEEK = "diffWeek"

@AndroidEntryPoint
class WeeklyCalendarFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
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
        binding.weeklyViewModel = weeklySharedViewModel

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

        //TODO
//        mainViewModel.getAllTasks()
        mainViewModel.tasksListLiveData.observe(viewLifecycleOwner) { taskEntities ->
            val monList: MutableList<TaskEntity>? = null
            val tueList: MutableList<TaskEntity>? = null
            val wedList: MutableList<TaskEntity>? = null
            val thuList: MutableList<TaskEntity>? = null
            val friList: MutableList<TaskEntity>? = null
            val satList: MutableList<TaskEntity>? = null
            val sunList: MutableList<TaskEntity>? = null
            taskEntities.forEach { taskEntity ->
                when (taskEntity.taskDate.getDateExceptTime()) {
                    daysOfWeek[0] -> monList?.add(taskEntity)
                    daysOfWeek[1] -> tueList?.add(taskEntity)
                    daysOfWeek[2] -> wedList?.add(taskEntity)
                    daysOfWeek[3] -> thuList?.add(taskEntity)
                    daysOfWeek[4] -> friList?.add(taskEntity)
                    daysOfWeek[5] -> satList?.add(taskEntity)
                    daysOfWeek[6] -> sunList?.add(taskEntity)
                }
            }
            monList?.let { tasksList ->
                weeklySharedViewModel.setRate(dayOfWeek = DayOfWeek.MONDAY, tasksList = tasksList)
            }
            tueList?.let { tasksList ->
                weeklySharedViewModel.setRate(dayOfWeek = DayOfWeek.TUESDAY, tasksList = tasksList)
            }
            wedList?.let { tasksList ->
                weeklySharedViewModel.setRate(dayOfWeek = DayOfWeek.WEDNESDAY, tasksList = tasksList)
            }
            thuList?.let { tasksList ->
                weeklySharedViewModel.setRate(dayOfWeek = DayOfWeek.THURSDAY, tasksList = tasksList)
            }
            friList?.let { tasksList ->
                weeklySharedViewModel.setRate(dayOfWeek = DayOfWeek.FRIDAY, tasksList = tasksList)
            }
            satList?.let { tasksList ->
                weeklySharedViewModel.setRate(dayOfWeek = DayOfWeek.SATURDAY, tasksList = tasksList)
            }
            sunList?.let { tasksList ->
                weeklySharedViewModel.setRate(dayOfWeek = DayOfWeek.SUNDAY, tasksList = tasksList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        mainViewModel.getAllTasks()
        //TODO
    }
}