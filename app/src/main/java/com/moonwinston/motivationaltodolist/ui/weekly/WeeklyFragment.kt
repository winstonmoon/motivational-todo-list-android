package com.moonwinston.motivationaltodolist.ui.weekly

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.ui.TaskAdapter
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
import com.moonwinston.motivationaltodolist.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class WeeklyFragment: BaseFragment<FragmentWeeklyBinding, WeeklyViewModel>() {
    override fun getViewBinding() = FragmentWeeklyBinding.inflate(layoutInflater)
    override val viewModel: WeeklyViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private val adapter = TaskAdapter(
        meatballsMenuCallback = { taskEntity, dmlState ->
            when (dmlState) {
                DmlState.Insert(method = "duplicate") -> {
                    val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                    view?.findNavController()?.navigate(R.id.action_weekly_to_add, bundle)
                }
                DmlState.Update -> {
                    val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                    view?.findNavController()?.navigate(R.id.action_weekly_to_add, bundle)
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

    override fun initViews() {
        initDisplayCoachMark()
        setToday()
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
                val selectedDate = viewModel.selectedDate.value.plusDays(diffDays.toLong())
                viewModel.setSelectedDate(selectedDate)
                lastPosition = position
            }
        })
        binding.weeklyTodoRecyclerView.adapter = adapter
    }
    override fun initListeners() {
        binding.settingsButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_weekly_to_settings)
        }
        binding.addButton.setOnClickListener {
            val bundle = bundleOf(
                "dmlState" to DmlState.Insert(method = "insert"),
                "taskEntity" to TaskEntity(
                    taskDate = viewModel.selectedDate.value,
                    task = "",
                    isCompleted = false
                )
            )
            it.findNavController().navigate(R.id.action_weekly_to_add, bundle)
        }
    }
    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedDate.onEach { selectedDate ->
                    binding.weeklyTitleTextView.text = createWeeklyTitle(
                        date = selectedDate,
                        language = mainViewModel.languageIndex.value
                    )
                    drawRedDotOnSelectedDate(date = selectedDate)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                viewModel.selectedDayTasks.onEach { selectedDayTasks ->
                    adapter.submitList(selectedDayTasks)
                    val calculatedRate = calculateRate(selectedDayTasks)
                    val achievementRate = AchievementRateEntity(
                        date = viewModel.selectedDate.value,
                        rate = calculatedRate
                    )
                    mainViewModel.insertAchievementRate(achievementRate)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setToday()
    }

    private fun setToday() {
        viewModel.setSelectedDate(dateOfToday())
        binding.weeklyTitleTextView.text = createWeeklyTitle(
                date = dateOfToday(),
                language = mainViewModel.languageIndex.value
            )
        drawRedDotOnSelectedDate(date = dateOfToday())
    }

    private fun createWeeklyTitle(date: OffsetDateTime, language: Int): String {
        val wordYear = resources.getString(R.string.label_year)
        val wordDay = resources.getString(R.string.label_day)
        val today = resources.getString(R.string.text_today)
        val year = date.year
        val month = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val day = date.dayOfMonth
        val dayOfWeek =
            if (date.isEqual(dateOfToday())) today
            else date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return when (Language.values()[language]) {
            Language.ENGLISH -> "$dayOfWeek, $month $day, $year"
            else -> "$year$wordYear $month $day$wordDay $dayOfWeek"
        }
    }

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
        if (viewModel.isCoachWeeklyDismissed.value.not()) {
            this@WeeklyFragment.binding.addButton.isEnabled = false
            binding.coachWeeklySwipe.containerCoach.visibility = View.VISIBLE
            binding.coachWeeklySwipe.containerCoach.setOnClickListener {
                binding.coachWeeklySwipe.containerCoach.visibility = View.GONE
                binding.coachWeeklyTap.containerCoach.visibility = View.VISIBLE
            }
            binding.coachWeeklyTap.containerCoach.setOnClickListener {
                binding.coachWeeklyTap.containerCoach.visibility = View.GONE
                this@WeeklyFragment.binding.addButton.isEnabled = true
                viewModel.setCoachWeeklyAsDismissed(true)
            }
        }
    }
}
