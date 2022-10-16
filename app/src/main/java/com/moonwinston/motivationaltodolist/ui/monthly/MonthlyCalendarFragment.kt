package com.moonwinston.motivationaltodolist.ui.monthly

import android.os.Bundle
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MonthlyCalendarFragment : BaseFragment<FragmentMonthlyCalendarBinding>() {
    override fun getViewBinding() = FragmentMonthlyCalendarBinding.inflate(layoutInflater)
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
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

    override fun initViews() = with(binding) {
        val calendar = Calendar.getInstance()
        calendar.apply {
            add(Calendar.MONTH, diffMonth)
            set(Calendar.DAY_OF_MONTH, 1)
            firstDayOfWeek = Calendar.MONDAY
        }
        //TODO fix dayOfWeek logic more simple, viewmodel
        val maxDate = calendar.getActualMaximum(Calendar.DATE)
        val dayOfWeek =
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1) 6 else calendar.get(Calendar.DAY_OF_WEEK) - 2
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        val parsedMonth = resources.getString(MonthEnum.values()[month].monthNumber)
        monthList = MutableList(
            dayOfWeek,
            init = { CalendarUtil.getNonExistDate() })
        for (date in 1..maxDate) {
            monthList.add(
                SimpleDateFormat("yyyy-MM-dd").parse("$year-$parsedMonth-$date")
            )
        }
        //TODO separate western and eastern
        monthTextView.text = resources.getString(MonthEnum.values()[month].monthAbbreviation)
    }

    override fun observeData() {
        sharedViewModel.setMonthlyTitle(month, year)
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
        sharedViewModel.setMonthlyTitle(month, year)
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