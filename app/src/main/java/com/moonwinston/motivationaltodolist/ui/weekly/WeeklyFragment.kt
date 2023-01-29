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
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.ui.common.TaskAdapter
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyBinding
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.ui.common.SharedViewModel
import com.moonwinston.motivationaltodolist.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class WeeklyFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val weeklyViewModel: WeeklyViewModel by viewModels()
    private lateinit var binding: FragmentWeeklyBinding
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
        setToday()

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
                val selectedDate = sharedViewModel.selectedDateLiveData.value?.dateToLocalDate()?.plusDays(diffDays.toLong())?.localDateToDate()
                sharedViewModel.setSelectedDate(selectedDate!!)
                lastPosition = position
            }
        })

        binding.addButton.setOnClickListener {
            val bundle = bundleOf(
                "dmlState" to DmlState.Insert(method = "insert"),
                "taskEntity" to sharedViewModel.selectedDateLiveData.value?.let { taskDate ->
                    TaskEntity(
                        taskDate = taskDate,
                        task = "",
                        isCompleted = false
                    )
                }
            )
            it.findNavController().navigate(R.id.action_weekly_to_add, bundle)
        }

        sharedViewModel.selectedDateLiveData.observe(viewLifecycleOwner) { selectedDate ->
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
                if (taskEntity.taskDate.getDateExceptTime() == sharedViewModel.selectedDateLiveData.value) selectedDayTasksList.add(taskEntity)
            }
            binding.weeklyTodoRecyclerView.adapter = adapter
            adapter.submitList(selectedDayTasksList.sortedBy { taskEntity ->
                taskEntity.taskDate })
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
        setToday()
    }

    private fun getWeeklyTitle(selectedDate: Date): String {
        val wordYear = resources.getString(R.string.label_year)
        val wordDay = resources.getString(R.string.label_day)
        val today = resources.getString(R.string.text_today)
        val year = selectedDate.dateToLocalDate().year
        val month = selectedDate.dateToLocalDate().month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val date = selectedDate.dateToLocalDate().dayOfMonth
        val dayOfWeek =
            if (selectedDate == dateOfToday()) today
            else selectedDate.dateToLocalDate().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return when (sharedViewModel.languageIndex.value) {
            ContextUtil.ENGLISH -> "$dayOfWeek, $month $date, $year"
            else -> "$year$wordYear $month $date$wordDay $dayOfWeek"
        }
    }

    private fun setToday() {
        sharedViewModel.setSelectedDate(dateOfToday())
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
