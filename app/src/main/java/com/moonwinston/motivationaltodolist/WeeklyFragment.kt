package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonwinston.motivationaltodolist.adapters.WeeklyCalendarAdapter
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyBinding
import com.moonwinston.motivationaltodolist.adapters.WeeklyTaskAdapter
import com.moonwinston.motivationaltodolist.viewmodels.WeeklyViewModel

class WeeklyFragment : Fragment() {

    private lateinit var binding: FragmentWeeklyBinding
    private lateinit var weeklyViewModel: WeeklyViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        weeklyViewModel =
                ViewModelProvider(this).get(WeeklyViewModel::class.java)
        binding = FragmentWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarLayoutManager = LinearLayoutManager(view.context)
        calendarLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerviewWeeklyCalendar.layoutManager = calendarLayoutManager
        binding.recyclerviewWeeklyCalendar.adapter = WeeklyCalendarAdapter(weeklyViewModel.tasks)

        val taskLayoutManager = LinearLayoutManager(view.context)
        taskLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerviewWeeklyTodo.layoutManager = taskLayoutManager
        binding.recyclerviewWeeklyTodo.adapter = WeeklyTaskAdapter(weeklyViewModel.tasks)
    }
}