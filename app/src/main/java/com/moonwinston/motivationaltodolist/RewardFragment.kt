package com.moonwinston.motivationaltodolist.ui.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.databinding.FragmentRewardBinding

class RewardFragment : Fragment() {

    private lateinit var binding: FragmentRewardBinding
    private lateinit var rewardViewModel: RewardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rewardViewModel =
            ViewModelProvider(this).get(RewardViewModel::class.java)
        binding = FragmentRewardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding.rewardView
        val gridLayoutManager = GridLayoutManager(view.context, 3)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = RewardAdapter(rewardViewModel.tasks)
    }
}