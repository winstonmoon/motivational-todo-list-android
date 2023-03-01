package com.moonwinston.motivationaltodolist.ui.rewards

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.databinding.FragmentRewardsBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RewardsFragment: BaseFragment<FragmentRewardsBinding, RewardsViewModel>() {
    override fun getViewBinding() = FragmentRewardsBinding.inflate(layoutInflater)
    override val viewModel: RewardsViewModel by viewModels()
    private val adapter = RewardsAdapter()

    override fun initViews() {
//        val adRequest = AdRequest.Builder().build()
//        binding.adView.loadAd(adRequest)
    }

    override fun initListeners() {
        binding.settingsButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_rewards_to_settings)
        }
    }

    override fun initObservers() {
        binding.rewardsRecyclerView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.completedTask.collect { achievementRateEntities ->
                    adapter.submitList(achievementRateEntities)
                }
            }
        }
    }
}