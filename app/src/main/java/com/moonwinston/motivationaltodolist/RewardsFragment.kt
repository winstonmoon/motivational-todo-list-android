package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.moonwinston.motivationaltodolist.adapters.RewardsAdapter
import com.moonwinston.motivationaltodolist.data.CalendarDate
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentRewardsBinding
import com.moonwinston.motivationaltodolist.viewmodels.RewardsViewModel
import java.text.SimpleDateFormat
import java.util.*

class RewardsFragment : Fragment() {

    private lateinit var binding: FragmentRewardsBinding
    private lateinit var rewardsViewModel: RewardsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rewardsViewModel =
            ViewModelProvider(this).get(RewardsViewModel::class.java)
        binding = FragmentRewardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RewardsAdapter()
        binding.recyclerviewRewards.adapter = adapter
        val testTaskEntity: MutableList<TaskEntity> = mutableListOf<TaskEntity>()
        for (a in 1..9) {
            testTaskEntity.add(
                TaskEntity(taskDate = Date(), taskTime = Date(), task = "", isCompleted = false)
            )
        }
        adapter.submitList(testTaskEntity)



        binding.buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_reward_to_settings)
        }
    }
}