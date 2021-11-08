package com.moonwinston.motivationaltodolist.ui.rewards

import androidx.navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.databinding.FragmentRewardsBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RewardsFragment : BaseFragment<RewardsViewModel, FragmentRewardsBinding>() {
    override fun getViewBinding() = FragmentRewardsBinding.inflate(layoutInflater)
    override val viewModel by viewModel<RewardsViewModel>()
    private val sharedViewModel by sharedViewModel<SharedViewModel>()

    override fun initViews() = with(binding) {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_reward_to_settings)
        }
    }

    override fun observeData() {
        sharedViewModel.getAllComplete()
        sharedViewModel.rateListLiveData.observe(viewLifecycleOwner) {
            val adapter = RewardsAdapter()
            binding.recyclerviewRewards.adapter = adapter
            adapter.submitList(it)
        }
    }
}