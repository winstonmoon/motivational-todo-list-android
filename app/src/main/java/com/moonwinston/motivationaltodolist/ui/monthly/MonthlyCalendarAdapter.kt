package com.moonwinston.motivationaltodolist.ui.monthly

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.ItemMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.utils.dateOfToday
import com.moonwinston.motivationaltodolist.utils.dateToLocalDate
import com.moonwinston.motivationaltodolist.utils.getDateExceptTime
import com.moonwinston.motivationaltodolist.utils.nonExistDate
import java.util.*

class MonthlyCalendarAdapter(monthTasksList: List<TaskEntity>) :
    ListAdapter<Date, MonthlyCalendarAdapter.ViewHolder>(diffUtil) {
    private val _monTasksList = monthTasksList

    inner class ViewHolder(private val binding: ItemMonthlyCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val monTasksList = _monTasksList

        fun bind(date: Date) {
            val tempTaskList = mutableListOf<TaskEntity>()
            monTasksList.forEach { taskEntity ->
                if (taskEntity.taskDate.getDateExceptTime() == date) tempTaskList.add(taskEntity)
            }
            val calculatedRate = calculateRate(tempTaskList)
            when {
                date == nonExistDate() -> {
                    binding.monthlyDateTextView.visibility = View.GONE
                    binding.monthlyCustomPieChart.visibility = View.GONE
                }
                date == dateOfToday() -> {
                    binding.monthlyDateTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_22)
                    binding.monthlyDateTextView.text = "${date.dateToLocalDate().dayOfMonth}"
                    if (calculatedRate == 0.0F) binding.monthlyCustomPieChart.alpha = 0.2F
                    binding.monthlyCustomPieChart.setPercentage(calculatedRate)
                }
                date.after(dateOfToday()) -> {
                    binding.monthlyDateTextView.text = "${date.dateToLocalDate().dayOfMonth}"
                    binding.monthlyCustomPieChart.visibility = View.INVISIBLE
                }
                else -> {
                    binding.monthlyDateTextView.text = "${date.dateToLocalDate().dayOfMonth}"
                    if (calculatedRate == 0.0F) binding.monthlyCustomPieChart.alpha = 0.2F
                    binding.monthlyCustomPieChart.setPercentage(calculatedRate)
                }
            }
        }

        private fun calculateRate(tasksList: List<TaskEntity>): Float {
            var totalTasks = 0F
            var doneTasks = 0F
            tasksList.forEach {  taskEntity ->
                totalTasks += 1F
                if (taskEntity.isCompleted) doneTasks += 1F
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
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(oldItem: Date, newItem: Date): Boolean {
                return oldItem == newItem
            }
        }
    }
}