package com.moonwinston.motivationaltodolist.ui.daily

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.ui.shared.TaskAdapter
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentDailyBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.math.roundToInt


class DailyFragment : BaseFragment<DailyViewModel, FragmentDailyBinding>() {
    override fun getViewBinding() = FragmentDailyBinding.inflate(layoutInflater)
    override val viewModel by viewModel<DailyViewModel>()
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private lateinit var adapter: TaskAdapter

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        //TODO move to fetchData
//        sharedViewModel.getAll()
//    }

    override fun initViews() = with(binding) {
        customviewPiechartDaily.setBorderStrokeWidth(40F)
        customviewPiechartDaily.setProgressiveStrokeWidth(20F)
        //TODO fix
        val cal = Calendar.getInstance()
        val date = cal.get(Calendar.DATE)
        val month = cal.get(Calendar.MONTH)
        val parsedMonth = resources.getString(MonthEnum.values()[month].monthAbbreviation)
        val year = cal.get(Calendar.YEAR)
        textDate.text = "Today, $parsedMonth $date, $year"

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
                        //TODO
//                        sharedViewModel.getAll()
                    }
                    else -> Unit
                }
            },
            radioButtonCallback = {
                sharedViewModel.insert(it)
                //TODO
//                sharedViewModel.getAll()
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
            binding.customviewPiechartDaily.setPercentage(rate)
            if (rate == 0.0F) {
                binding.customviewPiechartDaily.alpha = 0.2F
            }
            binding.textGoalPercent.text = "${(rate * 100).roundToInt()}%"
        }
    }
}