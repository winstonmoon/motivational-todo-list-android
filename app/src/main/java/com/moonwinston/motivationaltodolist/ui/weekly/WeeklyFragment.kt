package com.moonwinston.motivationaltodolist.ui.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
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
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import com.moonwinston.motivationaltodolist.utils.ContextUtil
import com.moonwinston.motivationaltodolist.utils.dateOfToday
import com.moonwinston.motivationaltodolist.utils.dateToLocalDate
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*

@AndroidEntryPoint
class WeeklyFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val weeklyViewModel: WeeklyViewModel by viewModels()
    private lateinit var binding: FragmentWeeklyBinding
    private lateinit var selectedDate: Date
    private var lastPosition: Int = WeeklyScreenSlidePagerAdapter.START_POSITION
    private val adapter by lazy {
        TaskAdapter(
            meatballsMenuCallback = { taskEntity, dmlState ->
                when (dmlState) {
                    DmlState.Insert(method = "copy") -> {
                        val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                        view?.findNavController()?.navigate(R.id.action_weekly_to_add, bundle)
                    }
                    DmlState.Update -> {
                        val bundle = bundleOf("dmlState" to dmlState, "taskEntity" to taskEntity)
                        view?.findNavController()?.navigate(R.id.action_weekly_to_add, bundle)
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
        binding = FragmentWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this@WeeklyFragment
        binding.weeklyFragment = this@WeeklyFragment
        initDisplayCoachMark()

        selectedDate = dateOfToday()
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
                "dmlState" to DmlState.Insert(method = "insert"),
                "taskEntity" to TaskEntity(
                    taskDate = selectedDate,
                    taskTime = Date(),
                    task = "",
                    isCompleted = false
                )
            )
            it.findNavController().navigate(R.id.action_weekly_to_add, bundle)
        }

        sharedViewModel.selectedDateLiveData.observe(viewLifecycleOwner) { selectedDate ->
            this.selectedDate = selectedDate
            sharedViewModel.getAllTasks()
            binding.weeklyTitleTextView.text = getWeeklyTitle(selectedDate)
            binding.mondayTextView.background = null
            binding.tuesdayTextView.background = null
            binding.wednesdayTextView.background = null
            binding.thursdayTextView.background = null
            binding.fridayTextView.background = null
            binding.saturdayTextView.background = null
            binding.sundayTextView.background = null

            when (selectedDate.dateToLocalDate().dayOfWeek) {
                DayOfWeek.MONDAY -> binding.mondayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                DayOfWeek.TUESDAY -> binding.tuesdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                DayOfWeek.WEDNESDAY -> binding.wednesdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                DayOfWeek.THURSDAY -> binding.thursdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                DayOfWeek.FRIDAY -> binding.fridayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                DayOfWeek.SATURDAY -> binding.saturdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                DayOfWeek.SUNDAY -> binding.sundayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
                else -> Unit
            }
        }

        sharedViewModel.tasksListLiveData.observe(viewLifecycleOwner) { taskEntities ->
            //TODO fix
            val selectedDayTasksList = mutableListOf<TaskEntity>()
            taskEntities.forEach { taskEntity ->
                if (taskEntity.taskDate == selectedDate) selectedDayTasksList.add(taskEntity)
            }
            binding.weeklyTodoRecyclerView.adapter = adapter
            adapter.submitList(selectedDayTasksList.sortedBy { taskEntity ->
                taskEntity.taskTime })

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
        sharedViewModel.setSelectedDate(dateOfToday())
        setToday()
    }

    private fun getWeeklyTitle(selectedDate: Date): String {
        val wordYear = resources.getString(R.string.label_year)
        val wordDay = resources.getString(R.string.label_day)
        val today = resources.getString(R.string.text_today)

        val cal = Calendar.getInstance().apply {
            time = selectedDate
        }
        val month = cal.get(Calendar.MONTH)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        val year = selectedDate.dateToLocalDate().year
        val monthTest = selectedDate.dateToLocalDate().month.value
        val date = selectedDate.dateToLocalDate().dayOfMonth
        val dayOfWeekTest = selectedDate.dateToLocalDate().dayOfWeek

        val parsedMonth = resources.getString(MonthEnum.values()[month].monthAbbreviation)
        val parsedDayOfWeek =
            if (selectedDate == dateOfToday()) today
            else resources.getString(DayOfWeekEnum.values()[dayOfWeek].dayOfWeek)

        //TODO separate western and eastern
        return when (sharedViewModel.languageIndex.value) {
            ContextUtil.ENGLISH -> "$parsedDayOfWeek, $parsedMonth $date, $year"
            else -> "$year$wordYear $parsedMonth $date$wordDay $parsedDayOfWeek"
        }
    }

    //TODO databinding
    private fun setToday() {
        binding.weeklyTitleTextView.text = getWeeklyTitle(dateOfToday())
        binding.mondayTextView.background = null
        binding.tuesdayTextView.background = null
        binding.wednesdayTextView.background = null
        binding.thursdayTextView.background = null
        binding.fridayTextView.background = null
        binding.saturdayTextView.background = null
        binding.sundayTextView.background = null

        when (dateOfToday().dateToLocalDate().dayOfWeek) {
            DayOfWeek.MONDAY -> binding.mondayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            DayOfWeek.TUESDAY -> binding.tuesdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            DayOfWeek.WEDNESDAY -> binding.wednesdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            DayOfWeek.THURSDAY -> binding.thursdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            DayOfWeek.FRIDAY -> binding.fridayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            DayOfWeek.SATURDAY -> binding.saturdayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            DayOfWeek.SUNDAY -> binding.sundayTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            else -> Unit
        }
    }

    private fun initDisplayCoachMark() {
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
