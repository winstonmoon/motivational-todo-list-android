package com.moonwinston.motivationaltodolist.ui.monthly

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MonthlyScreenSlidePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): MonthlyCalendarFragment {
        val diffMonth = position - START_POSITION
        return MonthlyCalendarFragment.newInstance(diffMonth)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    companion object {
        //TODO fix
        const val START_POSITION = Int.MAX_VALUE -1
    }
}