package com.moonwinston.motivationaltodolist.ui.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyCalendarBinding
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class WeeklyCalendarFragment : Fragment() {

    private lateinit var binding: FragmentWeeklyCalendarBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()
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
        //TODO fix dayOfWeek logic more simple, viewmodel
        val diffDays = diffWeek * 7
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DATE, diffDays)
            firstDayOfWeek = Calendar.MONDAY
        }
        val diffDateFromMonday =
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1) -6 else 2 - calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DATE, diffDateFromMonday)
        (1..7).forEach { _ ->
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val parsedMonth = resources.getString(MonthEnum.values()[month].monthNumber)
            val date = calendar.get(Calendar.DATE)

            SimpleDateFormat("yyyy-MM-dd").parse("$year-$parsedMonth-$date")
                ?.let { weekList.add(it) }
            calendar.add(Calendar.DATE, 1)
        }
        //TODO fix dayOfWeek logic more simple, viewmodel

        binding.mondayCustomPieChart.setPieChartViewDate(weekList[0])
        binding.mondayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.mondayCustomPieChart.getPieChartViewDate())
        }

        binding.tuesdayCustomPieChart.setPieChartViewDate(weekList[1])
        binding.tuesdayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.tuesdayCustomPieChart.getPieChartViewDate())
        }

        binding.wednesdayCustomPieChart.setPieChartViewDate(weekList[2])
        binding.wednesdayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.wednesdayCustomPieChart.getPieChartViewDate())
        }

        binding.thursdayCustomPieChart.setPieChartViewDate(weekList[3])
        binding.thursdayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.thursdayCustomPieChart.getPieChartViewDate())
        }

        binding.fridayCustomPieChart.setPieChartViewDate(weekList[4])
        binding.fridayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.fridayCustomPieChart.getPieChartViewDate())
        }

        binding.saturdayCustomPieChart.setPieChartViewDate(weekList[5])
        binding.saturdayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.saturdayCustomPieChart.getPieChartViewDate())
        }

        binding.sundayCustomPieChart.setPieChartViewDate(weekList[6])
        binding.sundayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.sundayCustomPieChart.getPieChartViewDate())
        }

        //TODO
        sharedViewModel.getAllTasks()
        sharedViewModel.tasksListLiveData.observe(viewLifecycleOwner) { taskEntities ->
            val monList = mutableListOf<TaskEntity>()
            val tueList = mutableListOf<TaskEntity>()
            val wedList = mutableListOf<TaskEntity>()
            val thuList = mutableListOf<TaskEntity>()
            val friList = mutableListOf<TaskEntity>()
            val satList = mutableListOf<TaskEntity>()
            val sunList = mutableListOf<TaskEntity>()
            taskEntities.forEach { taskEntity ->
                when (taskEntity.taskDate) {
                    weekList[0] -> monList.add(taskEntity)
                    weekList[1] -> tueList.add(taskEntity)
                    weekList[2] -> wedList.add(taskEntity)
                    weekList[3] -> thuList.add(taskEntity)
                    weekList[4] -> friList.add(taskEntity)
                    weekList[5] -> satList.add(taskEntity)
                    weekList[6] -> sunList.add(taskEntity)
                }
            }

            val monRate = sharedViewModel.getRate(monList)
            val tueRate = sharedViewModel.getRate(tueList)
            val wedRate = sharedViewModel.getRate(wedList)
            val thuRate = sharedViewModel.getRate(thuList)
            val friRate = sharedViewModel.getRate(friList)
            val satRate = sharedViewModel.getRate(satList)
            val sunRate = sharedViewModel.getRate(sunList)
            binding.mondayCustomPieChart.setPercentage(monRate)
            binding.tuesdayCustomPieChart.setPercentage(tueRate)
            binding.wednesdayCustomPieChart.setPercentage(wedRate)
            binding.thursdayCustomPieChart.setPercentage(thuRate)
            binding.fridayCustomPieChart.setPercentage(friRate)
            binding.saturdayCustomPieChart.setPercentage(satRate)
            binding.sundayCustomPieChart.setPercentage(sunRate)

            binding.mondayCustomPieChart.alpha = if (monRate == 0F) 0.2F else 1F
            binding.tuesdayCustomPieChart.alpha = if (tueRate == 0F) 0.2F else 1F
            binding.wednesdayCustomPieChart.alpha = if (wedRate == 0F) 0.2F else 1F
            binding.thursdayCustomPieChart.alpha = if (thuRate == 0F) 0.2F else 1F
            binding.fridayCustomPieChart.alpha = if (friRate == 0F) 0.2F else 1F
            binding.saturdayCustomPieChart.alpha = if (satRate == 0F) 0.2F else 1F
            binding.sundayCustomPieChart.alpha = if (sunRate == 0F) 0.2F else 1F
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.getAllTasks()
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