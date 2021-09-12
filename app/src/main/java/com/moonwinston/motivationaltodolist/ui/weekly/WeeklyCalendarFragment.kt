package com.moonwinston.motivationaltodolist.ui.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyCalendarBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.ui.monthly.MonthlyViewModel
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class WeeklyCalendarFragment : BaseFragment<WeeklyViewModel, FragmentWeeklyCalendarBinding>() {
    override fun getViewBinding(): FragmentWeeklyCalendarBinding =
        FragmentWeeklyCalendarBinding.inflate(layoutInflater)

    override val viewModel by viewModel<WeeklyViewModel>()
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private var diffWeek: Int = 0
    private val weekList = mutableListOf<Date>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            diffWeek = it.getInt(DIFF_WEEK)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val diffDays = diffWeek * 7
        val calendar: Calendar = Calendar.getInstance()
        calendar.apply {
            add(Calendar.DATE, diffDays)
            firstDayOfWeek = Calendar.MONDAY
        }

        //TODO fix dayOfWeek logic more simple, viewmodel
        val diffDate: Int =
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1) -6 else 2 - calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DATE, diffDate)
        //TODO
        for (date in 1..7) {
            //TODO
            val year: Int = calendar.get(Calendar.YEAR)
            val month: Int = calendar.get(Calendar.MONTH)
            val parsedMonth = resources.getString(MonthEnum.values()[month].monthNumber)
            val date: Int = calendar.get(Calendar.DATE)

            weekList.add(
                SimpleDateFormat("yyyy-MM-dd").parse("$year-$parsedMonth-$date")
            )
            calendar.add(Calendar.DATE, 1)
        }

        sharedViewModel.getAll()
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
                binding.customviewPiechartMonday.alpha = 0.2F
            }
            if (sharedViewModel.getRate(tueList) == 0F) {
                binding.customviewPiechartTuesday.alpha = 0.2F
            }
            if (sharedViewModel.getRate(wedList) == 0F) {
                binding.customviewPiechartWednesday.alpha = 0.2F
            }
            if (sharedViewModel.getRate(thuList) == 0F) {
                binding.customviewPiechartThursday.alpha = 0.2F
            }
            if (sharedViewModel.getRate(friList) == 0F) {
                binding.customviewPiechartFriday.alpha = 0.2F
            }
            if (sharedViewModel.getRate(satList) == 0F) {
                binding.customviewPiechartSaturday.alpha = 0.2F
            }
            if (sharedViewModel.getRate(sunList) == 0F) {
                binding.customviewPiechartSunday.alpha = 0.2F
            }
            binding.customviewPiechartMonday.setPercentage(sharedViewModel.getRate(monList))
            binding.customviewPiechartTuesday.setPercentage(sharedViewModel.getRate(tueList))
            binding.customviewPiechartWednesday.setPercentage(sharedViewModel.getRate(wedList))
            binding.customviewPiechartThursday.setPercentage(sharedViewModel.getRate(thuList))
            binding.customviewPiechartFriday.setPercentage(sharedViewModel.getRate(friList))
            binding.customviewPiechartSaturday.setPercentage(sharedViewModel.getRate(satList))
            binding.customviewPiechartSunday.setPercentage(sharedViewModel.getRate(sunList))
        }

        binding.customviewPiechartMonday.apply {
            setPieChartViewDate(weekList[0])
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
        }

        binding.customviewPiechartTuesday.apply {
            setPieChartViewDate(weekList[1])
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
        }

        binding.customviewPiechartWednesday.apply {
            setPieChartViewDate(weekList[2])
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
        }

        binding.customviewPiechartThursday.apply {
            setPieChartViewDate(weekList[3])
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
        }

        binding.customviewPiechartFriday.apply {
            setPieChartViewDate(weekList[4])
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
        }

        binding.customviewPiechartSaturday.apply {
            setPieChartViewDate(weekList[5])
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
        }

        binding.customviewPiechartSunday.apply {
            setPieChartViewDate(weekList[6])
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
        }

        //TODO implement set date
        binding.customviewPiechartMonday.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.customviewPiechartMonday.getPieChartViewDate())
        }
        binding.customviewPiechartTuesday.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.customviewPiechartTuesday.getPieChartViewDate())
        }
        binding.customviewPiechartWednesday.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.customviewPiechartWednesday.getPieChartViewDate())
        }
        binding.customviewPiechartThursday.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.customviewPiechartThursday.getPieChartViewDate())
        }
        binding.customviewPiechartFriday.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.customviewPiechartFriday.getPieChartViewDate())
        }
        binding.customviewPiechartSaturday.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.customviewPiechartSaturday.getPieChartViewDate())
        }
        binding.customviewPiechartSunday.setOnClickListener {
            sharedViewModel.setSelectedDate(binding.customviewPiechartSunday.getPieChartViewDate())
        }
    }

    override fun initViews() = with(binding) {
    }

    override fun observeData() {
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.getAll()
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