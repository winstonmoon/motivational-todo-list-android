package com.moonwinston.motivationaltodolist.ui.daily

import android.view.View
import androidx.core.os.bundleOf
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
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class DailyFragment : BaseFragment<DailyViewModel, FragmentDailyBinding>() {
    override fun getViewBinding() = FragmentDailyBinding.inflate(layoutInflater)
    override val viewModel by viewModel<DailyViewModel>()
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private lateinit var adapter: TaskAdapter
    private val sharedPref: SharedPref by inject()
    var rate: Float? = null
    var rateString: String? = null
    val bundleForAddDialog = bundleOf(
        "dmlState" to DmlState.Insert("insert"),
        "taskEntity" to TaskEntity(
            taskDate = CalendarUtil.getTodayDate(),
            taskTime = Date(),
            task = "",
            isCompleted = false
        )
    )

    override fun initViews() = with(binding) {
        lifecycleOwner = this@DailyFragment
        dailyFragment = this@DailyFragment
        //TODO
        if (sharedPref.isCoachDailyDismissed().not()) {
            this@DailyFragment.binding.addButton.isEnabled = false
            coachDailyTapAdd.containerCoach.visibility = View.VISIBLE
            coachDailyTapAdd.containerCoach.setOnClickListener {
                coachDailyTapAdd.containerCoach.visibility = View.GONE
                coachDailyTapEditOrDelete.containerCoach.visibility = View.VISIBLE
            }
            coachDailyTapEditOrDelete.containerCoach.setOnClickListener {
                coachDailyTapEditOrDelete.containerCoach.visibility = View.GONE
                coachDailyTapComplete.containerCoach.visibility = View.VISIBLE
            }
            coachDailyTapComplete.containerCoach.setOnClickListener {
                coachDailyTapComplete.containerCoach.visibility = View.GONE
                this@DailyFragment.binding.addButton.isEnabled = true
                sharedPref.setCoachDailyAsDismissed(true)
            }
        }

        //TODO
        adapter = TaskAdapter(
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
        dailyTodoRecyclerView.adapter = adapter

//        addButton.setOnClickListener {
//            val bundle = bundleOf(
//                "dmlState" to DmlState.Insert("insert"),
//                "taskEntity" to TaskEntity(
//                    taskDate = CalendarUtil.getTodayDate(),
//                    taskTime = Date(),
//                    task = "",
//                    isCompleted = false
//                )
//            )
//            it.findNavController().navigate(R.id.action_daily_to_add, bundle)
//        }
    }

    override fun observeData() {
        sharedViewModel.getAllTasks()
        sharedViewModel.tasksListLiveData.observe(viewLifecycleOwner) { it ->
            //TODO fix
            var todayTasksList = mutableListOf<TaskEntity>()
            for (taskEntity in it) {
                if (taskEntity.taskDate == CalendarUtil.getTodayDate()) {
                    todayTasksList.add(taskEntity)
                }
            }
            adapter.submitList(todayTasksList.sortedBy { it.taskTime })
            rate = sharedViewModel.getRate(todayTasksList)
//            when(rate){
//                0.0F -> binding.dailyCustomPieChart.alpha = 0.2F
//                else -> binding.dailyCustomPieChart.alpha = 1.0F
//            }
            binding.dailyCustomPieChart.setPercentage(rate!!)
            rateString = rate.toString()
//            binding.achievementRate.text = "${(rate!! * 100).roundToInt()}%"

            //TODO
            val achievementRate = AchievementRateEntity(date = CalendarUtil.getTodayDate(), rate = rate!!)
            sharedViewModel.insertAchievementRate(achievementRate)
        }
    }


    fun setDailyTitleText(language: Int):String {
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
}