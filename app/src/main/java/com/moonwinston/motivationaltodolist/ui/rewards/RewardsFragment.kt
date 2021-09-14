package com.moonwinston.motivationaltodolist.ui.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyBinding
import com.moonwinston.motivationaltodolist.databinding.FragmentRewardsBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.ui.monthly.MonthlyScreenSlidePagerAdapter
import com.moonwinston.motivationaltodolist.ui.monthly.MonthlyViewModel
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.math.roundToInt

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