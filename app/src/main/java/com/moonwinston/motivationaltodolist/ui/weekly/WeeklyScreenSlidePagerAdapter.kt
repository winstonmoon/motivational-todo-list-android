package com.moonwinston.motivationaltodolist.ui.weekly

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class WeeklyScreenSlidePagerAdapter(fragment: Fragment, val callback: (Int) -> Unit) :
    FragmentStateAdapter(fragment) {
    var lastPosition = 0
    var thisPosition = 0

    override fun createFragment(position: Int): WeeklyCalendarFragment {
        lastPosition = if (lastPosition == 0) START_POSITION else thisPosition
        thisPosition = position
        callback(thisPosition - lastPosition)
        val diffWeek = position - START_POSITION
        return WeeklyCalendarFragment.newInstance(diffWeek)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }
}