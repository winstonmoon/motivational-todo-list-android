package com.moonwinston.motivationaltodolist.ui.monthly

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.SharedPref
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import com.moonwinston.motivationaltodolist.utils.ContextUtil
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MonthlyFragment : BaseFragment<FragmentMonthlyBinding>() {
    override fun getViewBinding() = FragmentMonthlyBinding.inflate(layoutInflater)
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private val sharedPref: SharedPref by inject()

    override fun initViews() {
        initDisplayCoachMark()
        binding.calendarViewPager.adapter = MonthlyScreenSlidePagerAdapter(this@MonthlyFragment)
        binding.calendarViewPager.setCurrentItem(MonthlyScreenSlidePagerAdapter.START_POSITION, false)
        binding.calendarViewPager.setPageTransformer(ZoomOutPageTransformer())
    }

    override fun observeData() {
        sharedViewModel.monthlyTitleLiveData.observe(viewLifecycleOwner) {
            binding.monthlyTitleTextView.text = setMonthlyTitleText(it.first, it.second, sharedPref.getLanguage())
        }
    }

    private fun setMonthlyTitleText(year: Int, month: Int, language: Int): String {
        val wordYear = resources.getString(R.string.label_year)
        val parsedMonth = resources.getString(MonthEnum.values()[month].monthAbbreviation)
        return when (language) {
            ContextUtil.ENGLISH -> "$parsedMonth, $year"
            else -> "$year$wordYear $parsedMonth"
        }
    }

    private fun initDisplayCoachMark() {
        if (sharedPref.isCoachMonthlyDismissed().not()) {
            binding.coachMonthly.containerCoach.visibility = View.VISIBLE
            binding.coachMonthly.containerCoach.setOnClickListener {
                binding.coachMonthly.containerCoach.visibility = View.GONE
                sharedPref.setCoachMonthlyAsDismissed(true)
            }
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