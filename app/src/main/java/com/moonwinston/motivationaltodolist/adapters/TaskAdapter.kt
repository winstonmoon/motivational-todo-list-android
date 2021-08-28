package com.moonwinston.motivationaltodolist.adapters

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.ItemTasksBinding
import java.text.SimpleDateFormat

class TaskAdapter(
    val meatballsmenuCallback: (TaskEntity, DmlState) -> Unit,
    val radioButtonCalllback: (TaskEntity) -> Unit
) : ListAdapter<TaskEntity, TaskAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntity: TaskEntity) {
            binding.textTasks.text = taskEntity.task

            val formatTime = SimpleDateFormat("HH:mm")
            val time = formatTime.format(taskEntity.taskTime)
            binding.timeTasks.text = time

            binding.radiobuttonTasks.setOnClickListener {
                val builder = AlertDialog.Builder(it.context)
                builder.setMessage("test")
                    .setPositiveButton("ok",
                        DialogInterface.OnClickListener { _, _ ->
                            val insertTaskEntity = TaskEntity(
                                uid = taskEntity.uid,
                                taskDate = taskEntity.taskDate,
                                taskTime = taskEntity.taskTime,
                                task = taskEntity.task,
                                isCompleted = true
                            )
                            radioButtonCalllback(insertTaskEntity)
                        })
                    .setNegativeButton("cancel",
                        DialogInterface.OnClickListener { _, _ ->
                            binding.radiobuttonTasks.isChecked = false
                        })
                builder.create()
                builder.show()
                radioButtonCalllback(taskEntity)
            }

            binding.meatballsmenuTasks.setOnClickListener {
                val popupMenu = PopupMenu(it.context, it)
                popupMenu.menuInflater.inflate(R.menu.task_edit_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit ->
                            meatballsmenuCallback(taskEntity, DmlState.Update)
                        R.id.delete ->
                            meatballsmenuCallback(taskEntity, DmlState.Delete)
                    }
                    false
                }
                popupMenu.show()
            }
            binding.meatballsmenuTasks.isVisible = taskEntity.isCompleted.not()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTasksBinding.inflate(
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