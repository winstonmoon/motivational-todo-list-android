package com.moonwinston.motivationaltodolist.ui.weekly

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class WeeklyScreenSlidePagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): WeeklyCalendarFragment {
        val diffWeek = position - START_POSITION
        val fragment = WeeklyCalendarFragment()
        fragment.arguments = Bundle().apply {
            putInt(DIFF_WEEK, diffWeek)
        }
        return fragment
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }
}