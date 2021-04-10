package com.moonwinston.motivationaltodolist.ui.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.ui.monthly.MonthlyViewModel

class RewardFragment : Fragment() {

    private lateinit var rewardViewModel: RewardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rewardViewModel =
            ViewModelProvider(this).get(RewardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_reward, container, false)
        val textView: TextView = root.findViewById(R.id.text_reward)
        rewardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}