package com.moonwinston.motivationaltodolist.ui.weekly

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class WeeklyScreenSlidePagerAdapter(fragment: Fragment, val callback: (Int) -> Unit) : FragmentStateAdapter(fragment) {
    var test = 0

    override fun createFragment(position: Int): WeeklyCalendarFragment {
        val diffWeek = position - START_POSITION
        test = when {
            position == START_POSITION -> {
                0
            }
            test == START_POSITION -> {
                0
            }
            else -> {
                test - position
            }
        }
//        test = if (test == position) 0 else test - position
//        callback(test)
        callback(diffWeek)
        return WeeklyCalendarFragment.newInstance(diffWeek)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    companion object {
        const val START_POSITION = Int.MAX_VALUE -1
    }
}