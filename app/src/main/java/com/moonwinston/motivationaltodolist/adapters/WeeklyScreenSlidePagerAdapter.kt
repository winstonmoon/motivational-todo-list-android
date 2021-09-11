package com.moonwinston.motivationaltodolist.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.WeeklyCalendarFragment
import com.moonwinston.motivationaltodolist.data.TaskEntity

class WeeklyScreenSlidePagerAdapter(fragment: Fragment, val callback: (Int) -> Unit) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): WeeklyCalendarFragment {
        val diffWeek = position - START_POSITION
        callback(diffWeek)
        return WeeklyCalendarFragment.newInstance(diffWeek)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    companion object {
        const val START_POSITION = Int.MAX_VALUE -1
    }
}