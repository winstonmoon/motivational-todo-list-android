package com.moonwinston.motivationaltodolist.ui.rewards

import androidx.navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.moonwinston.motivationaltodolist.BuildConfig
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentRewardsBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
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
        adView.adUnitId = if (BuildConfig.IS_DEBUG) "ca-app-pub-3940256099942544/6300978111" else "ca-app-pub-2353870408305000~9513759562"
        adView.loadAd(adRequest)

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