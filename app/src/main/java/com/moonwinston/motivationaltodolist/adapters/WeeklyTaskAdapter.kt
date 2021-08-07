package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.ItemWeeklyTasksBinding

class WeeklyTaskAdapter : ListAdapter<TaskEntity, WeeklyTaskAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemWeeklyTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntity: TaskEntity) {
//            binding.checkboxDailyTasks.isVisible = taskEntity.isGoalSet
            binding.textDailyTasks.text = taskEntity.task
            //TODO fix format
            binding.timeDailyTasks.text = taskEntity.taskTime.toString()
//            binding.imagebuttonWeeklyTasks.isVisible = taskEntity.isCompleted
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemWeeklyTasksBinding.inflate(
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