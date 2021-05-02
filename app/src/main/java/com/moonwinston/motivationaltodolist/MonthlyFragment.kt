package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyBinding
import com.moonwinston.motivationaltodolist.adapters.MonthlyScreenSlidePagerAdapter

class MonthlyFragment : Fragment(){

    private lateinit var binding: FragmentMonthlyBinding
    private lateinit var monthlyScreenSlidePagerAdapter: MonthlyScreenSlidePagerAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonthlyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
            view: View,
            savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        monthlyScreenSlidePagerAdapter = MonthlyScreenSlidePagerAdapter(this)

        binding.calendar.adapter = monthlyScreenSlidePagerAdapter
//        binding.calendar.setCurrentItem(monthlyScreenSlidePagerAdapter.START_POSITION, false)
        binding.calendar.setCurrentItem(Int.MAX_VALUE / 2, false)
    }

}