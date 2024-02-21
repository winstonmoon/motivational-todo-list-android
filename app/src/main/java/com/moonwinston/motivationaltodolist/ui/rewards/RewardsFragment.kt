package com.moonwinston.motivationaltodolist.ui.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.databinding.FragmentRewardsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RewardsFragment: Fragment() {
    private lateinit var binding: FragmentRewardsBinding

    private val viewModel: RewardsViewModel by viewModels()

    private val adapter = RewardsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRewardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rewardsRecyclerView.adapter = adapter

        binding.settingsButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_rewards_to_settings)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.completedTask.onEach { achievementRateEntities ->
                    adapter.submitList(achievementRateEntities)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
    }
}