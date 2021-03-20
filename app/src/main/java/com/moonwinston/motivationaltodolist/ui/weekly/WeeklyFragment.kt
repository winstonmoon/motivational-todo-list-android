package com.moonwinston.motivationaltodolist.ui.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.moonwinston.motivationaltodolist.R

class WeeklyFragment : Fragment() {

    private lateinit var weeklyViewModel: WeeklyViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        weeklyViewModel =
                ViewModelProvider(this).get(WeeklyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_weekly, container, false)
        val textView: TextView = root.findViewById(R.id.text_weekly)
        weeklyViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}