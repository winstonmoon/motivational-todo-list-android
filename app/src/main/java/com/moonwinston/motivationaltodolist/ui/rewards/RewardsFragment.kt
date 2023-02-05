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
import com.google.android.gms.ads.AdRequest
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.databinding.FragmentRewardsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RewardsFragment : Fragment() {
    private val rewardsViewModel: RewardsViewModel by viewModels()
    private lateinit var binding: FragmentRewardsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRewardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_rewards_to_settings)
        }

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        val adapter = RewardsAdapter()
        binding.rewardsRecyclerView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                rewardsViewModel.completedTask.collect { achievementRateEntities ->
                    adapter.submitList(achievementRateEntities)
                }
            }
        }
    }
}