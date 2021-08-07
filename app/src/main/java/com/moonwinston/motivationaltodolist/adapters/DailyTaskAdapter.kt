package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.ItemDailyTasksBinding

class DailyTaskAdapter : ListAdapter<TaskEntity, DailyTaskAdapter.ViewHolder>(diffUtil) {

    private var taskList: List<TaskEntity> = listOf()

    inner class ViewHolder(private val binding: ItemDailyTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntity: TaskEntity) {
//        binding.checkboxDailyTasks.isVisible = taskEntity.isGoalSet
            binding.textDailyTasks.text = taskEntity.task
            //TODO fix format
            binding.timeDailyTasks.text = taskEntity.taskTime.toString()
//        binding.imagebuttonDailyTasks.isVisible = taskEntity.isCompleted
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
        holder.bind(taskList[position])
    }

    fun setTask(taskList: List<TaskEntity>) {
        this.taskList = taskList
        notifyDataSetChanged()
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