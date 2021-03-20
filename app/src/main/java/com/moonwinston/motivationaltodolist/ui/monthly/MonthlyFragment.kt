package com.moonwinston.motivationaltodolist.ui.monthly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.moonwinston.motivationaltodolist.R

class MonthlyFragment : Fragment() {

    private lateinit var monthlyViewModel: MonthlyViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        monthlyViewModel =
                ViewModelProvider(this).get(MonthlyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_monthly, container, false)
        val textView: TextView = root.findViewById(R.id.text_monthly)
        monthlyViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}