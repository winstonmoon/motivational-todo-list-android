package com.moonwinston.motivationaltodolist.ui.daily

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.data.SharedPref
import com.moonwinston.motivationaltodolist.ui.shared.TaskAdapter
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentDailyBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import com.moonwinston.motivationaltodolist.utils.ContextUtil
import dagger.hilt.android.AndroidEntryPoint
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class DailyFragment : BaseFragment<FragmentDailyBinding>() {
    override fun getViewBinding() = FragmentDailyBinding.inflate(layoutInflater)
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val dailyViewModel: DailyViewModel by viewModels()
    private val sharedPref: SharedPref by inject()
    private val adapter by lazy {
        TaskAdapter(
            meatballsMenuCallback = { taskEntity, dmlState ->
                when (dmlState) {
                    DmlState.Insert("copy") -> {
                        val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                        view?.findNavController()?.navigate(R.id.action_daily_to_add, bundle)
                    }
                    DmlState.Update -> {
                        val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                        view?.findNavController()?.navigate(R.id.action_daily_to_add, bundle)
                    }
                    DmlState.Delete -> {
                        sharedViewModel.deleteTask(taskEntity.uid)
                    }
                    else -> Unit
                }
            },
            radioButtonCallback = {
                sharedViewModel.insertTask(it)
                binding.congratulationsAnimationView.playAnimation()
            })
    }
    val bundleForAddDialog = bundleOf(
        "dmlState" to DmlState.Insert("insert"),
        "taskEntity" to TaskEntity(
            taskDate = CalendarUtil.getTodayDate(),
            taskTime = Date(),
            task = "",
            isCompleted = false
        )
    )

    override fun initViews() {
        binding.lifecycleOwner = this@DailyFragment
        binding.dailyFragment = this@DailyFragment
        initDisplayCoachMark()
        binding.dailyTitleTextView.text = setDailyTitleText(sharedPref.getLanguage())
        binding.dailyTodoRecyclerView.adapter = adapter
    }

    override fun observeData() {
        binding.sharedViewModel = sharedViewModel
        sharedViewModel.getAllTasks()
        sharedViewModel.tasksListLiveData.observe(viewLifecycleOwner) { tasksList ->
            val todayTasksList = mutableListOf<TaskEntity>().apply {
                tasksList.forEach { taskEntity ->
                    if (taskEntity.taskDate == CalendarUtil.getTodayDate()) add(taskEntity)
                }
            }
            adapter.submitList(todayTasksList.sortedBy { it.taskTime })
            dailyViewModel.setRate(todayTasksList)
        }

        dailyViewModel.rateLiveData.observe(viewLifecycleOwner) { rate ->
            val achievementRate = AchievementRateEntity(date = CalendarUtil.getTodayDate(), rate = rate)
            sharedViewModel.insertAchievementRate(achievementRate)
            val roundedAchievementRate = (rate * 100).roundToInt()
            binding.achievementRate.text = "$roundedAchievementRate%"
        }
    }

    private fun setDailyTitleText(language: Int): String {
        val cal = Calendar.getInstance()
        val date = cal.get(Calendar.DATE)
        val month = cal.get(Calendar.MONTH)
        val parsedMonth = resources.getString(MonthEnum.values()[month].monthAbbreviation)
        val year = cal.get(Calendar.YEAR)
        val today = resources.getString(R.string.text_today)
        val wordYear = resources.getString(R.string.label_year)
        val wordDay = resources.getString(R.string.label_day)
        return when (language) {
            ContextUtil.ENGLISH -> "$today, $parsedMonth $date, $year"
            else -> "$year$wordYear $parsedMonth $date$wordDay $today"
        }
    }

    private fun initDisplayCoachMark() {
        if (sharedPref.isCoachDailyDismissed().not()) {
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
                sharedPref.setCoachDailyAsDismissed(true)
            }
        }
    }
}