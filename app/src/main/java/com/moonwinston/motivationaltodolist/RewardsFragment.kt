package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.adapters.RewardsAdapter
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
        binding.recyclerviewRewards.adapter = RewardsAdapter()

        binding.buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_reward_to_settings)
        }
    }
}