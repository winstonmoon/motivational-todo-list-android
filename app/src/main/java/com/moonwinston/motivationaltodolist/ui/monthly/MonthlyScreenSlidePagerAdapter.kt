package com.moonwinston.motivationaltodolist.ui.monthly

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

const val START_POSITION = Int.MAX_VALUE -1

class MonthlyScreenSlidePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): MonthlyCalendarFragment {
        val diffMonth = position - START_POSITION
        val fragment = MonthlyCalendarFragment()
        fragment.arguments = Bundle().apply {
            putInt(DIFF_MONTH, diffMonth)
        }
        return fragment
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}