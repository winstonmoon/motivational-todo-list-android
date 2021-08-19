package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.CalendarDate
import com.moonwinston.motivationaltodolist.databinding.ItemMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.utilities.CalendarUtil

class MonthlyCalendarAdapter :
    ListAdapter<CalendarDate, MonthlyCalendarAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemMonthlyCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val nonExistDate = CalendarUtil.getNonExistDate()
        private val today = CalendarUtil.getToday()

        fun bind(calendarDate: CalendarDate) {
            when (calendarDate.calendarDate) {
                nonExistDate -> {
                    binding.textMonthlyDate.visibility = View.GONE
                    binding.customviewPiechartMonthly.visibility = View.GONE
                }
                today -> {
                    binding.textMonthlyDate.setBackgroundResource(R.drawable.bg_shape_oval_red_22)
                    binding.textMonthlyDate.text = calendarDate.calendarDate.date.toString()
                    binding.customviewPiechartMonthly.setPercentAndBoardWidthAndProgressiveWidth(
                        0.5F,
                        10F,
                        5F
                    )
                }
                else -> {
                    binding.textMonthlyDate.text = calendarDate.calendarDate.date.toString()
                    binding.customviewPiechartMonthly.setPercentAndBoardWidthAndProgressiveWidth(
                        0.5F,
                        10F,
                        5F
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMonthlyCalendarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CalendarDate>() {
            override fun areItemsTheSame(oldItem: CalendarDate, newItem: CalendarDate): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CalendarDate, newItem: CalendarDate): Boolean {
                return oldItem == newItem
            }
        }
    }
}