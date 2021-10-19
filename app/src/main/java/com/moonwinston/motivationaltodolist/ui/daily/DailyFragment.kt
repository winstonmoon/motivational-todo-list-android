package com.moonwinston.motivationaltodolist.ui.daily

import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.SharedPref
import com.moonwinston.motivationaltodolist.ui.shared.TaskAdapter
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentDailyBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.math.roundToInt


class DailyFragment : BaseFragment<DailyViewModel, FragmentDailyBinding>() {
    override fun getViewBinding() = FragmentDailyBinding.inflate(layoutInflater)
    override val viewModel by viewModel<DailyViewModel>()
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private lateinit var adapter: TaskAdapter
    private val sharedPref: SharedPref by inject()
    companion object { private const val ENGLISH = 1 }

    override fun initViews() = with(binding) {
        //TODO fix
        val cal = Calendar.getInstance()
        val date = cal.get(Calendar.DATE)
        val month = cal.get(Calendar.MONTH)
        val parsedMonth = resources.getString(MonthEnum.values()[month].monthAbbreviation)
        val year = cal.get(Calendar.YEAR)
        val today = resources.getString(R.string.text_today)
        val wordYear = resources.getString(R.string.label_year)
        val wordDay = resources.getString(R.string.label_day)
        //TODO
        textDate.text =
        when (sharedPref.getLanguage()) {
            ENGLISH -> "$today, $parsedMonth $date, $year"
            else -> "$year$wordYear $parsedMonth $date$wordDay $today"
        }

        //TODO
        adapter = TaskAdapter(
            meatballsMenuCallback = { taskEntity, dmlState ->
                when (dmlState) {
                    DmlState.Update -> {
                        val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                        view?.findNavController()?.navigate(R.id.action_daily_to_add, bundle)
                    }
                    DmlState.Delete -> {
                        sharedViewModel.delete(taskEntity.uid)
                    }
                    else -> Unit
                }
            },
            radioButtonCallback = {
                sharedViewModel.insert(it)
                binding.animationCongratulations.playAnimation()
            })
        recyclerviewDailyTodo.adapter = adapter
        buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_daily_to_settings)
        }
        buttonAdd.setOnClickListener {
            val bundle = bundleOf(
                "dmlState" to DmlState.Insert,
                "taskEntity" to TaskEntity(
                    taskDate = CalendarUtil.getTodayDate(),
                    taskTime = Date(),
                    task = "",
                    isCompleted = false
                )
            )
            it.findNavController().navigate(R.id.action_daily_to_add, bundle)
        }
    }

    override fun observeData() {
        sharedViewModel.getAll()
        sharedViewModel.tasksListLiveData.observe(viewLifecycleOwner) {
            //TODO fix
            var todayTasksList = mutableListOf<TaskEntity>()
            for (taskEntity in it) {
                if (taskEntity.taskDate == CalendarUtil.getTodayDate()) {
                    todayTasksList.add(taskEntity)
                }
            }
            adapter.submitList(todayTasksList.sortedBy { it.taskTime })
            var rate = sharedViewModel.getRate(todayTasksList)
            when(rate){
                0.0F -> binding.customviewPiechartDaily.alpha = 0.2F
                else -> binding.customviewPiechartDaily.alpha = 1.0F
            }
            binding.customviewPiechartDaily.setPercentage(rate)
            binding.textGoalPercent.text = "${(rate * 100).roundToInt()}%"
        }
    }
}