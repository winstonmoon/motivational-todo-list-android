package com.moonwinston.motivationaltodolist.ui.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdRequest
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.databinding.FragmentRewardsBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardsFragment : Fragment() {

    private lateinit var binding: FragmentRewardsBinding

//    override fun getViewBinding() = FragmentRewardsBinding.inflate(layoutInflater)
    private val rewardsViewModel: RewardsViewModel by viewModels()

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
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        rewardsViewModel.getAllComplete()
        rewardsViewModel.rateListLiveData.observe(viewLifecycleOwner) {
            val adapter = RewardsAdapter()
            binding.rewardsRecyclerView.adapter = adapter
            adapter.submitList(it)
        }
    }

//    override fun initViews() {
//        val adRequest = AdRequest.Builder().build()
//        binding.adView.loadAd(adRequest)
//    }

//    override fun observeData() {
//        rewardsViewModel.getAllComplete()
//        rewardsViewModel.rateListLiveData.observe(viewLifecycleOwner) {
//            val adapter = RewardsAdapter()
//            binding.rewardsRecyclerView.adapter = adapter
//            adapter.submitList(it)
//        }
//    }
}