package com.moonwinston.motivationaltodolist.ui.monthly

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Month
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.util.*

const val DIFF_MONTH = "diffMonth"

@AndroidEntryPoint
class MonthlyCalendarFragment: BaseFragment<FragmentMonthlyCalendarBinding, MonthlyViewModel>() {
    override fun getViewBinding() = FragmentMonthlyCalendarBinding.inflate(layoutInflater)
    override val viewModel: MonthlyViewModel by activityViewModels()
    private var daysOfMonth = listOf<OffsetDateTime>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var diffMonth = 0
        arguments?.takeIf { it.containsKey(DIFF_MONTH) }?.apply {
            diffMonth = getInt(DIFF_MONTH)
        }
        daysOfMonth = viewModel.createDaysOfMonth(diffMonth)
        viewModel.setYearAndMonthByLastDayOfMonth(daysOfMonth)
    }

    override fun initViews() {
    }
    override fun initListeners() {
    }
    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.yearAndMonth.onEach { yearAndMonth ->
                    binding.monthTextView.text = Month.of(yearAndMonth.second).getDisplayName(TextStyle.SHORT, Locale.getDefault())
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                viewModel.getAllTasksByDates(daysOfMonth)

                viewModel.monthTasks.onEach { taskEntities ->
                    val adapter = MonthlyCalendarAdapter(taskEntities)
                    binding.calendarRecyclerView.adapter = adapter
                    adapter.submitList(daysOfMonth)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setYearAndMonthByLastDayOfMonth(daysOfMonth)
        viewModel.getAllTasksByDates(daysOfMonth)
    }
}