package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.adapters.WeeklyScreenSlidePagerAdapter
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyBinding
import com.moonwinston.motivationaltodolist.adapters.WeeklyTaskAdapter
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class WeeklyFragment : Fragment() {

    private lateinit var binding: FragmentWeeklyBinding
    private val sharedViewModel by sharedViewModel<SharedViewModel>()

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
        binding.viewpagerWeeklyCalendar.setCurrentItem(WeeklyScreenSlidePagerAdapter.START_POSITION, false)

        sharedViewModel.isMondaySelectedLiveData.observe(viewLifecycleOwner) {
            binding.textWeeklyMon.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyTue.background = null
            binding.textWeeklyWed.background = null
            binding.textWeeklyThu.background = null
            binding.textWeeklyFri.background = null
            binding.textWeeklySat.background = null
            binding.textWeeklySun.background = null
        }
        sharedViewModel.isTuesdaySelectedLiveData.observe(viewLifecycleOwner) {
            binding.textWeeklyTue.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyMon.background = null
            binding.textWeeklyWed.background = null
            binding.textWeeklyThu.background = null
            binding.textWeeklyFri.background = null
            binding.textWeeklySat.background = null
            binding.textWeeklySun.background = null
        }
        sharedViewModel.isWednesdaySelectedLiveData.observe(viewLifecycleOwner) {
            binding.textWeeklyWed.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyTue.background = null
            binding.textWeeklyMon.background = null
            binding.textWeeklyThu.background = null
            binding.textWeeklyFri.background = null
            binding.textWeeklySat.background = null
            binding.textWeeklySun.background = null
        }
        sharedViewModel.isThursdaySelectedLiveData.observe(viewLifecycleOwner) {
            binding.textWeeklyThu.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyTue.background = null
            binding.textWeeklyWed.background = null
            binding.textWeeklyMon.background = null
            binding.textWeeklyFri.background = null
            binding.textWeeklySat.background = null
            binding.textWeeklySun.background = null
        }
        sharedViewModel.isFridaySelectedLiveData.observe(viewLifecycleOwner) {
            binding.textWeeklyFri.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyTue.background = null
            binding.textWeeklyWed.background = null
            binding.textWeeklyThu.background = null
            binding.textWeeklyMon.background = null
            binding.textWeeklySat.background = null
            binding.textWeeklySun.background = null
        }
        sharedViewModel.isSaturdaySelectedLiveData.observe(viewLifecycleOwner) {
            binding.textWeeklySat.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyTue.background = null
            binding.textWeeklyWed.background = null
            binding.textWeeklyThu.background = null
            binding.textWeeklyFri.background = null
            binding.textWeeklyMon.background = null
            binding.textWeeklySun.background = null
        }
        sharedViewModel.isSundaySelectedLiveData.observe(viewLifecycleOwner) {
            binding.textWeeklySun.setBackgroundResource(R.drawable.bg_shape_oval_red_28)
            binding.textWeeklyTue.background = null
            binding.textWeeklyWed.background = null
            binding.textWeeklyThu.background = null
            binding.textWeeklyFri.background = null
            binding.textWeeklySat.background = null
            binding.textWeeklyMon.background = null
        }

        binding.recyclerviewWeeklyTodo.adapter = WeeklyTaskAdapter()

        binding.buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_weekly_to_settings)
        }

        binding.buttonAdd.setOnClickListener {
            it.findNavController().navigate(R.id.action_weekly_to_add)
        }
    }
}