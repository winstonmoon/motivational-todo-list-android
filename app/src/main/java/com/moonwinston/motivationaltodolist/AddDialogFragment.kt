package com.moonwinston.motivationaltodolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.DialogAddBinding
import com.moonwinston.motivationaltodolist.utilities.CalendarUtil
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddDialogFragment : DialogFragment() {
    private lateinit var binding: DialogAddBinding
    private lateinit var date: Date
    private lateinit var dmlState: DmlState
    private lateinit var taskEntity: TaskEntity
    private val formatDate = SimpleDateFormat("yyyy-MM-dd")
    private val formatTime = SimpleDateFormat("HH:mm")
    private val sharedViewModel by sharedViewModel<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dmlState = arguments?.getParcelable<DmlState>("dmlState") as DmlState
        taskEntity = arguments?.getParcelable<TaskEntity>("taskEntity") as TaskEntity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = DialogAddBinding.inflate(layoutInflater)
            binding.inputTime.setIs24HourView(true)
            binding.viewCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val parsedMonth = resources.getString(MonthEnum.values()[month].monthNumber)
                date = formatDate.parse("$year-$parsedMonth-$dayOfMonth")
            }
            var positiveButton: Int? = null
            when (dmlState) {
                DmlState.Insert -> {
                    initInsert(binding)
                    positiveButton = R.string.button_add
                }
                DmlState.Update -> {
                    initUpdate(binding, taskEntity)
                    positiveButton = R.string.button_edit
                }
                else -> Unit
            }

            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            builder.setView(binding.root)
                .setPositiveButton(positiveButton ?: R.string.button_add,
                    DialogInterface.OnClickListener { _, _ ->
                        val hour by lazy {
                            if (Build.VERSION.SDK_INT <= 23) binding.inputTime.currentHour else binding.inputTime.hour
                        }
                        val minute by lazy {
                            if (Build.VERSION.SDK_INT <= 23) binding.inputTime.currentMinute else binding.inputTime.minute
                        }

                        var insertTaskEntity = TaskEntity(
                            uid = taskEntity?.uid,
                            taskDate = date,
                            taskTime = formatTime.parse("%02d:%02d".format(hour, minute)),
                            task = binding.inputTask.text.toString(),
                            isGoalSet = binding.switchGoalTask.isChecked,
                            isCompleted = false
                        )
                        sharedViewModel.insert(insertTaskEntity)
                    })
                .setNegativeButton(R.string.button_cancel,
                    DialogInterface.OnClickListener { _, _ ->
                        dialog?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initInsert(binding: DialogAddBinding) {
        if (Build.VERSION.SDK_INT <= 23) {
            binding.inputTime.currentHour
            binding.inputTime.currentMinute
        } else {
            binding.inputTime.hour
            binding.inputTime.minute
        }

        binding.viewCalendar.date = CalendarUtil.getToday().time
        date = CalendarUtil.getToday()
    }

    private fun initUpdate(binding: DialogAddBinding, taskEntity: TaskEntity) {
        if (Build.VERSION.SDK_INT <= 23) {
            binding.inputTime.currentHour = taskEntity.taskTime.hours
            binding.inputTime.currentMinute = taskEntity.taskTime.minutes
        } else {
            binding.inputTime.hour = taskEntity.taskTime.hours
            binding.inputTime.minute = taskEntity.taskTime.minutes
        }

        binding.viewCalendar.date = taskEntity.taskDate.time
        date = taskEntity.taskDate
        binding.inputTask.setText(taskEntity.task)
        binding.switchGoalTask.isChecked = taskEntity.isGoalSet
    }
}

