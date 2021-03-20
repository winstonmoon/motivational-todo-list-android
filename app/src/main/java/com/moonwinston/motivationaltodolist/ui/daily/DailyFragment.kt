package com.moonwinston.motivationaltodolist.ui.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.moonwinston.motivationaltodolist.R

class DailyFragment : Fragment() {

    private lateinit var dailyViewModel: DailyViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dailyViewModel =
                ViewModelProvider(this).get(DailyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_daily, container, false)
        val textView: TextView = root.findViewById(R.id.text_daily)
        dailyViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}