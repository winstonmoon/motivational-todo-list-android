package com.moonwinston.motivationaltodolist.ui.rewards

import androidx.navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.moonwinston.motivationaltodolist.BuildConfig
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentRewardsBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class RewardsFragment : BaseFragment<RewardsViewModel, FragmentRewardsBinding>() {
    override fun getViewBinding() = FragmentRewardsBinding.inflate(layoutInflater)
    override val viewModel by viewModel<RewardsViewModel>()
    private val adapter = RewardsAdapter()

    override fun initViews() = with(binding) {
        recyclerviewRewards.adapter = adapter

        //TODO test
        val testTaskEntity: MutableList<TaskEntity> = mutableListOf<TaskEntity>()
        for (a in 1..9) {
            testTaskEntity.add(
                TaskEntity(taskDate = Date(), taskTime = Date(), task = "", isCompleted = false)
            )
        }
        adapter.submitList(testTaskEntity)

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_reward_to_settings)
        }
    }

    override fun observeData() {
//        var taskDateList = mutableListOf<Date>()
//        sharedViewModel.getAll()
//        sharedViewModel.tasksListLiveData.observe(viewLifecycleOwner) {
//            for (task in it) {
//                if (sharedViewModel.getRate(it) == 100F) {
//                    taskDateList.add(task.taskDate)
//                }
//
//            }
//        }
    }
}