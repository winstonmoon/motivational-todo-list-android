package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonwinston.motivationaltodolist.adapters.DailyTaskAdapter
import com.moonwinston.motivationaltodolist.databinding.FragmentDailyBinding
import com.moonwinston.motivationaltodolist.viewmodels.DailyViewModel
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class DailyFragment : Fragment() {

    private lateinit var binding: FragmentDailyBinding
    private lateinit var dailyViewModel: DailyViewModel
    private val sharedViewModel: SharedViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dailyViewModel =
                ViewModelProvider(this).get(DailyViewModel::class.java)
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        //TODO get percentage from viewmodel and set
        binding.customviewPiechartDaily.setPercentAndBoardWidthAndProgressiveWidth(0.5F, 40F, 20F)
        //TODO edit make percentage
        binding.textGoalPercent.text = "50%"
//        val c = Calendar.getInstance().time
//        sharedViewModel.getTasks(c)
//        binding.textGoalPercent.text =

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(view.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerviewDailyTodo.layoutManager = layoutManager
        binding.recyclerviewDailyTodo.adapter = DailyTaskAdapter()

        val c = Calendar.getInstance().time

//        binding.recyclerviewDailyTodo.adapter.setTask(sharedViewModel.getTasks(c))

        binding.buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_daily_to_settings)
        }

        binding.buttonAdd.setOnClickListener {
            it.findNavController().navigate(R.id.action_daily_to_add)
        }
    }
}