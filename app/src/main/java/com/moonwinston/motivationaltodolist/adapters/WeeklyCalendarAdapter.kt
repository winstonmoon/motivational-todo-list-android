package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.CalendarDate
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.ItemWeeklyCalendarBinding
import com.moonwinston.motivationaltodolist.utilities.CalendarUtil

class WeeklyCalendarAdapter :
    ListAdapter<CalendarDate, WeeklyCalendarAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemWeeklyCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val nonExistDate = CalendarUtil.getNonExistDate()

        fun bind(calendarDate: CalendarDate) {
            when (calendarDate.calendarDate) {
                nonExistDate -> {
                }
                else -> {
                    //TODO fix
                    binding.customviewPiechartWeekly.setPercentAndBoardWidthAndProgressiveWidth(
                        0.10F,
                        20F,
                        10F
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWeeklyCalendarBinding.inflate(
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