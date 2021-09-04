package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.moonwinston.motivationaltodolist.data.CalendarDate
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyCalendarBinding
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class WeeklyCalendarFragment : Fragment() {
    private lateinit var binding: FragmentWeeklyCalendarBinding
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private var diffWeek: Int = 0

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
        diffWeek *= 7
        val calendar: Calendar = Calendar.getInstance()
        calendar.apply {
            add(Calendar.DATE, diffWeek)
            firstDayOfWeek = Calendar.MONDAY
        }

        //TODO fix dayOfWeek logic more simple
        //TODO viewmodel
        val diffDate: Int = 2 - calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DATE, diffDate)
        //TODO
        val weekList = mutableListOf<CalendarDate>()
        for (date in 1..7) {
            //TODO
            val year: Int = calendar.get(Calendar.YEAR)
            val month: Int = calendar.get(Calendar.MONTH)
            val parsedMonth = resources.getString(MonthEnum.values()[month].monthNumber)
            val date: Int = calendar.get(Calendar.DATE)

            weekList.add(
                CalendarDate(SimpleDateFormat("yyyy-MM-dd").parse("$year-$parsedMonth-$date"))
            )
            calendar.add(Calendar.DATE, 1)
        }

        binding.customviewPiechartMonday.apply {
            setCalendarDate(weekList[0].calendarDate)
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
            setPercentage(0.5F)
        }

        binding.customviewPiechartTuesday.apply {
            setCalendarDate(weekList[1].calendarDate)
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
            setPercentage(0.5F)
        }

        binding.customviewPiechartWednesday.apply {
            setCalendarDate(weekList[2].calendarDate)
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
            setPercentage(0.5F)
        }

        binding.customviewPiechartThursday.apply {
            setCalendarDate(weekList[3].calendarDate)
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
            setPercentage(0.5F)
        }

        binding.customviewPiechartFriday.apply {
            setCalendarDate(weekList[4].calendarDate)
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
            setPercentage(0.5F)
        }

        binding.customviewPiechartSaturday.apply {
            setCalendarDate(weekList[5].calendarDate)
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
            setPercentage(0.5F)
        }
        binding.customviewPiechartSunday.apply {
            setCalendarDate(weekList[6].calendarDate)
            setBorderStrokeWidth(10F)
            setProgressiveStrokeWidth(5F)
            setPercentage(0.5F)
        }

        //TODO implement set date
        binding.customviewPiechartMonday.setOnClickListener {
            sharedViewModel.selectMonday(binding.customviewPiechartMonday.getCalendarDate())
        }
        binding.customviewPiechartTuesday.setOnClickListener {
            sharedViewModel.selectTuesday(binding.customviewPiechartTuesday.getCalendarDate())
        }
        binding.customviewPiechartWednesday.setOnClickListener {
            sharedViewModel.selectWednesday(binding.customviewPiechartWednesday.getCalendarDate())
        }
        binding.customviewPiechartThursday.setOnClickListener {
            sharedViewModel.selectThursday(binding.customviewPiechartThursday.getCalendarDate())
        }

        binding.customviewPiechartFriday.setOnClickListener {
            sharedViewModel.selectFriday(binding.customviewPiechartFriday.getCalendarDate())
        }
        binding.customviewPiechartSaturday.setOnClickListener {
            sharedViewModel.selectSaturday(binding.customviewPiechartSaturday.getCalendarDate())
        }
        binding.customviewPiechartSunday.setOnClickListener {
            sharedViewModel.selectSunday(binding.customviewPiechartSunday.getCalendarDate())
        }
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