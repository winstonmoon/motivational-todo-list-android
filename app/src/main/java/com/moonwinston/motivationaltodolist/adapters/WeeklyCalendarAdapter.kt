package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.Task
import com.moonwinston.motivationaltodolist.databinding.ItemWeeklyCalendarBinding
import kotlinx.android.synthetic.main.item_weekly_calendar.view.*

class WeeklyCalendarAdapter(private val tasks: List<Task> = arrayListOf()) :
        RecyclerView.Adapter<WeeklyCalendarAdapter.TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val binding = ItemWeeklyCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        initDaySelectable(binding)
        return TaskHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.onBindViewHolder(tasks[position])
        holder.binding.pieChartWeeklyMon.setPercentAndBoardWidthAndProgressiveWidth(0.10F, 20F, 10F)
        holder.binding.pieChartWeeklyTue.setPercentAndBoardWidthAndProgressiveWidth(0.20F, 20F, 10F)
        holder.binding.pieChartWeeklyWed.setPercentAndBoardWidthAndProgressiveWidth(0.30F, 20F, 10F)
        holder.binding.pieChartWeeklyThu.setPercentAndBoardWidthAndProgressiveWidth(0.40F, 20F, 10F)
        holder.binding.pieChartWeeklyFri.setPercentAndBoardWidthAndProgressiveWidth(0.50F, 20F, 10F)
        holder.binding.pieChartWeeklySat.setPercentAndBoardWidthAndProgressiveWidth(0.60F, 20F, 10F)
        holder.binding.pieChartWeeklySun.setPercentAndBoardWidthAndProgressiveWidth(0.70F, 20F, 10F)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    inner class TaskHolder(val binding: ItemWeeklyCalendarBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBindViewHolder(task: Task) {
            binding.task = task
        }
    }

    private fun initDaySelectable(binding: ItemWeeklyCalendarBinding) {
        binding.pieChartWeeklyMon.setOnClickListener {
            Toast.makeText(it.context, "test", Toast.LENGTH_SHORT).show()
        }
        binding.pieChartWeeklyTue.setOnClickListener {
            Toast.makeText(it.context, "test", Toast.LENGTH_SHORT).show()
        }
        binding.pieChartWeeklyWed.setOnClickListener {
            Toast.makeText(it.context, "test", Toast.LENGTH_SHORT).show()
        }
        binding.pieChartWeeklyThu.setOnClickListener {
            Toast.makeText(it.context, "test", Toast.LENGTH_SHORT).show()
        }
        binding.pieChartWeeklyFri.setOnClickListener {
            Toast.makeText(it.context, "test", Toast.LENGTH_SHORT).show()
        }
        binding.pieChartWeeklySat.setOnClickListener {
            Toast.makeText(it.context, "test", Toast.LENGTH_SHORT).show()
        }
        binding.pieChartWeeklySun.setOnClickListener {
            Toast.makeText(it.context, "test", Toast.LENGTH_SHORT).show()
        }
    }
}