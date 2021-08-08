package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.ItemDailyTasksBinding
import java.text.SimpleDateFormat

class DailyTaskAdapter : ListAdapter<TaskEntity, DailyTaskAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemDailyTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntity: TaskEntity) {
            if (taskEntity.isGoalSet) {
                binding.checkboxDailyTasks.visibility = VISIBLE
            } else {
                binding.checkboxDailyTasks.visibility = INVISIBLE
            }
            binding.textDailyTasks.text = taskEntity.task
            //TODO fix format
            val formatTime = SimpleDateFormat("HH:mm")
            val time = formatTime.format(taskEntity.taskTime)
            binding.timeDailyTasks.text = time
            binding.imagebuttonDailyTasks.isVisible = taskEntity.isCompleted.not()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDailyTasksBinding.inflate(
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