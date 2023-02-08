package com.moonwinston.motivationaltodolist.ui.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.ui.adapter.TaskAdapter
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyBinding
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
import com.moonwinston.motivationaltodolist.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class WeeklyFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val weeklySharedViewModel: WeeklyViewModel by activityViewModels()
    private lateinit var binding: FragmentWeeklyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDisplayCoachMark()
        setToday()
        binding.settingsButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_weekly_to_settings)
        }
        binding.addButton.setOnClickListener {
            val bundle = bundleOf(
                "dmlState" to DmlState.Insert(method = "insert"),
                "taskEntity" to TaskEntity(
                    taskDate = weeklySharedViewModel.selectedDate.value,
                    task = "",
                    isCompleted = false
                )
            )
            it.findNavController().navigate(R.id.action_weekly_to_add, bundle)
        }

        val slideAdapter = WeeklyScreenSlidePagerAdapter(this@WeeklyFragment)
        binding.weeklyPieChartViewPager.adapter = slideAdapter
        binding.weeklyPieChartViewPager.setCurrentItem(
            START_POSITION,
            false
        )
        var lastPosition = START_POSITION
        binding.weeklyPieChartViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val diffDays = (position - lastPosition) * 7
//                val selectedDate = weeklySharedViewModel.selectedDate.value.dateToLocalDate()
//                    .plusDays(diffDays.toLong()).localDateToDate()
                val selectedDate = weeklySharedViewModel.selectedDate.value.plusDays(diffDays.toLong())
                weeklySharedViewModel.setSelectedDate(selectedDate)
                lastPosition = position
            }
        })
        val adapter = TaskAdapter(
            meatballsMenuCallback = { taskEntity, dmlState ->
                when (dmlState) {
                    DmlState.Insert(method = "duplicate") -> {
                        val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                        view.findNavController().navigate(R.id.action_weekly_to_add, bundle)
                    }
                    DmlState.Update -> {
                        val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                        view.findNavController().navigate(R.id.action_weekly_to_add, bundle)
                    }
                    DmlState.Delete -> mainViewModel.deleteTask(taskEntity.uid)
                    else -> Unit
                }
                                    },
            radioButtonCallback = { taskEntity ->
                mainViewModel.insertTask(taskEntity)
                binding.congratulationsAnimationView.playAnimation()
            }
        )
        binding.weeklyTodoRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weeklySharedViewModel.selectedDate.collect { selectedDate ->
                    binding.weeklyTitleTextView.text = createWeeklyTitle(
                        date = selectedDate,
                        language = mainViewModel.languageIndex.value
                    )
                    drawRedDotOnSelectedDate(date = selectedDate)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weeklySharedViewModel.selectedDayTasks.collect { selectedDayTasks ->
                    adapter.submitList(selectedDayTasks)
                    val calculatedRate = weeklySharedViewModel.calculateRate(selectedDayTasks)
                    val achievementRate = AchievementRateEntity(
                        date = weeklySharedViewModel.selectedDate.value,
                        rate = calculatedRate
                    )
                    mainViewModel.insertAchievementRate(achievementRate)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setToday()
    }

    private fun setToday() {
        weeklySharedViewModel.setSelectedDate(dateOfToday())
        binding.weeklyTitleTextView.text = createWeeklyTitle(
                date = dateOfToday(),
                language = mainViewModel.languageIndex.value
            )
        drawRedDotOnSelectedDate(date = dateOfToday())
    }

//    private fun createWeeklyTitle(date: Date, language: Int): String {
//        val wordYear = resources.getString(R.string.label_year)
//        val wordDay = resources.getString(R.string.label_day)
//        val today = resources.getString(R.string.text_today)
//        val year = date.dateToLocalDate().year
//        val month = date.dateToLocalDate().month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
//        val day = date.dateToLocalDate().dayOfMonth
//        val dayOfWeek =
//            if (date == dateOfToday()) today
//            else date.dateToLocalDate().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
//        return when (language) {
//            ContextUtil.ENGLISH -> "$dayOfWeek, $month $day, $year"
//            else -> "$year$wordYear $month $day$wordDay $dayOfWeek"
//        }
//    }

    private fun createWeeklyTitle(date: OffsetDateTime, language: Int): String {
        val wordYear = resources.getString(R.string.label_year)
        val wordDay = resources.getString(R.string.label_day)
        val today = resources.getString(R.string.text_today)
        val year = date.year
        val month = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val day = date.dayOfMonth
        val dayOfWeek =
            if (date == dateOfToday()) today
            else date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return when (language) {
            ContextUtil.ENGLISH -> "$dayOfWeek, $month $day, $year"
            else -> "$year$wordYear $month $day$wordDay $dayOfWeek"
        }
    }

//    private fun drawRedDotOnSelectedDate(date: Date) {
//        binding.mondayTextView.background = null
//        binding.tuesdayTextView.background = null
//        binding.wednesdayTextView.background = null
//        binding.thursdayTextView.background = null
//        binding.fridayTextView.background = null
//        binding.saturdayTextView.background = null
//        binding.sundayTextView.background = null
//        when (date.dateToLocalDate().dayOfWeek) {
//            DayOfWeek.MONDAY -> binding.mondayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
//            DayOfWeek.TUESDAY -> binding.tuesdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
//            DayOfWeek.WEDNESDAY -> binding.wednesdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
//            DayOfWeek.THURSDAY -> binding.thursdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
//            DayOfWeek.FRIDAY -> binding.fridayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
//            DayOfWeek.SATURDAY -> binding.saturdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
//            DayOfWeek.SUNDAY -> binding.sundayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
//            else -> Unit
//        }
//    }

    private fun drawRedDotOnSelectedDate(date: OffsetDateTime) {
        binding.mondayTextView.background = null
        binding.tuesdayTextView.background = null
        binding.wednesdayTextView.background = null
        binding.thursdayTextView.background = null
        binding.fridayTextView.background = null
        binding.saturdayTextView.background = null
        binding.sundayTextView.background = null
        when (date.dayOfWeek) {
            DayOfWeek.MONDAY -> binding.mondayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            DayOfWeek.TUESDAY -> binding.tuesdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            DayOfWeek.WEDNESDAY -> binding.wednesdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            DayOfWeek.THURSDAY -> binding.thursdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            DayOfWeek.FRIDAY -> binding.fridayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            DayOfWeek.SATURDAY -> binding.saturdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            DayOfWeek.SUNDAY -> binding.sundayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            else -> Unit
        }
    }

    private fun initDisplayCoachMark() {
        if (weeklySharedViewModel.isCoachWeeklyDismissed.value.not()) {
            this@WeeklyFragment.binding.addButton.isEnabled = false
            binding.coachWeeklySwipe.containerCoach.visibility = View.VISIBLE
            binding.coachWeeklySwipe.containerCoach.setOnClickListener {
                binding.coachWeeklySwipe.containerCoach.visibility = View.GONE
                binding.coachWeeklyTap.containerCoach.visibility = View.VISIBLE
            }
            binding.coachWeeklyTap.containerCoach.setOnClickListener {
                binding.coachWeeklyTap.containerCoach.visibility = View.GONE
                this@WeeklyFragment.binding.addButton.isEnabled = true
                weeklySharedViewModel.setCoachWeeklyAsDismissed(true)
            }
        }
    }
}
