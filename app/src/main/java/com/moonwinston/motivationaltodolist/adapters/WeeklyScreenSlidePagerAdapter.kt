package com.moonwinston.motivationaltodolist.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.moonwinston.motivationaltodolist.WeeklyCalendarFragment

class WeeklyScreenSlidePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): WeeklyCalendarFragment {
        val diffWeek = position - START_POSITION
        return WeeklyCalendarFragment.newInstance(diffWeek)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }
}