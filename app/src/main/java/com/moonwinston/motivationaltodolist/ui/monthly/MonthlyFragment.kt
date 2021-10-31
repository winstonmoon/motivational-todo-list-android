package com.moonwinston.motivationaltodolist.ui.monthly

import android.view.View
import androidx.navigation.findNavController
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class MonthlyFragment : BaseFragment<MonthlyViewModel, FragmentMonthlyBinding>() {
    override fun getViewBinding() = FragmentMonthlyBinding.inflate(layoutInflater)
    override val viewModel by viewModel<MonthlyViewModel>()
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private val sharedPref: SharedPref by inject()

    override fun initViews() = with(binding) {
        viewpagerCalendar.adapter = MonthlyScreenSlidePagerAdapter(this@MonthlyFragment)
        viewpagerCalendar.setCurrentItem(MonthlyScreenSlidePagerAdapter.START_POSITION, false)
        viewpagerCalendar.setPageTransformer(ZoomOutPageTransformer())

        //TODO
        if (sharedPref.isCoachMonthlyDismissed().not()) {
            coachMonthly.containerCoach.visibility = View.VISIBLE
            coachMonthly.containerCoach.setOnClickListener {
                coachMonthly.containerCoach.visibility = View.GONE
                sharedPref.setCoachMonthlyAsDismissed(true)
            }
        }

        buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_monthly_to_settings)
        }
    }

    override fun observeData() {
        sharedViewModel.monthlyTitleLiveData.observe(viewLifecycleOwner) {
            val year = it[0]
            val wordYear = resources.getString(R.string.label_year)
            val parsedMonth = resources.getString(MonthEnum.values()[it[1]].monthAbbreviation)
            binding.textDate.text =
                when (sharedPref.getLanguage()) {
                    ContextUtil.ENGLISH -> "$parsedMonth, $year"
                    else -> "$year$wordYear $parsedMonth"
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