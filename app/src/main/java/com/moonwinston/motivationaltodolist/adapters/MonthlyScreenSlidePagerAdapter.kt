package com.moonwinston.motivationaltodolist.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.moonwinston.motivationaltodolist.MonthlyCalendarFragment

class MonthlyScreenSlidePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): MonthlyCalendarFragment {
        val diffMonth = position - START_POSITION
        return MonthlyCalendarFragment.newInstance(diffMonth)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }
}