package com.moonwinston.motivationaltodolist.ui.monthly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import com.moonwinston.motivationaltodolist.utils.nonExistDate
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.Month
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class MonthlyCalendarFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentMonthlyCalendarBinding
    private var diffMonth = 0
    //TODO fix
    private var year = 0
    private var month = 0
    private var monthList = mutableListOf<Date>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            diffMonth = it.getInt(DIFF_MONTH)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonthlyCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar = Calendar.getInstance()
        calendar.apply {
            add(Calendar.MONTH, diffMonth)
            set(Calendar.DAY_OF_MONTH, 1)
            firstDayOfWeek = Calendar.MONDAY
        }
        //TODO fix dayOfWeek logic more simple, viewmodel
        val dayOfWeek =
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1) 6
            else calendar.get(Calendar.DAY_OF_WEEK) - 2
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        val parsedMonth = resources.getString(MonthEnum.values()[month].monthNumber)
        monthList = MutableList(
            dayOfWeek,
            init = { nonExistDate() })
        val endOfMonth = calendar.getActualMaximum(Calendar.DATE)
        (1..endOfMonth).forEach { date ->
            monthList.add(SimpleDateFormat("yyyy-MM-dd").parse("$year-$parsedMonth-$date"))
        }
        //TODO separate western and eastern
//        binding.monthTextView.text = resources.getString(MonthEnum.values()[month].monthAbbreviation)
        binding.monthTextView.text = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.getDefault())

        sharedViewModel.setMonthlyTitle(year, month)
        //TODO fix to use getAll instead of getAllByDates
        sharedViewModel.getAllTasksByDates(monthList)
        sharedViewModel.multipleDaysTasksList.observe(viewLifecycleOwner) {
            val adapter = MonthlyCalendarAdapter(it)
            binding.calendarRecyclerView.adapter = adapter
            adapter.submitList(monthList)
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setMonthlyTitle(year, month)
        //TODO fix to use getAll instead of getAllByDates
        sharedViewModel.getAllTasksByDates(monthList)
        sharedViewModel.multipleDaysTasksList.observe(viewLifecycleOwner) {
            val adapter = MonthlyCalendarAdapter(it)
            binding.calendarRecyclerView.adapter = adapter
            adapter.submitList(monthList)
        }
    }

    companion object {
        private const val DIFF_MONTH = "diffMonth"
        fun newInstance(diffMonth: Int) = MonthlyCalendarFragment().apply {
            arguments = Bundle().apply {
                putInt(DIFF_MONTH, diffMonth)
            }
        }
    }
}