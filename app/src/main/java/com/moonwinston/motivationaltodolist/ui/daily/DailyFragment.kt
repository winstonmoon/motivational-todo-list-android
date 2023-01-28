package com.moonwinston.motivationaltodolist.ui.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.ui.common.TaskAdapter
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentDailyBinding
import com.moonwinston.motivationaltodolist.ui.common.SharedViewModel
import com.moonwinston.motivationaltodolist.utils.ContextUtil
import com.moonwinston.motivationaltodolist.utils.dateOfToday
import com.moonwinston.motivationaltodolist.utils.getDateExceptTime
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class DailyFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val dailyViewModel: DailyViewModel by viewModels()
    private lateinit var binding: FragmentDailyBinding
    val bundleForAddDialog = bundleOf(
        "dmlState" to DmlState.Insert(method = "insert"),
        "taskEntity" to TaskEntity(
            taskDate = Date(),
            task = "",
            isCompleted = false
        )
    )
    private val adapter by lazy {
        TaskAdapter(
            meatballsMenuCallback = { taskEntity, dmlState ->
                when (dmlState) {
                    DmlState.Insert(method = "copy") -> {
                        val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                        view?.findNavController()?.navigate(R.id.action_daily_to_add, bundle)
                    }
                    DmlState.Update -> {
                        val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                        view?.findNavController()?.navigate(R.id.action_daily_to_add, bundle)
                    }
                    DmlState.Delete -> sharedViewModel.deleteTask(taskEntity.uid)
                    else -> Unit
                }
            },
            radioButtonCallback = {
                sharedViewModel.insertTask(it)
                binding.congratulationsAnimationView.playAnimation()
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this@DailyFragment
        binding.dailyFragment = this@DailyFragment
        binding.dailyViewModel = dailyViewModel
        binding.sharedViewModel = sharedViewModel
        initDisplayCoachMark()
        binding.dailyTitleTextView.text = setDailyTitleText(sharedViewModel.languageIndex.value)
        binding.dailyTodoRecyclerView.adapter = adapter

        sharedViewModel.getAllTasks()
        sharedViewModel.tasksListLiveData.observe(viewLifecycleOwner) { tasksList ->
            val todayTasksList = mutableListOf<TaskEntity>().apply {
                tasksList.forEach { taskEntity ->
                    if (taskEntity.taskDate.getDateExceptTime() == dateOfToday()) add(taskEntity)
                }
            }
            adapter.submitList(todayTasksList.sortedBy { it.taskDate })
            dailyViewModel.setRate(todayTasksList)
        }

        dailyViewModel.rateLiveData.observe(viewLifecycleOwner) { rate ->
            val achievementRate = AchievementRateEntity(date = dateOfToday(), rate = rate)
            sharedViewModel.insertAchievementRate(achievementRate)
            val roundedAchievementRate = (rate * 100).roundToInt()
            binding.achievementRate.text = "$roundedAchievementRate%"
        }
    }

    private fun setDailyTitleText(language: Int): String {
        val today = resources.getString(R.string.text_today)
        val wordYear = resources.getString(R.string.label_year)
        val wordDay = resources.getString(R.string.label_day)
        val year = LocalDate.now().year
        val month = LocalDate.now().month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val date = LocalDate.now().dayOfMonth
        return when (language) {
            ContextUtil.ENGLISH -> "$today, $month $date, $year"
            else -> "$year$wordYear $month $date$wordDay $today"
        }
    }

    private fun initDisplayCoachMark() {
        if (dailyViewModel.isCoachDailyDismissed.value.not()) {
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
                dailyViewModel.setCoachDailyAsDismissed(true)
            }
        }
    }
}