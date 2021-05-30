package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.moonwinston.motivationaltodolist.adapters.RewardAdapter
import com.moonwinston.motivationaltodolist.databinding.FragmentRewardsBinding
import com.moonwinston.motivationaltodolist.viewmodels.RewardsViewModel

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
        binding.textDate.text = RewardsViewModel.REWARDS_TITLE

        val gridLayoutManager = GridLayoutManager(view.context, 5)
        binding.recyclerviewRewards.layoutManager = gridLayoutManager
        binding.recyclerviewRewards.adapter = RewardAdapter(rewardsViewModel.tasks)

        binding.buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_reward_to_settings)
        }

        binding.buttonAdd.setOnClickListener {
            it.findNavController().navigate(R.id.action_reward_to_add)
        }
    }
}