package com.moonwinston.motivationaltodolist.ui.adapter

import android.content.DialogInterface
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
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
import com.moonwinston.motivationaltodolist.utils.dateOfToday
import com.moonwinston.motivationaltodolist.utils.dateToLocalDateTime
import com.moonwinston.motivationaltodolist.utils.getDateExceptTime

class TaskAdapter(
    val meatballsMenuCallback: (TaskEntity, DmlState) -> Unit,
    val radioButtonCallback: (TaskEntity) -> Unit
) : ListAdapter<TaskEntity, TaskAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntity: TaskEntity) {
//            val hour = taskEntity.taskDate.dateToLocalDateTime().hour
//            val minute = taskEntity.taskDate.dateToLocalDateTime().minute
            val hour = taskEntity.taskDate.hour
            val minute = taskEntity.taskDate.minute
            val taskTime = "$hour:$minute"

            if (taskEntity.isCompleted) {
                binding.taskRadioButton.isChecked = true
                binding.taskTextView.paintFlags =
                    binding.taskTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.taskTextView.text = taskEntity.task
                binding.timeTextView.text = taskTime
                binding.taskMeatballsMenu.setOnClickListener { view ->
                    showCopyPopupMenu(view, taskEntity)
                }
            } else {
                binding.taskRadioButton.isChecked = false
                binding.taskTextView.paintFlags = 0
                binding.taskTextView.text = taskEntity.task
                binding.timeTextView.text = taskTime
                binding.taskRadioButton.setOnClickListener { view ->
                    showAlertDialog(view = view, binding = binding, taskEntity = taskEntity)
                }
                binding.taskMeatballsMenu.setOnClickListener { view ->
                    showEditPopupMenu(view, taskEntity)
                }
            }
        }
    }

    private fun showAlertDialog(view: View, binding: ItemTasksBinding, taskEntity: TaskEntity) {
        val builder = AlertDialog.Builder(view.context, R.style.CustomAlertDialog)
        builder.setMessage(R.string.message_dialog_confirm_complete)
            .setPositiveButton(R.string.button_ok,
                DialogInterface.OnClickListener { _, _ ->
//                    if (taskEntity.taskDate.getDateExceptTime().after(dateOfToday())) {
                    if (taskEntity.taskDate.getDateExceptTime().isAfter(dateOfToday())) {
                        Toast.makeText(view.context, R.string.message_toast_uncompletable, Toast.LENGTH_LONG).show()
                        return@OnClickListener
                    }
                    val taskEntity = TaskEntity(
                        uid = taskEntity.uid,
                        taskDate = taskEntity.taskDate,
                        task = taskEntity.task,
                        isCompleted = true
                    )
                    radioButtonCallback(taskEntity)
                })
            .setNegativeButton(R.string.button_cancel) { _, _ ->
                binding.taskRadioButton.isChecked = false
            }
        builder.setOnDismissListener { binding.taskRadioButton.isChecked = false }
        builder.show()
    }

    fun showCopyPopupMenu(view: View, taskEntity: TaskEntity) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.task_copy_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.copy -> meatballsMenuCallback(taskEntity, DmlState.Insert(method = "copy"))
            }
            false
        }
        popupMenu.show()
    }

    fun showEditPopupMenu(view: View, taskEntity: TaskEntity) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.task_edit_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.copy -> meatballsMenuCallback(taskEntity, DmlState.Insert(method = "copy"))
                R.id.edit -> meatballsMenuCallback(taskEntity, DmlState.Update)
                R.id.delete -> meatballsMenuCallback(taskEntity, DmlState.Delete)
            }
            false
        }
        popupMenu.show()
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
                return oldItem.task == newItem.task
            }

            override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}