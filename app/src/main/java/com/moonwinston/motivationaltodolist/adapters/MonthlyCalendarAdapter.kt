package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.CalendarDate
import com.moonwinston.motivationaltodolist.databinding.ItemMonthlyCalendarBinding
import java.text.SimpleDateFormat

class MonthlyCalendarAdapter: ListAdapter<CalendarDate, MonthlyCalendarAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(private val binding: ItemMonthlyCalendarBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(calendarDate: CalendarDate) {
                if (calendarDate.calendarDate == SimpleDateFormat("yyyy-MM-dd").parse("0000-00-00")) {
                binding.textMonthlyDate.visibility = View.GONE
                binding.customviewPiechartMonthly.visibility = View.GONE
            } else {
                binding.textMonthlyDate.text = calendarDate.calendarDate.date.toString()
                binding.customviewPiechartMonthly.setPercentAndBoardWidthAndProgressiveWidth(0.5F, 10F, 5F)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMonthlyCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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