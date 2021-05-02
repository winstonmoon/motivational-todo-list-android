package com.moonwinston.motivationaltodolist.ui.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.databinding.FragmentDailyBinding

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