package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.entity.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.ItemDailyTasksBinding

class DailyTaskAdapter: ListAdapter<TaskEntity, DailyTaskAdapter.ViewHolder>(diffUtil){

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
//        val binding = ItemDailyTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return TaskHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
//        holder.onBindViewHolder(tasks[position])
//        holder.binding.executePendingBindings()
//    }
//
//    override fun getItemCount(): Int {
//        return tasks.size
//    }
//
//    inner class TaskHolder(val binding: ItemDailyTasksBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun onBindViewHolder(task: Task) {
//            binding.task = task
//        }
//    }
//}
inner class ViewHolder(private val binding: ItemDailyTasksBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(taskEntity: TaskEntity) {
        binding.checkboxDailyTasks.isVisible = taskEntity.isGoalSet
        binding.textDailyTasks.text = taskEntity.task
        //TODO fix format
        binding.timeDailyTasks.text = taskEntity.taskTime.toString()
        binding.imagebuttonDailyTasks.isVisible = taskEntity.isCompleted
    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDailyTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TaskEntity>() {
            override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}