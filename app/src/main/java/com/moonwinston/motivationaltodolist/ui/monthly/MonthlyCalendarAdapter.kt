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
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import java.text.SimpleDateFormat
import java.util.*

class MonthlyCalendarAdapter(monthTasksList: List<TaskEntity>) :
    ListAdapter<Date, MonthlyCalendarAdapter.ViewHolder>(diffUtil) {
    private val _monTasksList = monthTasksList

    inner class ViewHolder(private val binding: ItemMonthlyCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val nonExistDate = CalendarUtil.getNonExistDate()
        private val today = CalendarUtil.getTodayDate()
        private val monTasksList = _monTasksList

        fun bind(testDate: Date) {
            val tempTaskList = mutableListOf<TaskEntity>()
            for (date in monTasksList){
                if (date.taskDate == testDate) tempTaskList.add(date)
            }
            val rate = getRate(tempTaskList)

            //TODO
            val cal = Calendar.getInstance()
            cal.time = testDate

            when {
                testDate == nonExistDate -> {
                    binding.monthlyDateTextView.visibility = View.GONE
                    binding.monthlyCustomPieChart.visibility = View.GONE
                }
                testDate == today -> {
                    binding.monthlyDateTextView.setBackgroundResource(R.drawable.bg_shape_oval_red_22)
//                    binding.monthlyDateTextView.text = SimpleDateFormat("d").format(testDate)
                    binding.monthlyDateTextView.text = "${cal.get(Calendar.DAY_OF_MONTH)}"
                    if (rate == 0.0F) binding.monthlyCustomPieChart.alpha = 0.2F
                    binding.monthlyCustomPieChart.setPercentage(rate)
                }
                testDate.after(today) -> {
//                    binding.monthlyDateTextView.text = SimpleDateFormat("d").format(testDate)
                    binding.monthlyDateTextView.text = "${cal.get(Calendar.DAY_OF_MONTH)}"
                    binding.monthlyCustomPieChart.visibility = View.INVISIBLE
                }
                else -> {
//                    binding.monthlyDateTextView.text = SimpleDateFormat("d").format(testDate)
                    binding.monthlyDateTextView.text = "${cal.get(Calendar.DAY_OF_MONTH)}"
                    if (rate == 0.0F) binding.monthlyCustomPieChart.alpha = 0.2F
                    binding.monthlyCustomPieChart.setPercentage(rate)
                }
            }
        }

        private fun getRate(tasksList: List<TaskEntity>): Float {
            var totalTasks = 0F
            var doneTasks = 0F
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
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(oldItem: Date, newItem: Date): Boolean {
                return oldItem == newItem
            }
        }
    }
}