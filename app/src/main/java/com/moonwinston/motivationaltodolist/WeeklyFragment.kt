package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.adapters.TaskAdapter
import com.moonwinston.motivationaltodolist.adapters.WeeklyScreenSlidePagerAdapter
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyBinding
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.utilities.CalendarUtil
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class WeeklyFragment : Fragment() {
    private lateinit var binding: FragmentWeeklyBinding
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private lateinit var selectedDate: Date

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectedDate = CalendarUtil.getTodayDate()
        binding = FragmentWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val slideAdapter = WeeklyScreenSlidePagerAdapter(
            this,
            callback = { diffWeek ->
                val diffDays = diffWeek * 7
                val y = SimpleDateFormat("y").format(selectedDate).toInt()
                val m = SimpleDateFormat("M").format(selectedDate).toInt()
                val d = SimpleDateFormat("d").format(selectedDate).toInt()
                val gregorianCalendar = GregorianCalendar(y, m - 1, d)
                gregorianCalendar.add(Calendar.DATE, diffDays)
                sharedViewModel.setSelectedDate(gregorianCalendar.time) })
        binding.viewpagerWeeklyCalendar.adapter = slideAdapter
        binding.viewpagerWeeklyCalendar.setCurrentItem(
            WeeklyScreenSlidePagerAdapter.START_POSITION,
            false
        )
        setToday()

        sharedViewModel.selectedDateLiveData.observe(viewLifecycleOwner) {
            selectedDate = it
            sharedViewModel.getAll()
            binding.textDate.text = getWeeklyTitle(it)
            //TODO day number of week
            when (SimpleDateFormat("u").format(it)) {
                "1" -> {
                    binding.apply {
                        textWeeklyMon.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                        textWeeklyTue.background = null
                        textWeeklyWed.background = null
                        textWeeklyThu.background = null
                        textWeeklyFri.background = null
                        textWeeklySat.background = null
                        textWeeklySun.background = null
                    }
                }
                "2" -> {
                    binding.apply {
                        textWeeklyTue.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                        textWeeklyMon.background = null
                        textWeeklyWed.background = null
                        textWeeklyThu.background = null
                        textWeeklySat.background = null
                        textWeeklySun.background = null
                    }
                }
                "3" -> {
                    binding.apply {
                        textWeeklyWed.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                        textWeeklyTue.background = null
                        textWeeklyMon.background = null
                        textWeeklyThu.background = null
                        textWeeklyFri.background = null
                        textWeeklySat.background = null
                        textWeeklySun.background = null
                    }
                }
                "4" -> {
                    binding.apply {
                        textWeeklyThu.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                        textWeeklyTue.background = null
                        textWeeklyWed.background = null
                        textWeeklyMon.background = null
                        textWeeklyFri.background = null
                        textWeeklySat.background = null
                        textWeeklySun.background = null
                    }
                }
                "5" -> {
                    binding.apply {
                        textWeeklyFri.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                        textWeeklyTue.background = null
                        textWeeklyWed.background = null
                        textWeeklyThu.background = null
                        textWeeklyMon.background = null
                        textWeeklySat.background = null
                        textWeeklySun.background = null
                    }
                }
                "6" -> {
                    binding.apply {
                        textWeeklySat.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                        textWeeklyTue.background = null
                        textWeeklyWed.background = null
                        textWeeklyThu.background = null
                        textWeeklyFri.background = null
                        textWeeklyMon.background = null
                        textWeeklySun.background = null
                    }
                }
                "7" -> {
                    binding.apply {
                        textWeeklySun.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                        textWeeklyTue.background = null
                        textWeeklyWed.background = null
                        textWeeklyThu.background = null
                        textWeeklyFri.background = null
                        textWeeklySat.background = null
                        textWeeklyMon.background = null
                    }
                }
            }
        }

        val adapter = TaskAdapter(
            meatballsMenuCallback = { taskEntity, dmlState ->
                when (dmlState) {
                    DmlState.Update -> {
                        val bundle =
                            bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                        view.findNavController().navigate(R.id.action_weekly_to_add, bundle)
                    }
                    DmlState.Delete -> {
                        sharedViewModel.delete(taskEntity.uid)
                        //TODO
                        sharedViewModel.getAll()
                    }
                    else -> Unit
                }
            },
            radioButtonCallback = {
                sharedViewModel.insert(it)
                //TODO
                sharedViewModel.getAll()
            })
        binding.recyclerviewWeeklyTodo.adapter = adapter

        sharedViewModel.tasksListLiveData.observe(viewLifecycleOwner) {
            //TODO fix
            val selectedDayTasksList = mutableListOf<TaskEntity>()
            for (taskEntity in it) {
                if (taskEntity.taskDate == selectedDate) {
                    selectedDayTasksList.add(taskEntity)
                }
            }
            adapter.submitList(selectedDayTasksList.sortedBy { it.taskTime })
        }

        binding.buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_weekly_to_settings)
        }

        binding.buttonAdd.setOnClickListener {
            val bundle = bundleOf(
                "dmlState" to DmlState.Insert,
                "taskEntity" to TaskEntity(
                    taskDate = selectedDate,
                    taskTime = Date(),
                    task = "",
                    isCompleted = false
                )
            )
            it.findNavController().navigate(R.id.action_weekly_to_add, bundle)
        }
    }

    override fun onResume() {
        super.onResume()
        //TODO
        sharedViewModel.setSelectedDate(CalendarUtil.getTodayDate())
        setToday()
    }

    private fun getWeeklyTitle(selectedDate: Date): String {
        val y = SimpleDateFormat("y").format(selectedDate).toInt()
        val m = SimpleDateFormat("M").format(selectedDate).toInt()
        val d = SimpleDateFormat("d").format(selectedDate).toInt()
        val gregorianCalendar = GregorianCalendar(y, m - 1, d)
        val year = gregorianCalendar.get(Calendar.YEAR)
        val month = gregorianCalendar.get(Calendar.MONTH)
        val date = gregorianCalendar.get(Calendar.DATE)
        val dayOfWeek = gregorianCalendar.get(Calendar.DAY_OF_WEEK)
        val parsedMonth = resources.getString(MonthEnum.values()[month].monthAbbreviation)
        val parsedDayOfWeek = resources.getString(DayOfWeekEnum.values()[dayOfWeek].dayOfWeek)
        return "$parsedDayOfWeek, $parsedMonth $date, $year"
    }

    //TODO
    private fun setToday() {
        binding.textDate.text = getWeeklyTitle(CalendarUtil.getTodayDate())
        when (SimpleDateFormat("u").format(CalendarUtil.getTodayDate())) {
            "1" -> {
                binding.apply {
                    textWeeklyMon.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                    textWeeklyTue.background = null
                    textWeeklyWed.background = null
                    textWeeklyThu.background = null
                    textWeeklyFri.background = null
                    textWeeklySat.background = null
                    textWeeklySun.background = null
                }
            }
            "2" -> {
                binding.apply {
                    textWeeklyTue.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                    textWeeklyMon.background = null
                    textWeeklyWed.background = null
                    textWeeklyThu.background = null
                    textWeeklySat.background = null
                    textWeeklySun.background = null
                }
            }
            "3" -> {
                binding.apply {
                    textWeeklyWed.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                    textWeeklyTue.background = null
                    textWeeklyMon.background = null
                    textWeeklyThu.background = null
                    textWeeklyFri.background = null
                    textWeeklySat.background = null
                    textWeeklySun.background = null
                }
            }
            "4" -> {
                binding.apply {
                    textWeeklyThu.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                    textWeeklyTue.background = null
                    textWeeklyWed.background = null
                    textWeeklyMon.background = null
                    textWeeklyFri.background = null
                    textWeeklySat.background = null
                    textWeeklySun.background = null
                }
            }
            "5" -> {
                binding.apply {
                    textWeeklyFri.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                    textWeeklyTue.background = null
                    textWeeklyWed.background = null
                    textWeeklyThu.background = null
                    textWeeklyMon.background = null
                    textWeeklySat.background = null
                    textWeeklySun.background = null
                }
            }
            "6" -> {
                binding.apply {
                    textWeeklySat.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                    textWeeklyTue.background = null
                    textWeeklyWed.background = null
                    textWeeklyThu.background = null
                    textWeeklyFri.background = null
                    textWeeklyMon.background = null
                    textWeeklySun.background = null
                }
            }
            "7" -> {
                binding.apply {
                    textWeeklySun.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                    textWeeklyTue.background = null
                    textWeeklyWed.background = null
                    textWeeklyThu.background = null
                    textWeeklyFri.background = null
                    textWeeklySat.background = null
                    textWeeklyMon.background = null
                }
            }
        }
    }
}
