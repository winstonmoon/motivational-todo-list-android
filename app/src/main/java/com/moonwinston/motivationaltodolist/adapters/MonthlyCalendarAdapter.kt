package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.CalendarData
import com.moonwinston.motivationaltodolist.databinding.ItemMonthlyCalendarBinding

class MonthlyCalendarAdapter: ListAdapter<CalendarData, MonthlyCalendarAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(private val binding: ItemMonthlyCalendarBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(calendarData: CalendarData) {
            if (calendarData.dayOfMonth == 0) {
                binding.textMonthlyDate.visibility = View.GONE
                binding.customviewPiechartMonthly.visibility = View.GONE
            } else {
                binding.textMonthlyDate.text = calendarData.dayOfMonth.toString()
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
        val diffUtil = object : DiffUtil.ItemCallback<CalendarData>() {
            override fun areItemsTheSame(oldItem: CalendarData, newItem: CalendarData): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: CalendarData, newItem: CalendarData): Boolean {
                return oldItem == newItem
            }
        }
    }
}