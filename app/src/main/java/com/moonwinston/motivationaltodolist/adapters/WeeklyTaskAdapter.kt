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
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.ItemWeeklyTasksBinding
import java.text.SimpleDateFormat

class WeeklyTaskAdapter : ListAdapter<TaskEntity, WeeklyTaskAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemWeeklyTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntity: TaskEntity) {
            if (taskEntity.isGoalSet) {
                binding.checkboxWeeklyTasks.visibility = View.VISIBLE
            } else {
                binding.checkboxWeeklyTasks.visibility = View.INVISIBLE
            }
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
                            //TODO edit task function
                            Toast.makeText(it.context, "edit", Toast.LENGTH_SHORT).show()
                        R.id.delete ->
                            //TODO delete task function
                            Toast.makeText(it.context, "delete", Toast.LENGTH_SHORT).show()
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