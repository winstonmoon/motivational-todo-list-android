package com.moonwinston.motivationaltodolist.adapters

import android.content.DialogInterface
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.ItemTasksBinding
import java.text.SimpleDateFormat

class TaskAdapter(
    val meatballsMenuCallback: (TaskEntity, DmlState) -> Unit,
    val radioButtonCallback: (TaskEntity) -> Unit
) : ListAdapter<TaskEntity, TaskAdapter.ViewHolder>(diffUtil) {
    val formatTime = SimpleDateFormat("HH:mm")

    inner class ViewHolder(private val binding: ItemTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntity: TaskEntity) {
            if (taskEntity.isCompleted) {
                binding.radiobuttonTasks.isChecked = true
                binding.textTasks.paintFlags = binding.textTasks.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.textTasks.text = taskEntity.task
                binding.meatballsmenuTasks.setOnClickListener {
                    Toast.makeText(it.context, R.string.message_toast_uneditable, Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.radiobuttonTasks.isChecked = false
                binding.textTasks.text = taskEntity.task
                binding.timeTasks.text = formatTime.format(taskEntity.taskTime)

                binding.radiobuttonTasks.setOnClickListener {
                    val builder = AlertDialog.Builder(it.context)
                    builder.setMessage(R.string.message_dialog_confirm_complete)
                        .setPositiveButton(R.string.button_ok,
                            DialogInterface.OnClickListener { _, _ ->
                                val insertTaskEntity = TaskEntity(
                                    uid = taskEntity.uid,
                                    taskDate = taskEntity.taskDate,
                                    taskTime = taskEntity.taskTime,
                                    task = taskEntity.task,
                                    isCompleted = true
                                )
                                radioButtonCallback(insertTaskEntity)
                            })
                        .setNegativeButton(R.string.button_cancel,
                            DialogInterface.OnClickListener { _, _ ->
                                binding.radiobuttonTasks.isChecked = false
                            })
                    builder.show()
                    radioButtonCallback(taskEntity)
                }

                binding.meatballsmenuTasks.setOnClickListener {
                    val popupMenu = PopupMenu(it.context, it)
                    popupMenu.menuInflater.inflate(R.menu.task_edit_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.edit ->
                                meatballsMenuCallback(taskEntity, DmlState.Update)
                            R.id.delete ->
                                meatballsMenuCallback(taskEntity, DmlState.Delete)
                        }
                        false
                    }
                    popupMenu.show()
                }
            }
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