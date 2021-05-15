package com.moonwinston.motivationaltodolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.util.CalendarUtil.Companion.getMonthList
import org.joda.time.DateTime

class MonthlyCalendarFragment : Fragment() {

    private lateinit var binding: FragmentMonthlyCalendarBinding
    private var millis: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            millis = it.getLong(MILLIS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonthlyCalendarBinding.inflate(inflater, container, false)
        binding.textMillis.text = DateTime(millis).toString("yyyy-MM")
        binding.customviewCalendar.initCalendar(DateTime(millis), getMonthList(DateTime(millis)))
        return binding.root
    }

    companion object {

        private const val MILLIS = "MILLIS"

        fun newInstance(millis: Long) = MonthlyCalendarFragment().apply {
            arguments = Bundle().apply {
                putLong(MILLIS, millis)
            }
        }
    }
}