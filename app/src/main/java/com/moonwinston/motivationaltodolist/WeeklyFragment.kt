package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.adapters.DailyTaskAdapter
import com.moonwinston.motivationaltodolist.adapters.WeeklyScreenSlidePagerAdapter
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyBinding
import com.moonwinston.motivationaltodolist.adapters.WeeklyTaskAdapter
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.utilities.CalendarUtil
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class WeeklyFragment : Fragment() {

    private lateinit var binding: FragmentWeeklyBinding
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private var selectedDate: Date = CalendarUtil.getToday()

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
        binding.viewpagerWeeklyCalendar.adapter = WeeklyScreenSlidePagerAdapter(this)
        binding.viewpagerWeeklyCalendar.setCurrentItem(
            WeeklyScreenSlidePagerAdapter.START_POSITION,
            false
        )

        sharedViewModel.isMondaySelectedLiveData.observe(viewLifecycleOwner) {
            selectedDate = it
            binding.textDate.text = getWeeklyTitle(it)
            binding.textWeeklyMon.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyTue.background = null
            binding.textWeeklyWed.background = null
            binding.textWeeklyThu.background = null
            binding.textWeeklyFri.background = null
            binding.textWeeklySat.background = null
            binding.textWeeklySun.background = null
        }
        sharedViewModel.isTuesdaySelectedLiveData.observe(viewLifecycleOwner) {
            selectedDate = it
            binding.textDate.text = getWeeklyTitle(it)
            binding.textWeeklyTue.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyMon.background = null
            binding.textWeeklyWed.background = null
            binding.textWeeklyThu.background = null
            binding.textWeeklyFri.background = null
            binding.textWeeklySat.background = null
            binding.textWeeklySun.background = null
        }
        sharedViewModel.isWednesdaySelectedLiveData.observe(viewLifecycleOwner) {
            selectedDate = it
            binding.textDate.text = getWeeklyTitle(it)
            binding.textWeeklyWed.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyTue.background = null
            binding.textWeeklyMon.background = null
            binding.textWeeklyThu.background = null
            binding.textWeeklyFri.background = null
            binding.textWeeklySat.background = null
            binding.textWeeklySun.background = null
        }
        sharedViewModel.isThursdaySelectedLiveData.observe(viewLifecycleOwner) {
            selectedDate = it
            binding.textDate.text = getWeeklyTitle(it)
            binding.textWeeklyThu.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyTue.background = null
            binding.textWeeklyWed.background = null
            binding.textWeeklyMon.background = null
            binding.textWeeklyFri.background = null
            binding.textWeeklySat.background = null
            binding.textWeeklySun.background = null
        }
        sharedViewModel.isFridaySelectedLiveData.observe(viewLifecycleOwner) {
            selectedDate = it
            binding.textDate.text = getWeeklyTitle(it)
            binding.textWeeklyFri.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyTue.background = null
            binding.textWeeklyWed.background = null
            binding.textWeeklyThu.background = null
            binding.textWeeklyMon.background = null
            binding.textWeeklySat.background = null
            binding.textWeeklySun.background = null
        }
        sharedViewModel.isSaturdaySelectedLiveData.observe(viewLifecycleOwner) {
            selectedDate = it
            binding.textDate.text = getWeeklyTitle(it)
            binding.textWeeklySat.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyTue.background = null
            binding.textWeeklyWed.background = null
            binding.textWeeklyThu.background = null
            binding.textWeeklyFri.background = null
            binding.textWeeklyMon.background = null
            binding.textWeeklySun.background = null
        }
        sharedViewModel.isSundaySelectedLiveData.observe(viewLifecycleOwner) {
            selectedDate = it
            binding.textDate.text = getWeeklyTitle(it)
            binding.textWeeklySun.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyTue.background = null
            binding.textWeeklyWed.background = null
            binding.textWeeklyThu.background = null
            binding.textWeeklyFri.background = null
            binding.textWeeklySat.background = null
            binding.textWeeklyMon.background = null
        }

        val adapter = WeeklyTaskAdapter(callback = { taskEntity, dmlState ->
            when (dmlState) {
                DmlState.Update -> {
                    val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                    view.findNavController().navigate(R.id.action_weekly_to_add, bundle)
                }
                DmlState.Delete -> {
                    sharedViewModel.deleteTasks(taskEntity.uid)
                }
                else -> Unit
            }
        })
        binding.recyclerviewWeeklyTodo.adapter = adapter

        sharedViewModel.getTasks(selectedDate)

        sharedViewModel.todayTaskListLiveData.observe(viewLifecycleOwner) {
            //TODO fix
            sharedViewModel.getTasks(selectedDate)
            adapter.submitList(it)
        }

        binding.buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_weekly_to_settings)
        }

        binding.buttonAdd.setOnClickListener {
            val bundle = bundleOf(
                "dmlState" to DmlState.Insert,
                "taskEntity" to TaskEntity(
                    taskDate = Date(),
                    taskTime = Date(),
                    task = "",
                    isGoalSet = false,
                    isCompleted = false
                )
            )
            it.findNavController().navigate(R.id.action_weekly_to_add, bundle)
        }
    }

    private fun getWeeklyTitle(selectedDate: Date): String {
        val year = selectedDate.year
        val month = selectedDate.month
        val parsedMonth = resources.getString(MonthEnum.values()[month].monthAbbreviation)
        val date = selectedDate.date
        val day =
            when (selectedDate.day) {
                1 -> "Monday"
                2 -> "Tuesday"
                3 -> "Wednesday"
                4 -> "Thursday"
                5 -> "Friday"
                6 -> "Saturday"
                else -> "Sunday"
            }
        return "$day, $parsedMonth $date, $year"
    }
}