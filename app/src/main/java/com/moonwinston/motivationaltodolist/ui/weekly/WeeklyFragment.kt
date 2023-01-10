package com.moonwinston.motivationaltodolist.ui.weekly

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.moonwinston.motivationaltodolist.DayOfWeekEnum
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.ui.shared.TaskAdapter
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyBinding
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import com.moonwinston.motivationaltodolist.utils.ContextUtil
import java.text.SimpleDateFormat
import java.util.*

class WeeklyFragment : BaseFragment<FragmentWeeklyBinding>() {
    override fun getViewBinding() = FragmentWeeklyBinding.inflate(layoutInflater)
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val weeklyViewModel: WeeklyViewModel by viewModels()
    private lateinit var selectedDate: Date
    private var lastPosition: Int = WeeklyScreenSlidePagerAdapter.START_POSITION

    override fun initViews() {
        initDisplayCoachMark()

        selectedDate = CalendarUtil.getTodayDate()
        val slideAdapter = WeeklyScreenSlidePagerAdapter(this@WeeklyFragment)
        binding.weeklyPieChartViewPager.adapter = slideAdapter
        binding.weeklyPieChartViewPager.setCurrentItem(
            WeeklyScreenSlidePagerAdapter.START_POSITION,
            false
        )
        binding.weeklyPieChartViewPager.registerOnPageChangeCallback(object :
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

        binding.addButton.setOnClickListener {
            val bundle = bundleOf(
                "dmlState" to DmlState.Insert("insert"),
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

    override fun observeData() {
        sharedViewModel.selectedDateLiveData.observe(viewLifecycleOwner) {
            selectedDate = it
            sharedViewModel.getAllTasks()
            binding.weeklyTitleTextView.text = getWeeklyTitle(it)
            binding.mondayTextView.background = null
            binding.tuesdayTextView.background = null
            binding.wednesdayTextView.background = null
            binding.thursdayTextView.background = null
            binding.fridayTextView.background = null
            binding.saturdayTextView.background = null
            binding.sundayTextView.background = null
            val cal = Calendar.getInstance().apply {
                this.time = it
            }

            when (cal.get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY -> binding.mondayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                Calendar.TUESDAY -> binding.tuesdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                Calendar.WEDNESDAY -> binding.wednesdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                Calendar.THURSDAY -> binding.thursdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                Calendar.FRIDAY -> binding.fridayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                Calendar.SATURDAY -> binding.saturdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                Calendar.SUNDAY -> binding.sundayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            }
        }

        sharedViewModel.tasksListLiveData.observe(viewLifecycleOwner) { taskEntities ->
            //TODO fix
            val selectedDayTasksList = mutableListOf<TaskEntity>()
            taskEntities.forEach { taskEntity ->
                if (taskEntity.taskDate == selectedDate) selectedDayTasksList.add(taskEntity)
            }
            val adapter = TaskAdapter(
                meatballsMenuCallback = { taskEntity, dmlState ->
                    when (dmlState) {
                        DmlState.Insert("copy") -> {
                            val bundle =
                                bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                            view?.findNavController()?.navigate(R.id.action_weekly_to_add, bundle)
                        }

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
                    binding.congratulationsAnimationView.playAnimation()
                })
            binding.weeklyTodoRecyclerView.adapter = adapter
            adapter.submitList(selectedDayTasksList.sortedBy { it.taskTime })

            //TODO
            if (selectedDayTasksList.isEmpty().not()) {
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
        val cal = Calendar.getInstance().apply {
            this.time = selectedDate
        }

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val date = cal.get(Calendar.DATE)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        val parsedMonth = resources.getString(MonthEnum.values()[month].monthAbbreviation)
        val today = resources.getString(R.string.text_today)
        val parsedDayOfWeek =
            if (selectedDate == CalendarUtil.getTodayDate()) today
            else resources.getString(DayOfWeekEnum.values()[dayOfWeek].dayOfWeek)
        val wordYear = resources.getString(R.string.label_year)
        val wordDay = resources.getString(R.string.label_day)
        //TODO separate western and eastern
        sharedViewModel.getLanguage()
        return when (sharedViewModel.languageIndex.value) {
            ContextUtil.ENGLISH -> "$parsedDayOfWeek, $parsedMonth $date, $year"
            else -> "$year$wordYear $parsedMonth $date$wordDay $parsedDayOfWeek"
        }
    }

    //TODO
    private fun setToday() {
        binding.weeklyTitleTextView.text = getWeeklyTitle(CalendarUtil.getTodayDate())
        binding.mondayTextView.background = null
        binding.tuesdayTextView.background = null
        binding.wednesdayTextView.background = null
        binding.thursdayTextView.background = null
        binding.fridayTextView.background = null
        binding.saturdayTextView.background = null
        binding.sundayTextView.background = null

        val cal = Calendar.getInstance().apply {
            this.time = CalendarUtil.getTodayDate()
        }
        when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> binding.mondayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            Calendar.TUESDAY -> binding.tuesdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            Calendar.WEDNESDAY -> binding.wednesdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            Calendar.THURSDAY -> binding.thursdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            Calendar.FRIDAY -> binding.fridayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            Calendar.SATURDAY -> binding.saturdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            Calendar.SUNDAY -> binding.sundayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
        }
    }

    private fun initDisplayCoachMark() {
        weeklyViewModel.getCoachWeeklyDismissed()
        if (weeklyViewModel.isCoachWeeklyDismissed.value.not()) {
            this@WeeklyFragment.binding.addButton.isEnabled = false
            binding.coachWeeklySwipe.containerCoach.visibility = View.VISIBLE
            binding.coachWeeklySwipe.containerCoach.setOnClickListener {
                binding.coachWeeklySwipe.containerCoach.visibility = View.GONE
                binding.coachWeeklyTap.containerCoach.visibility = View.VISIBLE
            }
            binding.coachWeeklyTap.containerCoach.setOnClickListener {
                binding.coachWeeklyTap.containerCoach.visibility = View.GONE
                this@WeeklyFragment.binding.addButton.isEnabled = true
                weeklyViewModel.setCoachWeeklyAsDismissed(true)
            }
        }
    }
}
