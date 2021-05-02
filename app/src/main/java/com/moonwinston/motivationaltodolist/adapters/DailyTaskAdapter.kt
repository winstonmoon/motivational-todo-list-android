package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.Task
import com.moonwinston.motivationaltodolist.databinding.ItemDailyTasksBinding

class DailyTaskAdapter(private val tasks: List<Task> = arrayListOf()) :
        RecyclerView.Adapter<DailyTaskAdapter.TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val binding = ItemDailyTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.onBindViewHolder(tasks[position])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    inner class TaskHolder(val binding: ItemDailyTasksBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBindViewHolder(task: Task) {
            binding.task = task
        }
    }
}