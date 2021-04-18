package com.moonwinston.motivationaltodolist.ui.weekly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.Task
import com.moonwinston.motivationaltodolist.databinding.ItemWeeklyTasksBinding

class WeeklyTaskAdapter(private val tasks: List<Task> = arrayListOf()) :
RecyclerView.Adapter<WeeklyTaskAdapter.TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val binding = ItemWeeklyTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.onBindViewHolder(tasks[position])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    inner class TaskHolder(val binding: ItemWeeklyTasksBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBindViewHolder(task: Task) {
            binding.task = task
        }
    }
}