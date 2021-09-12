package com.moonwinston.motivationaltodolist.ui.monthly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentDailyBinding
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.ui.daily.DailyViewModel
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class MonthlyFragment : BaseFragment<MonthlyViewModel, FragmentMonthlyBinding>() {
    override fun getViewBinding(): FragmentMonthlyBinding =
        FragmentMonthlyBinding.inflate(layoutInflater)
    override val viewModel by viewModel<MonthlyViewModel>()
    private val sharedViewModel by sharedViewModel<SharedViewModel>()

    override fun initViews() = with(binding) {
        viewpagerCalendar.adapter = MonthlyScreenSlidePagerAdapter(this@MonthlyFragment)
        viewpagerCalendar.setCurrentItem(MonthlyScreenSlidePagerAdapter.START_POSITION, false)
        viewpagerCalendar.setPageTransformer(ZoomOutPageTransformer())
        buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_monthly_to_settings)
        }
    }

    override fun observeData() {
        sharedViewModel.monthlyTitleLiveData.observe(viewLifecycleOwner) {
            binding.textDate.text = it
        }
    }

    //TODO fix
    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {

        private val MIN_SCALE = 0.85f
        private val MIN_ALPHA = 0.5f

        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}