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

@AndroidEntryPoint
class WeeklyCalendarFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val weeklySharedViewModel: WeeklyViewModel by activityViewModels()
    private lateinit var binding: FragmentWeeklyCalendarBinding
    private var diffWeek = 0
    private val weekList = mutableListOf<Date>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            diffWeek = it.getInt(DIFF_WEEK)
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
        //TODO fix dayOfWeek logic more simple, viewmodel
        val diffDays = diffWeek * 7
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DATE, diffDays)
            firstDayOfWeek = Calendar.MONDAY
        }
        val diffDateFromMonday =
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1) -6
            else 2 - calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DATE, diffDateFromMonday)
        (1..7).forEach { _ ->
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val date = calendar.get(Calendar.DATE)
            weekList.add(LocalDate.of(year, month + 1, date).localDateToDate())
            calendar.add(Calendar.DATE, 1)
        }
        //TODO fix dayOfWeek logic more simple, viewmodel

        binding.mondayCustomPieChart.setPieChartViewDate(weekList[0])
        binding.mondayCustomPieChart.setOnClickListener {
            mainViewModel.setSelectedDate(binding.mondayCustomPieChart.getPieChartViewDate())
        }

        binding.tuesdayCustomPieChart.setPieChartViewDate(weekList[1])
        binding.tuesdayCustomPieChart.setOnClickListener {
            mainViewModel.setSelectedDate(binding.tuesdayCustomPieChart.getPieChartViewDate())
        }

        binding.wednesdayCustomPieChart.setPieChartViewDate(weekList[2])
        binding.wednesdayCustomPieChart.setOnClickListener {
            mainViewModel.setSelectedDate(binding.wednesdayCustomPieChart.getPieChartViewDate())
        }

        binding.thursdayCustomPieChart.setPieChartViewDate(weekList[3])
        binding.thursdayCustomPieChart.setOnClickListener {
            mainViewModel.setSelectedDate(binding.thursdayCustomPieChart.getPieChartViewDate())
        }

        binding.fridayCustomPieChart.setPieChartViewDate(weekList[4])
        binding.fridayCustomPieChart.setOnClickListener {
            mainViewModel.setSelectedDate(binding.fridayCustomPieChart.getPieChartViewDate())
        }

        binding.saturdayCustomPieChart.setPieChartViewDate(weekList[5])
        binding.saturdayCustomPieChart.setOnClickListener {
            mainViewModel.setSelectedDate(binding.saturdayCustomPieChart.getPieChartViewDate())
        }

        binding.sundayCustomPieChart.setPieChartViewDate(weekList[6])
        binding.sundayCustomPieChart.setOnClickListener {
            mainViewModel.setSelectedDate(binding.sundayCustomPieChart.getPieChartViewDate())
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
                    weekList[0] -> monList?.add(taskEntity)
                    weekList[1] -> tueList?.add(taskEntity)
                    weekList[2] -> wedList?.add(taskEntity)
                    weekList[3] -> thuList?.add(taskEntity)
                    weekList[4] -> friList?.add(taskEntity)
                    weekList[5] -> satList?.add(taskEntity)
                    weekList[6] -> sunList?.add(taskEntity)
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

    companion object {
        private const val DIFF_WEEK = "diffWeek"
        fun newInstance(diffWeek: Int) = WeeklyCalendarFragment().apply {
            arguments = Bundle().apply {
                putInt(DIFF_WEEK, diffWeek)
            }
        }
    }
}