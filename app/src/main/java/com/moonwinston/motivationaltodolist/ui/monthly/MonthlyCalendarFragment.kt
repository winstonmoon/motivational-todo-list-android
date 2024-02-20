package com.moonwinston.motivationaltodolist.ui.monthly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.moonwinston.motivationaltodolist.databinding.FragmentDailyBinding
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
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
class MonthlyCalendarFragment: Fragment() {

    private lateinit var binding: FragmentMonthlyCalendarBinding

    private val viewModel: MonthlyViewModel by activityViewModels()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonthlyCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.yearAndMonth.onEach { yearAndMonth ->
                    binding.monthTextView.text = Month.of(yearAndMonth.second).getDisplayName(TextStyle.SHORT, Locale.getDefault())
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                viewModel.monthTasks.onEach { taskEntities ->
                    val adapter = MonthlyCalendarAdapter(taskEntities)
                    binding.calendarRecyclerView.adapter = adapter
                    adapter.submitList(daysOfMonth)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                viewModel.setYearAndMonthByLastDayOfMonth(daysOfMonth)
                viewModel.getAllTasksByDates(daysOfMonth)
            }
        })
    }
}