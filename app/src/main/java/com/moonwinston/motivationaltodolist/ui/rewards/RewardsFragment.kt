package com.moonwinston.motivationaltodolist.ui.rewards

import com.google.android.gms.ads.AdRequest
import com.moonwinston.motivationaltodolist.databinding.FragmentRewardsBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RewardsFragment : BaseFragment<FragmentRewardsBinding>() {
    override fun getViewBinding() = FragmentRewardsBinding.inflate(layoutInflater)
    private val rewardsViewModel: RewardsViewModel by viewModel()

    override fun initViews() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    override fun observeData() {
        rewardsViewModel.getAllComplete()
        rewardsViewModel.rateListLiveData.observe(viewLifecycleOwner) {
            val adapter = RewardsAdapter()
            binding.rewardsRecyclerView.adapter = adapter
            adapter.submitList(it)
        }
    }
}