package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.adapters.TaskAdapter
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentDailyBinding
import com.moonwinston.motivationaltodolist.utilities.CalendarUtil
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*


class DailyFragment : Fragment() {

    private lateinit var binding: FragmentDailyBinding
    private val sharedViewModel by sharedViewModel<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        //TODO get percentage from viewmodel and set
        binding.customviewPiechartDaily.setPercentAndBoardWidthAndProgressiveWidth(0.5F, 40F, 20F)
        //TODO edit make percentage
        binding.textGoalPercent.text = "50%"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TaskAdapter(meatballsmenuCallback = { taskEntity, dmlState ->
            when (dmlState) {
                DmlState.Update -> {
                    val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                    view.findNavController().navigate(R.id.action_daily_to_add, bundle)
                }
                DmlState.Delete -> {
                    sharedViewModel.deleteTasks(taskEntity.uid)
                }
                else -> Unit
            }
        },
            radioButtonCalllback = {
                sharedViewModel.insert(it)
            })
        binding.recyclerviewDailyTodo.adapter = adapter

        //TODO fix
        val date = Calendar.getInstance().get(Calendar.DATE)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val parsedMonth = resources.getString(MonthEnum.values()[month].monthAbbreviation)
        val year = Calendar.getInstance().get(Calendar.YEAR)
        binding.textDate.text = "Today, $parsedMonth $date, $year"

        sharedViewModel.getTasks(CalendarUtil.getToday())

        sharedViewModel.todayTaskListLiveData.observe(viewLifecycleOwner) {
            //TODO fix
            sharedViewModel.getTasks(CalendarUtil.getToday())
            adapter.submitList(it)
        }

        binding.buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_daily_to_settings)
        }

        binding.buttonAdd.setOnClickListener {
            val bundle = bundleOf(
                "dmlState" to DmlState.Insert,
                "taskEntity" to TaskEntity(
                    taskDate = Date(),
                    taskTime = Date(),
                    task = "",
                    isCompleted = false
                )
            )
            it.findNavController().navigate(R.id.action_daily_to_add, bundle)
        }
    }
}