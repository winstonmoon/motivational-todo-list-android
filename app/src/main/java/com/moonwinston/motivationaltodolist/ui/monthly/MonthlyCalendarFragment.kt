package com.moonwinston.motivationaltodolist.ui.monthly

import android.os.Bundle
import android.view.View
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MonthlyCalendarFragment : BaseFragment<MonthlyViewModel, FragmentMonthlyCalendarBinding>() {
    override fun getViewBinding(): FragmentMonthlyCalendarBinding =
        FragmentMonthlyCalendarBinding.inflate(layoutInflater)
    override val viewModel by viewModel<MonthlyViewModel>()
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private var diffMonth: Int = 0

    //TODO fix
    private var year: Int = 0
    private var month: Int = 0
    private var monthList: MutableList<Date> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            diffMonth = it.getInt(DIFF_MONTH)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar: Calendar = Calendar.getInstance()
        calendar.apply {
            add(Calendar.MONTH, diffMonth)
            set(Calendar.DAY_OF_MONTH, 1)
            firstDayOfWeek = Calendar.MONDAY
        }

        //TODO fix dayOfWeek logic more simple, viewmodel
        val maxDate: Int  = calendar.getActualMaximum(Calendar.DATE)
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 2
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        val parsedMonth = resources.getString(MonthEnum.values()[month].monthNumber)
        monthList = MutableList(
            if (dayOfWeek == -1) 6 else dayOfWeek,
            init = { CalendarUtil.getNonExistDate() })
        for (date in 1..maxDate) {
            monthList.add(
                SimpleDateFormat("yyyy-MM-dd").parse("$year-$parsedMonth-$date")
            )
        }

        sharedViewModel.setMonthlyTitle(month, year)

        //TODO fix to use getAll instead of getAllByDates
        sharedViewModel.getAllByDates(monthList)
        sharedViewModel.multipleDaysTasksList.observe(viewLifecycleOwner) {
            val adapter = MonthlyCalendarAdapter(it)
            binding.recyclerviewMonthlyCalendar.adapter = adapter
            adapter.submitList(monthList)
        }
        binding.textMonthlyMonth.setText(MonthEnum.values()[month].monthAbbreviation)
    }

    override fun initViews() = with(binding) {

    }

    override fun observeData() {

    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.setMonthlyTitle(month, year)

        //TODO fix to use getAll instead of getAllByDates
        sharedViewModel.getAllByDates(monthList)
        sharedViewModel.multipleDaysTasksList.observe(viewLifecycleOwner) {
            val adapter = MonthlyCalendarAdapter(it)
            binding.recyclerviewMonthlyCalendar.adapter = adapter
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