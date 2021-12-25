package com.moonwinston.motivationaltodolist.ui.shared

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
import java.util.*

class TaskAdapter(
    val meatballsMenuCallback: (TaskEntity, DmlState) -> Unit,
    val radioButtonCallback: (TaskEntity) -> Unit
) : ListAdapter<TaskEntity, TaskAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntity: TaskEntity) {
            if (taskEntity.isCompleted) {
                binding.taskRadioButton.isChecked = true
                binding.taskTextView.paintFlags =
                    binding.taskTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.taskTextView.text = taskEntity.task
                binding.taskMeatballsMenu.setOnClickListener {
//                    Toast.makeText(
//                        it.context,
//                        R.string.message_toast_uneditable,
//                        Toast.LENGTH_SHORT
//                    ).show()
                    val popupMenu = PopupMenu(it.context, it)
                    popupMenu.menuInflater.inflate(R.menu.task_copy_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.copy ->
                                meatballsMenuCallback(taskEntity, DmlState.Insert("copy"))
                        }
                        false
                    }
                    popupMenu.show()
                }
                binding.timeTextView.text = SimpleDateFormat("HH:mm").format(taskEntity.taskTime)
            } else {
                binding.taskRadioButton.isChecked = false
                //TODO fix
                binding.taskTextView.paintFlags = 0
                binding.taskTextView.text = taskEntity.task
                binding.timeTextView.text = SimpleDateFormat("HH:mm").format(taskEntity.taskTime)

                binding.taskRadioButton.setOnClickListener {
                    val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
                    builder.setMessage(R.string.message_dialog_confirm_complete)
                        .setPositiveButton(R.string.button_ok,
                            DialogInterface.OnClickListener { _, _ ->
                                if (taskEntity.taskDate.after(Date())) {
                                    Toast.makeText(it.context, R.string.message_toast_uncompletable, Toast.LENGTH_LONG).show()
                                    return@OnClickListener
                                }
                                val taskEntity = TaskEntity(
                                    uid = taskEntity.uid,
                                    taskDate = taskEntity.taskDate,
                                    taskTime = taskEntity.taskTime,
                                    task = taskEntity.task,
                                    isCompleted = true
                                )
                                radioButtonCallback(taskEntity)
                            })
                        .setNegativeButton(R.string.button_cancel,
                            DialogInterface.OnClickListener { _, _ ->
                                binding.taskRadioButton.isChecked = false
                            })
                    builder.setOnDismissListener { binding.taskRadioButton.isChecked = false }
                    builder.show()
                }

                binding.taskMeatballsMenu.setOnClickListener {
                    val popupMenu = PopupMenu(it.context, it)
                    popupMenu.menuInflater.inflate(R.menu.task_edit_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.copy ->
                                meatballsMenuCallback(taskEntity, DmlState.Insert("copy"))
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