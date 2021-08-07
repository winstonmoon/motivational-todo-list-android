package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.moonwinston.motivationaltodolist.adapters.MonthlyCalendarAdapter
import com.moonwinston.motivationaltodolist.data.CalendarDate
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.viewmodels.MonthlyCalendarViewModel
import java.text.SimpleDateFormat
import java.util.*

class MonthlyCalendarFragment : Fragment() {

    private lateinit var binding: FragmentMonthlyCalendarBinding
    private lateinit var monthlyCalendarViewModel: MonthlyCalendarViewModel
    private var diffMonth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            diffMonth = it.getInt(DIFF_MOTH)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        monthlyCalendarViewModel =
            ViewModelProvider(this).get(MonthlyCalendarViewModel::class.java)
        binding = FragmentMonthlyCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MonthlyCalendarAdapter()
        binding.recyclerviewMonthlyCalendar.adapter = adapter

        val calendar: Calendar = Calendar.getInstance()
        calendar.apply {
            add(Calendar.MONTH, diffMonth)
            set(Calendar.DAY_OF_MONTH, 1)
            firstDayOfWeek = Calendar.MONDAY
        }

        val maxDate: Int = calendar.getActualMaximum(Calendar.DATE)
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 2
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH) + 1
        val dayOfMonthList: MutableList<CalendarDate> = MutableList(if(dayOfWeek == -1) 6 else dayOfWeek, init = { CalendarDate()})
        for (date in 1..maxDate) {
            dayOfMonthList.add(
                CalendarDate(SimpleDateFormat("yyyy-MM-dd").parse("$year-$month-$date"))
            )
        }
        adapter.submitList(dayOfMonthList)
        binding.textMonthlyMonth.text = MonthEnum.values()[month -1].name
    }

    companion object {

        private const val DIFF_MOTH = "diffMonth"

        fun newInstance(diffMoth: Int) = MonthlyCalendarFragment().apply {
            arguments = Bundle().apply {
                putInt(DIFF_MOTH, diffMoth)
            }
        }
    }
}