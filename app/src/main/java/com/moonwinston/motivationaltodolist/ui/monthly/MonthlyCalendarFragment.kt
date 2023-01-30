package com.moonwinston.motivationaltodolist.ui.monthly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
import com.moonwinston.motivationaltodolist.utils.localDateToDate
import com.moonwinston.motivationaltodolist.utils.nonExistDate
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class MonthlyCalendarFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val monthlySharedViewModel: MonthlyViewModel by activityViewModels()
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
        //TODO use dayOfWeek and endOfMonth calendar
        val dayOfWeek =
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1) 6
            else calendar.get(Calendar.DAY_OF_WEEK) - 2
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        monthList = MutableList(
            dayOfWeek,
            init = { nonExistDate() })
        val endOfMonth = calendar.getActualMaximum(Calendar.DATE)
        (1..endOfMonth).forEach { date ->
            monthList.add(LocalDate.of(year, month + 1, date).localDateToDate())
        }
        binding.monthTextView.text = Month.of(month + 1).getDisplayName(TextStyle.SHORT, Locale.getDefault())

        monthlySharedViewModel.setMonthlyTitle(year, month + 1)
        //TODO fix to use getAll instead of getAllByDates
        mainViewModel.getAllTasksByDates(monthList)
        mainViewModel.multipleDaysTasksList.observe(viewLifecycleOwner) { taskEntities ->
            val adapter = MonthlyCalendarAdapter(taskEntities)
            binding.calendarRecyclerView.adapter = adapter
            adapter.submitList(monthList)
        }
    }

    override fun onResume() {
        super.onResume()
        monthlySharedViewModel.setMonthlyTitle(year, month + 1)
        //TODO fix to use getAll instead of getAllByDates
        mainViewModel.getAllTasksByDates(monthList)
        mainViewModel.multipleDaysTasksList.observe(viewLifecycleOwner) { taskEntities ->
            val adapter = MonthlyCalendarAdapter(taskEntities)
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