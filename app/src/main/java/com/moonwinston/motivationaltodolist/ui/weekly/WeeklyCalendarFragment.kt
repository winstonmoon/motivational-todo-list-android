package com.moonwinston.motivationaltodolist.ui.weekly

import android.os.Bundle
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyCalendarBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class WeeklyCalendarFragment : BaseFragment<WeeklyViewModel, FragmentWeeklyCalendarBinding>() {
    override fun getViewBinding() = FragmentWeeklyCalendarBinding.inflate(layoutInflater)
    override val viewModel by viewModel<WeeklyViewModel>()
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private var diffWeek = 0
    private val weekList = mutableListOf<Date>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            diffWeek = it.getInt(DIFF_WEEK)
        }
    }

    override fun initViews() = with(binding) {
        //TODO fix dayOfWeek logic more simple, viewmodel
        val diffDays = diffWeek * 7
        val calendar = Calendar.getInstance()
        calendar.apply {
            add(Calendar.DATE, diffDays)
            firstDayOfWeek = Calendar.MONDAY
        }
        val diffDateFromMonday: Int =
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1) -6 else 2 - calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DATE, diffDateFromMonday)
        for (date in 1..7) {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val parsedMonth = resources.getString(MonthEnum.values()[month].monthNumber)
            val date = calendar.get(Calendar.DATE)

            weekList.add(
                SimpleDateFormat("yyyy-MM-dd").parse("$year-$parsedMonth-$date")
            )
            calendar.add(Calendar.DATE, 1)
        }
        //TODO fix dayOfWeek logic more simple, viewmodel

        mondayCustomPieChart.setPieChartViewDate(weekList[0])
        mondayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.mondayCustomPieChart.getPieChartViewDate())
        }

        tuesdayCustomPieChart.setPieChartViewDate(weekList[1])
        tuesdayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.tuesdayCustomPieChart.getPieChartViewDate())
        }

        wednesdayCustomPieChart.setPieChartViewDate(weekList[2])
        wednesdayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.wednesdayCustomPieChart.getPieChartViewDate())
        }

        thursdayCustomPieChart.setPieChartViewDate(weekList[3])
        thursdayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.thursdayCustomPieChart.getPieChartViewDate())
        }

        fridayCustomPieChart.setPieChartViewDate(weekList[4])
        fridayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.fridayCustomPieChart.getPieChartViewDate())
        }

        saturdayCustomPieChart.setPieChartViewDate(weekList[5])
        saturdayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.saturdayCustomPieChart.getPieChartViewDate())
        }

        sundayCustomPieChart.setPieChartViewDate(weekList[6])
        sundayCustomPieChart.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.sundayCustomPieChart.getPieChartViewDate())
        }
    }

    override fun observeData() {
        //TODO
        sharedViewModel.getAllTasks()
        sharedViewModel.tasksListLiveData.observe(viewLifecycleOwner) {
            val monList = mutableListOf<TaskEntity>()
            val tueList = mutableListOf<TaskEntity>()
            val wedList = mutableListOf<TaskEntity>()
            val thuList = mutableListOf<TaskEntity>()
            val friList = mutableListOf<TaskEntity>()
            val satList = mutableListOf<TaskEntity>()
            val sunList = mutableListOf<TaskEntity>()
            for (taskEntity in it) {
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
            if (sharedViewModel.getRate(monList) == 0F) {
                binding.mondayCustomPieChart.alpha = 0.2F
            } else {
                binding.mondayCustomPieChart.alpha = 1F
            }
            if (sharedViewModel.getRate(tueList) == 0F) {
                binding.tuesdayCustomPieChart.alpha = 0.2F
            } else {
                binding.tuesdayCustomPieChart.alpha = 1F
            }
            if (sharedViewModel.getRate(wedList) == 0F) {
                binding.wednesdayCustomPieChart.alpha = 0.2F
            } else {
                binding.wednesdayCustomPieChart.alpha = 1F
            }
            if (sharedViewModel.getRate(thuList) == 0F) {
                binding.thursdayCustomPieChart.alpha = 0.2F
            } else {
                binding.thursdayCustomPieChart.alpha = 1F
            }
            if (sharedViewModel.getRate(friList) == 0F) {
                binding.fridayCustomPieChart.alpha = 0.2F
            } else {
                binding.fridayCustomPieChart.alpha = 1F
            }
            if (sharedViewModel.getRate(satList) == 0F) {
                binding.saturdayCustomPieChart.alpha = 0.2F
            } else {
                binding.saturdayCustomPieChart.alpha = 1F
            }
            if (sharedViewModel.getRate(sunList) == 0F) {
                binding.sundayCustomPieChart.alpha = 0.2F
            } else {
                binding.sundayCustomPieChart.alpha = 1F
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