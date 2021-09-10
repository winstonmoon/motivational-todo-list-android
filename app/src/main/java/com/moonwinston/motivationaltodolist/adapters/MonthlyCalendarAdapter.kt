package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.ItemMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.utilities.CalendarUtil
import java.text.SimpleDateFormat
import java.util.*

class MonthlyCalendarAdapter(monthTasksList: List<TaskEntity>) :
    ListAdapter<Date, MonthlyCalendarAdapter.ViewHolder>(diffUtil) {
    private val testList = monthTasksList

    inner class ViewHolder(private val binding: ItemMonthlyCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val nonExistDate = CalendarUtil.getNonExistDate()
        private val today = CalendarUtil.getTodayDate()
        private val testtList = testList

        fun bind(testDate: Date) {
            binding.customviewPiechartMonthly.apply {
                setBorderStrokeWidth(10F)
                setProgressiveStrokeWidth(5F)
            }
            var testteList = mutableListOf<TaskEntity>()
            for (date in testtList){
                if (date.taskDate == testDate) {
                    testteList.add(date)
                }
            }
            val rate = getRate(testteList)

            when {
                testDate == nonExistDate -> {
                    binding.textMonthlyDate.visibility = View.GONE
                    binding.customviewPiechartMonthly.visibility = View.GONE
                }
                testDate == today -> {
                    binding.textMonthlyDate.setBackgroundResource(R.drawable.bg_shape_oval_red_22)
                    binding.textMonthlyDate.text = SimpleDateFormat("d").format(testDate)
                    if (rate == 0.0F) {
                        binding.customviewPiechartMonthly.alpha = 0.2F
                    }
                    binding.customviewPiechartMonthly.setPercentage(rate)
                }
                testDate.after(today) -> {
                    binding.textMonthlyDate.text = SimpleDateFormat("d").format(testDate)
                    binding.customviewPiechartMonthly.visibility = View.INVISIBLE
                }
                else -> {
                    binding.textMonthlyDate.text = SimpleDateFormat("d").format(testDate)
                    if (rate == 0.0F) {
                        binding.customviewPiechartMonthly.alpha = 0.2F
                    }
                    binding.customviewPiechartMonthly.setPercentage(rate)
                }
            }
        }

        private fun getRate(tasksList: List<TaskEntity>): Float {
            var totalTasks: Float = 0F
            var doneTasks: Float = 0F
            for (task in tasksList) {
                totalTasks += 1F
                if (task.isCompleted) doneTasks += 1F
            }
            return if (doneTasks == 0F) 0F else doneTasks / totalTasks
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
        val diffUtil = object : DiffUtil.ItemCallback<Date>() {
            override fun areItemsTheSame(oldItem: Date, newItem: Date): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Date, newItem: Date): Boolean {
                return oldItem == newItem
            }
        }
    }
}