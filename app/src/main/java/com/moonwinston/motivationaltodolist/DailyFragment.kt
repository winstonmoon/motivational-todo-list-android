package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonwinston.motivationaltodolist.databinding.FragmentDailyBinding
import com.moonwinston.motivationaltodolist.adapters.DailyTaskAdapter
import com.moonwinston.motivationaltodolist.viewmodels.DailyViewModel

class DailyFragment : Fragment() {

    private lateinit var binding: FragmentDailyBinding
    private lateinit var dailyViewModel: DailyViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dailyViewModel =
                ViewModelProvider(this).get(DailyViewModel::class.java)
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        //TODO get percentage from viewmodel and set
        binding.pieChartGoal.setPercent(0.5F)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(view.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerviewDailyTodo.layoutManager = layoutManager
        binding.recyclerviewDailyTodo.adapter = DailyTaskAdapter(dailyViewModel.tasks)
    }
}