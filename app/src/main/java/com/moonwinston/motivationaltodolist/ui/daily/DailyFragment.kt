package com.moonwinston.motivationaltodolist.ui.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentDailyBinding
import com.moonwinston.motivationaltodolist.ui.TaskAdapter
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
import com.moonwinston.motivationaltodolist.utils.calculateRate
import com.moonwinston.motivationaltodolist.utils.dateOfToday
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import kotlin.math.roundToInt

@AndroidEntryPoint
class DailyFragment: Fragment() {
    private lateinit var binding: FragmentDailyBinding

    private val viewModel: DailyViewModel by viewModels()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dailyTitleTextView.text = viewModel.createDailyTitle(language = mainViewModel.languageIndex.value)
        binding.dailyTodoRecyclerView.adapter = adapter

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todayTasks.onEach { todayTasks ->
                    adapter.submitList(todayTasks)
                    val calculatedRate = calculateRate(todayTasks)
                    val achievementRate = AchievementRateEntity(date = dateOfToday(), rate = calculatedRate)
                    mainViewModel.insertAchievementRate(achievementRate)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                viewModel.todayAchievementRate.onEach { rate ->
                    val roundedAchievementRate = (rate * 100).roundToInt().toString() + "%"
                    binding.achievementRate.text = roundedAchievementRate
                    binding.dailyCustomPieChart.alpha = if (rate == 0.0F) 0.2F else 1.0F
                    binding.dailyCustomPieChart.updatePercentage(rate)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
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
        })
    }
}