package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.ItemWeeklyTasksBinding
import java.text.SimpleDateFormat

class WeeklyTaskAdapter (val callback: (TaskEntity, DmlState) -> Unit) : ListAdapter<TaskEntity, WeeklyTaskAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemWeeklyTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntity: TaskEntity) {
            binding.textWeeklyTasks.text = taskEntity.task

            val formatTime = SimpleDateFormat("HH:mm")
            val time = formatTime.format(taskEntity.taskTime)
            binding.timeWeeklyTasks.text = time

            binding.imagebuttonWeeklyTasks.setOnClickListener {
                val popupMenu = PopupMenu(it.context, it)
                popupMenu.menuInflater.inflate(R.menu.task_edit_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit ->
                            callback(taskEntity, DmlState.Update)
                        R.id.delete ->
                            callback(taskEntity, DmlState.Delete)
                    }
                    false
                }
                popupMenu.show()
            }
            binding.imagebuttonWeeklyTasks.isVisible = taskEntity.isCompleted.not()
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