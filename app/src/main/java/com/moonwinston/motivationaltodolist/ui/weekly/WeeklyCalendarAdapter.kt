package com.moonwinston.motivationaltodolist.ui.weekly

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.Task
import com.moonwinston.motivationaltodolist.databinding.ItemWeeklyCalendarBinding

class WeeklyCalendarAdapter(private val tasks: List<Task> = arrayListOf()) :
        RecyclerView.Adapter<WeeklyCalendarAdapter.TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val binding = ItemWeeklyCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //val holder = TaskHolder(binding)
        //val params = holder. as ConstraintLayout.LayoutParams
        //params.matchConstraintMaxHeight = (parent.height * 0.6).toInt()
        //holder.frame.layoutParams = params

        //val layoutParams = binding.root.layoutParams as ConstraintLayout.LayoutParams
        //layoutParams.width = parent.width * 0.14 as Int
        //binding.root.layoutParams

        return TaskHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.onBindViewHolder(tasks[position])
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
}