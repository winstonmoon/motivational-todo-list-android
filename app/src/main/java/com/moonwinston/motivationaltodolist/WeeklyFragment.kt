package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.adapters.WeeklyCalendarAdapter
import com.moonwinston.motivationaltodolist.databinding.FragmentWeeklyBinding
import com.moonwinston.motivationaltodolist.adapters.WeeklyTaskAdapter
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import com.moonwinston.motivationaltodolist.viewmodels.WeeklyViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class WeeklyFragment : Fragment() {

    private lateinit var binding: FragmentWeeklyBinding
    private lateinit var weeklyViewModel: WeeklyViewModel
    private val sharedViewModel by sharedViewModel<SharedViewModel>()

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
        binding.viewpagerWeeklyCalendar.adapter = WeeklyCalendarAdapter()
        binding.recyclerviewWeeklyTodo.adapter = WeeklyTaskAdapter()

        binding.buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_weekly_to_settings)
        }

        binding.buttonAdd.setOnClickListener {
            it.findNavController().navigate(R.id.action_weekly_to_add)
        }
    }
}