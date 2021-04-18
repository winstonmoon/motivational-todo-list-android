package com.moonwinston.motivationaltodolist.ui.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyBinding

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
        val weeklyCalendarRecyclerView: RecyclerView = view.findViewById(R.id.recyclerview_weekly_calendar)
        weeklyCalendarRecyclerView.layoutManager = calendarLayoutManager
        weeklyCalendarRecyclerView.adapter = WeeklyCalendarAdapter(weeklyViewModel.tasks)

        val taskLayoutManager = LinearLayoutManager(view.context)
        taskLayoutManager.orientation = LinearLayoutManager.VERTICAL
        val weeklyTodoRecyclerView: RecyclerView = view.findViewById(R.id.recyclerview_weekly_todo)
        weeklyTodoRecyclerView.layoutManager = taskLayoutManager
        weeklyTodoRecyclerView.adapter = WeeklyTaskAdapter(weeklyViewModel.tasks)
    }
}