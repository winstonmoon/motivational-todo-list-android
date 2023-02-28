package com.moonwinston.motivationaltodolist.ui.daily

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.ui.TaskAdapter
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentDailyBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
import com.moonwinston.motivationaltodolist.utils.Language
import com.moonwinston.motivationaltodolist.utils.calculateRate
import com.moonwinston.motivationaltodolist.utils.dateOfToday
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class DailyFragment: BaseFragment<FragmentDailyBinding, DailyViewModel>() {
    override fun getViewBinding() = FragmentDailyBinding.inflate(layoutInflater)
    override val viewModel: DailyViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private val adapter = TaskAdapter(
        meatballsMenuCallback = { taskEntity, dmlState ->
            when (dmlState) {
                DmlState.Insert(method = "duplicate") -> {
                    val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                    view?.findNavController()?.navigate(R.id.action_daily_to_add, bundle)
                }
                DmlState.Update -> {
                    val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                    view?.findNavController()?.navigate(R.id.action_daily_to_add, bundle)
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
        binding.dailyTitleTextView.text = createDailyTitle(language = mainViewModel.languageIndex.value)
        binding.dailyTodoRecyclerView.adapter = adapter
    }

    override fun initListeners() {
        binding.settingsButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_daily_to_settings)
        }
        binding.addButton.setOnClickListener {
            val bundle = bundleOf(
                "dmlState" to DmlState.Insert(method = "insert"),
                "taskEntity" to TaskEntity(
                    taskDate = OffsetDateTime.now(),
                    task = "",
                    isCompleted = false
                )
            )
            it.findNavController().navigate(R.id.action_daily_to_add, bundle)
        }
    }

    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todayTasks.collect { todayTasks ->
                    adapter.submitList(todayTasks)
                    val calculatedRate = calculateRate(todayTasks)
                    val achievementRate = AchievementRateEntity(date = dateOfToday(), rate = calculatedRate)
                    mainViewModel.insertAchievementRate(achievementRate)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todayAchievementRate.collect { rate ->
                    val roundedAchievementRate = (rate * 100).roundToInt().toString() + "%"
                    binding.achievementRate.text = roundedAchievementRate
                    binding.dailyCustomPieChart.alpha = if (rate == 0.0F) 0.2F else 1.0F
                    binding.dailyCustomPieChart.updatePercentage(rate)
                }
            }
        }
    }

    private fun createDailyTitle(language: Int): String {
        val today = resources.getString(R.string.text_today)
        val wordYear = resources.getString(R.string.label_year)
        val wordDay = resources.getString(R.string.label_day)
        val year = LocalDate.now().year
        val month = LocalDate.now().month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val day = LocalDate.now().dayOfMonth
        return when (Language.values()[language]) {
            Language.ENGLISH -> "$today, $month $day, $year"
            else -> "$year$wordYear $month $day$wordDay $today"
        }
    }

    private fun initDisplayCoachMark() {
        if (viewModel.isCoachDailyDismissed.value.not()) {
            this@DailyFragment.binding.addButton.isEnabled = false
            binding.coachDailyTapAdd.containerCoach.visibility = View.VISIBLE
            binding.coachDailyTapAdd.containerCoach.setOnClickListener {
                binding.coachDailyTapAdd.containerCoach.visibility = View.GONE
                binding.coachDailyTapEditOrDelete.containerCoach.visibility = View.VISIBLE
            }
            binding.coachDailyTapEditOrDelete.containerCoach.setOnClickListener {
                binding.coachDailyTapEditOrDelete.containerCoach.visibility = View.GONE
                binding.coachDailyTapComplete.containerCoach.visibility = View.VISIBLE
            }
            binding.coachDailyTapComplete.containerCoach.setOnClickListener {
                binding.coachDailyTapComplete.containerCoach.visibility = View.GONE
                this@DailyFragment.binding.addButton.isEnabled = true
                viewModel.setCoachDailyAsDismissed(true)
            }
        }
    }
}