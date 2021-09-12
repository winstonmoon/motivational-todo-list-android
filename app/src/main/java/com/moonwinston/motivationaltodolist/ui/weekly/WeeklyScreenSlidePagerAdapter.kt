package com.moonwinston.motivationaltodolist.ui.weekly

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

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