package com.moonwinston.motivationaltodolist.ui.monthly

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
import com.moonwinston.motivationaltodolist.utils.Language
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Month
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class MonthlyFragment: BaseFragment<FragmentMonthlyBinding, MonthlyViewModel>() {
    override fun getViewBinding() = FragmentMonthlyBinding.inflate(layoutInflater)
    override val viewModel: MonthlyViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun initViews() {
        initDisplayCoachMark()
        binding.calendarViewPager.adapter = MonthlyScreenSlidePagerAdapter(this@MonthlyFragment)
        binding.calendarViewPager.setCurrentItem(START_POSITION, false)
        binding.calendarViewPager.setPageTransformer(ZoomOutPageTransformer())
    }

    override fun initListeners() {
        binding.settingsButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_monthly_to_settings)
        }
    }

    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.yearAndMonth.onEach { yearAndMonth ->
                    binding.monthlyTitleTextView.text = createMonthlyTitle(yearAndMonth.first, yearAndMonth.second)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
    }

    private fun createMonthlyTitle(year: Int, month: Int): String {
        val wordYear = resources.getString(R.string.label_year)
        val month = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.getDefault())
        return when (Language.values()[mainViewModel.languageIndex.value]) {
            Language.ENGLISH -> "$month, $year"
            else -> "$year$wordYear $month"
        }
    }

    private fun initDisplayCoachMark() {
        if (viewModel.isCoachMonthlyDismissed.value.not()) {
            binding.coachMonthly.containerCoach.visibility = View.VISIBLE
            binding.coachMonthly.containerCoach.setOnClickListener {
                binding.coachMonthly.containerCoach.visibility = View.GONE
                viewModel.setCoachMonthlyAsDismissed(true)
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