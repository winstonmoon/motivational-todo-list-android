package com.moonwinston.motivationaltodolist.ui.monthly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyCalendarBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Month
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.util.*

const val DIFF_MONTH = "diffMonth"

@AndroidEntryPoint
class MonthlyCalendarFragment : Fragment() {
    private val monthlySharedViewModel: MonthlyViewModel by activityViewModels()
    private lateinit var binding: FragmentMonthlyCalendarBinding
//    private var daysOfMonth = listOf<Date>()
    private var daysOfMonth = listOf<OffsetDateTime>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var diffMonth = 0
        arguments?.takeIf { it.containsKey(DIFF_MONTH) }?.apply {
            diffMonth = getInt(DIFF_MONTH)
        }
        daysOfMonth = monthlySharedViewModel.createDaysOfMonth(diffMonth)
        monthlySharedViewModel.setYearAndMonthByLastDayOfMonth(daysOfMonth)
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                monthlySharedViewModel.yearAndMonth.collect { yearAndMonth ->
                    binding.monthTextView.text = Month.of(yearAndMonth.second)
                        .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                }
            }
        }

        monthlySharedViewModel.getAllTasksByDates(daysOfMonth)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                monthlySharedViewModel.monthTasks.collect { taskEntities ->
                    val adapter = MonthlyCalendarAdapter(taskEntities)
                    binding.calendarRecyclerView.adapter = adapter
                    adapter.submitList(daysOfMonth)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        monthlySharedViewModel.setYearAndMonthByLastDayOfMonth(daysOfMonth)
        monthlySharedViewModel.getAllTasksByDates(daysOfMonth)
    }
}