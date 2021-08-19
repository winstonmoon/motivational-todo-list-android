package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.moonwinston.motivationaltodolist.adapters.WeeklyCalendarAdapter
import com.moonwinston.motivationaltodolist.data.CalendarDate
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyCalendarBinding
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import org.koin.android.ext.android.bind
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

        val calendar: Calendar = Calendar.getInstance()
        calendar.apply {
            firstDayOfWeek = Calendar.MONDAY
        }

        //TODO fix dayOfWeek logic more simple
        //TODO viewmodel
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val parsedMonth = resources.getString(MonthEnum.values()[month].monthNumber)
        val dayOfWeekList: MutableList<CalendarDate> = mutableListOf<CalendarDate>()

        for (date in 1..7) {
            dayOfWeekList.add(
                CalendarDate(SimpleDateFormat("yyyy-MM-dd").parse("$year-$parsedMonth-$date"))
            )
        }

        binding.customviewPiechartMonday.setPercentAndBoardWidthAndProgressiveWidth(
            0.5F,
            10F,
            5F
        )
        binding.customviewPiechartTuesday.setPercentAndBoardWidthAndProgressiveWidth(
            0.5F,
            10F,
            5F
        )
        binding.customviewPiechartWednesday.setPercentAndBoardWidthAndProgressiveWidth(
            0.5F,
            10F,
            5F
        )
        binding.customviewPiechartWednesday.setPercentAndBoardWidthAndProgressiveWidth(
            0.5F,
            10F,
            5F
        )
        binding.customviewPiechartThursday.setPercentAndBoardWidthAndProgressiveWidth(
            0.5F,
            10F,
            5F
        )
        binding.customviewPiechartFriday.setPercentAndBoardWidthAndProgressiveWidth(
            0.5F,
            10F,
            5F
        )
        binding.customviewPiechartSaturday.setPercentAndBoardWidthAndProgressiveWidth(
            0.5F,
            10F,
            5F
        )
        binding.customviewPiechartSunday.setPercentAndBoardWidthAndProgressiveWidth(
            0.5F,
            10F,
            5F
        )


        binding.customviewPiechartMonday.setOnClickListener {
            sharedViewModel.selectMonday()
        }
        binding.customviewPiechartTuesday.setOnClickListener {
            sharedViewModel.selectTuesday()
        }
        binding.customviewPiechartWednesday.setOnClickListener {
            sharedViewModel.selectWednesday()
        }
        binding.customviewPiechartThursday.setOnClickListener {
            sharedViewModel.selectThursday()
        }

        binding.customviewPiechartFriday.setOnClickListener {
            sharedViewModel.selectFriday()
        }
        binding.customviewPiechartSaturday.setOnClickListener {
            sharedViewModel.selectSaturday()
        }
        binding.customviewPiechartSunday.setOnClickListener {
            sharedViewModel.selectSunday()
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