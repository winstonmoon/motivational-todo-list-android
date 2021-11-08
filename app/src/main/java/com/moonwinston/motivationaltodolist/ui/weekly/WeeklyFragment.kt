package com.moonwinston.motivationaltodolist.ui.weekly

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.moonwinston.motivationaltodolist.DayOfWeekEnum
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.data.SharedPref
import com.moonwinston.motivationaltodolist.ui.shared.TaskAdapter
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyBinding
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import com.moonwinston.motivationaltodolist.utils.ContextUtil
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class WeeklyFragment : BaseFragment<WeeklyViewModel, FragmentWeeklyBinding>() {
    override fun getViewBinding() = FragmentWeeklyBinding.inflate(layoutInflater)
    override val viewModel by viewModel<WeeklyViewModel>()
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private val sharedPref: SharedPref by inject()
    private lateinit var selectedDate: Date
    private var lastPosition: Int = WeeklyScreenSlidePagerAdapter.START_POSITION

    override fun initViews() = with(binding) {
        //TODO
        if (sharedPref.isCoachWeeklyDismissed().not()) {
            this@WeeklyFragment.binding.buttonAdd.isEnabled = false
            coachWeeklySwipe.containerCoach.visibility = View.VISIBLE
            coachWeeklySwipe.containerCoach.setOnClickListener {
                coachWeeklySwipe.containerCoach.visibility = View.GONE
                coachWeeklyTap.containerCoach.visibility = View.VISIBLE
            }
            coachWeeklyTap.containerCoach.setOnClickListener {
                coachWeeklyTap.containerCoach.visibility = View.GONE
                this@WeeklyFragment.binding.buttonAdd.isEnabled = true
                sharedPref.setCoachWeeklyAsDismissed(true)
            }
        }

        selectedDate = CalendarUtil.getTodayDate()
        val slideAdapter = WeeklyScreenSlidePagerAdapter(
            this@WeeklyFragment
        )
        viewpagerWeeklyCalendar.adapter = slideAdapter
        viewpagerWeeklyCalendar.setCurrentItem(
            WeeklyScreenSlidePagerAdapter.START_POSITION,
            false
        )
        viewpagerWeeklyCalendar.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val diffDays = (position - lastPosition) * 7
                val year = SimpleDateFormat("y").format(selectedDate).toInt()
                val month = SimpleDateFormat("M").format(selectedDate).toInt()
                val day = SimpleDateFormat("d").format(selectedDate).toInt()
                val gregorianCalendar = GregorianCalendar(year, month - 1, day)
                gregorianCalendar.add(Calendar.DATE, diffDays)
                sharedViewModel.setSelectedDate(gregorianCalendar.time)
                lastPosition = position
            }
        })
        setToday()

        buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_weekly_to_settings)
        }

        buttonAdd.setOnClickListener {
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun observeData() {
        sharedViewModel.selectedDateLiveData.observe(viewLifecycleOwner) {
            selectedDate = it
            sharedViewModel.getAllTasks()
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

        sharedViewModel.tasksListLiveData.observe(viewLifecycleOwner) {
            //TODO fix
            val selectedDayTasksList = mutableListOf<TaskEntity>()
            for (taskEntity in it) {
                if (taskEntity.taskDate == selectedDate) {
                    selectedDayTasksList.add(taskEntity)
                }
            }
            val adapter = TaskAdapter(
                meatballsMenuCallback = { taskEntity, dmlState ->
                    when (dmlState) {
                        DmlState.Update -> {
                            val bundle =
                                bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                            view?.findNavController()?.navigate(R.id.action_weekly_to_add, bundle)
                        }
                        DmlState.Delete -> {
                            sharedViewModel.deleteTask(taskEntity.uid)
                        }
                        else -> Unit
                    }
                },
                radioButtonCallback = {
                    sharedViewModel.insertTask(it)
                    binding.animationCongratulations.playAnimation()
                })
            binding.recyclerviewWeeklyTodo.adapter = adapter
            adapter.submitList(selectedDayTasksList.sortedBy { it.taskTime })

            //TODO
            if (selectedDayTasksList.isNullOrEmpty().not()) {
                val rate = sharedViewModel.getRate(selectedDayTasksList)
                val date = selectedDayTasksList[0].taskDate
                val achievementRate = AchievementRateEntity(date = date, rate = rate)
                sharedViewModel.insertAchievementRate(achievementRate)
            }
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
        val today = resources.getString(R.string.text_today)
        val parsedDayOfWeek =
            if (selectedDate == CalendarUtil.getTodayDate()) today else resources.getString(
                DayOfWeekEnum.values()[dayOfWeek].dayOfWeek
            )
        val wordYear = resources.getString(R.string.label_year)
        val wordDay = resources.getString(R.string.label_day)
        //TODO separate western and eastern
        return when (sharedPref.getLanguage()) {
            ContextUtil.ENGLISH -> "$parsedDayOfWeek, $parsedMonth $date, $year"
            else -> "$year$wordYear $parsedMonth $date$wordDay $parsedDayOfWeek"
        }
    }

    //TODO
    @RequiresApi(Build.VERSION_CODES.N)
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
